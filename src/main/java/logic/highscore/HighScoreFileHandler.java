package logic.highscore;

import java.util.ArrayList;

/**
 * Klasse, die die Struktur einer JSON-Datei abbildet, mit der eine
 * Highscore Liste geladen und verwendet bzw gespeichert
 * werden kann.
 *
 * @author Annabella Hamperl
 */
public class HighScoreFileHandler {
    /**
     * Liste mit den Highscores.
     */
    public ArrayList<HighScore> highScores;

    /**
     * Untere Grenze des Bereichs aus dem die Zufallszahlen
     * stammen und in dem der Spieler raten muss.
     */
    private int lowerLimit;
    private int upperLimit;

    /**
     * Konstruktor, der eine leere Highscore Liste erstellt.
     */
    public HighScoreFileHandler() {}

    /**
     * Konstruktor, der eine Highscore Liste mit den übergebenen
     * Highscores erstellt.
     * @return
     */
    public HighScoreList getHighScores() {
        return new HighScoreList(highScores.toArray(new HighScore[0]));
    }

    /**
     * Setzt die Highscore Liste auf die übergebene Liste.
     * @param highScores die neue Highscore Liste
     */
    public void setHighScores(ArrayList<HighScore> highScores) {
        this.highScores = highScores;
    }

    /**
     * Liefert die untere Grenze des Bereichs aus dem die Zufallszahlen
     * @return lowerLimit die untere Grenze
     */
    public int getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Setzt die untere Grenze des Bereichs aus dem die Zufallszahlen
     * @param lowerLimit die neue untere Grenze
     */
    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * Liefert die obere Grenze des Bereichs aus dem die Zufallszahlen
     * @return upperLimit die obere Grenze
     */
    public int getUpperLimit() {
        return upperLimit;
    }

    /**
     * Setzt die obere Grenze des Bereichs aus dem die Zufallszahlen
     * @param upperLimit die neue obere Grenze
     */
    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * Liefert die Highscore Liste als String.
     *
     * @return die Highscore Liste als String
     */
    @Override
    public String toString() {
        return "HighScoreData{" +
                "highScores=" + highScores +
                ", lowerLimit=" + lowerLimit +
                ", upperLimit=" + upperLimit +
                '}';
    }
}
