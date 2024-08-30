package logic.highscore;

import java.util.ArrayList;

/**
 * Eine Liste von HighScores. Die Liste ist nach den Punkten absteigend sortiert. Jeder Spieler (Name) darf nur
 * einmal in der Liste vorkommen.
 *
 * @author nvk, Annabella Hamperl
 */
public class HighScoreList {

    /**
     * Die Liste zur Verwaltung der HighScores.
     */
    private final ArrayList<HighScore> highScores;

    /**
     * Erstellt eine leere HighScoreList.
     */
    public HighScoreList() {
        highScores = new ArrayList<>();
    }

    /**
     * Erstellt eine neue HighScoreList mit den Namen und Punktzahlen aus dem übergebenen Array.
     *
     * @param scores die Namen und Punkte der Spieler.
     */
    public HighScoreList(HighScore[] scores) {
        highScores = new ArrayList<>();
        for (HighScore score : scores) {
            add(score);
        }
    }

    /**
     * Liefert die sortierte List der Highscores, hauptsächlich um sie zur Anzeige an die GUI zu übermitteln.
     *
     * @return die Liste der Highscores.
     */
    public ArrayList<HighScore> getHighScores() {
        return highScores;
    }

    /**
     * Liefert den HighScore an dem übergebenen Index zurück.
     *
     * @param index der Index des HighScores.
     * @return der HighScore an der Stelle index.
     */
    public HighScore getHighScoreAt(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return highScores.get(index);
    }

    /**
     * Liefert die Länge der Liste.
     *
     * @return die Länge der Liste.
     */
    public int size() {return highScores.size();}

    /**
     * Fügt einen neuen HighScore absteigend nach der Punktzahl sortiert in die Liste ein. Jeder Name darf nur
     * einmal vorkommen. Gibt es in der Liste bereits einen HighScore mit dem selben Namen wie in dem übergebenen
     * HighScore wird der neue HighScore nur übernommen, wenn die Punktezahl des Spielers höher ist als die
     * bisherige. In diesem Fall wird der alte Eintrag gelöscht und der neue HighScore übernommen. Ansonsten bleibt
     * die Liste unverändert.
     *
     * @param score der einzufügende HighScore;
     */
    public void add(HighScore score) {

        //TODO nnochmal aufräumen aber hier
        int idxPlayerInList = 0;
        int newIdx = 0;
        boolean alrInList = false;

        if (highScores.isEmpty()) {
            highScores.add(score);
        } else {
        while ((idxPlayerInList < size() && !alrInList)) {
            if (highScores.get(idxPlayerInList).name().equals(score.name())) {
                alrInList = true;
            } else {
                idxPlayerInList++;
            }
        }

        if (alrInList) {
                if (highScores.get(idxPlayerInList).score() < score.score()) {
                    highScores.remove(idxPlayerInList);
                    while (newIdx < highScores.size() && score.score() <= highScores.get(newIdx).score()) {
                        newIdx++;
                    }
                        highScores.add(newIdx, score);
                    System.out.println("added score" + score);
                }
        } else {
            while (newIdx < highScores.size() && score.score() <= highScores.get(newIdx).score()) {
                newIdx++;
            }
            highScores.add(newIdx, score);
        }
        }
    }
}
