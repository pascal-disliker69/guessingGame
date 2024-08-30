package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.highscore.HighScore;
import logic.highscore.HighScoreFileHandler;
import logic.highscore.HighScoreList;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Die Hauptklasse der Spiellogik. Enthält die nötige Logik und die Attribute um das Spiel spielen zu können.
 * Dem Spieler wird eine zufällige Zahl angezeigt. Dieser muss nun raten ob die nächste Zufallszahl höher oder
 * niedriger ist als die letzte. Liegt er richtig erhält er einen Punkt und kann von der neuen Zahl aus erneut
 * raten. Liegt er falsch endet das Spiel. Ziel ist es eine möglichst hohe Punktzahl zu erreichen. Für jeden
 * Spieler wird die höchste Punktzahl gespeichert und die besten Spieler werden auf der GUI angezeigt.
 *
 * @author nvk, Annabella Hamperl
 */
public class Game {

    /**
     * Die GUI auf der das Spiel für den Spieler dargestellt wird.
     */
    private final GUIConnector gui;

    /**
     * Die untere Grenze des Bereichs aus dem die Zufallszahlen
     * stammen und in dem der Spieler raten muss.
     */
    private int lowerLimit;
    private int upperLimit;

    /**
     * Die aktuelle Nummer von der aus geraten werden soll, die
     * nächste Nummer die geraten werden soll
     * der aktuelle Punktestand
     * und der Name des aktuellen Spielers.
     */
    private int currentNumber;
    private int newNumber;
    private int currentScore;
    private String currentPlayer;

    /**
     * Die Highscore Liste und der aktuelle Highscore des Spielers.
     */
    private HighScoreList highScores;
    private HighScore currentHighScore;

    /**
     * Gibt an ob der Spieler falsch geraten hat.
     */
    private boolean wrongGuess;

    /**
     * Erstellt ein neues Spiel mit default Werten und einer leeren Highscore Liste.
     *
     * @param gui die GUI auf der das Spiel angezeigt wird.
     */
    public Game(GUIConnector gui) {
        this.gui = gui;
        this.lowerLimit = 1;
        this.upperLimit = 10;
        this.currentNumber = getRandomNumber(lowerLimit, upperLimit);
        this.newNumber = getRandomNumber(lowerLimit, upperLimit);
        this.currentScore = 0;
        this.currentPlayer = "";
        this.highScores = new HighScoreList();
        this.currentHighScore = new HighScore("", 0);
        this.wrongGuess = false;
    }

    /**
     * Startet ein neues Spiel mit dem übergebenen Namen als Spieler. Der Punktestand wird auf 0 gesetzt und eine
     * Zufallszahl erzeugt.
     *
     * @param name der Name des Spielers.
     */
    public void startNewGame(String name) {
        this.currentPlayer = name;
        this.currentScore = 0;
        this.currentHighScore = new HighScore(name, 0);
        this.currentNumber = getRandomNumber(lowerLimit, upperLimit);
        this.newNumber = getRandomNumber(lowerLimit, upperLimit);
        this.gui.showNumber(currentNumber);
        this.gui.updateCurrentScore(currentScore);
        this.gui.updateRange(lowerLimit, upperLimit);
        this.wrongGuess = false;
    }

    /**
     * Beendet das aktuelle Spiel. Der erreichte Highscore wird in die Liste übernommen, sofern der Spieler nicht
     * bereits einen höheren Score in der Liste hat.
     */
    public void endGame() {
        currentHighScore = new HighScore(currentPlayer, currentScore);
        highScores.add(currentHighScore);
        this.gui.updateHighScores(highScores.getHighScores());

        this.gui.handleEndOfGame(currentScore, wrongGuess,
                newNumber);
    }

    /**
     * Speichert die aktuelle HighScoreListe und ihre Konfiguration ab.
     * @param saveFile die Datei in die die HighScoreListe gespeichert
     *                 werden soll.
     * @throws IOException wenn die Datei nicht gespeichert werden
     * kann.
     */
    public void saveScore(File saveFile) throws IOException {
        if (saveFile != null) {
            if (!saveFile.getName().endsWith(".json")) {
                saveFile = new File(saveFile.getAbsolutePath() +
                        ".json");
            }
            HighScoreFileHandler highScoreFileHandler = new HighScoreFileHandler();
            highScoreFileHandler.setHighScores(getHighScores());
            highScoreFileHandler.setLowerLimit(getLowerLimit());
            highScoreFileHandler.setUpperLimit(getUpperLimit());

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter(saveFile)) {
                gson.toJson(highScoreFileHandler, writer);
                System.out.println("Highscore data successfully saved to " +
                        saveFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Fehler beim Speichern der Datei " +
                        e.getMessage());
                throw e;
            }
        }   else {
            System.err.println("Provided file is null.");
            throw new IllegalArgumentException("Provided file is null.");
        }
    }

    /**
     * Hilfsmethode, die nach einem richtigen Zug den aktuellen
     * Punktestand erhöht und die aktuelle und nächste Nummer
     * aktualisiert
     */
    public void guessNumber() {
            currentScore++;
            currentNumber = newNumber;
            gui.showNumber(currentNumber);
            gui.updateCurrentScore(currentScore);
            newNumber = getRandomNumber(lowerLimit, upperLimit);
    }

    /**
     * Hilfsmethode, die verarbeitet, wenn der Spieler die gleiche
     * Zahl nochmal geraten hat.
     */
    public void guessSame() {
        gui.handleSameNumber(newNumber);
        currentNumber = newNumber;
        newNumber = getRandomNumber(lowerLimit, upperLimit);
    }

    /**
     * Der Spieler hat geraten, dass die nächste Zahl höher ist.
     */
    public void guessHigher() {
        gui.resetInfo();
        if (newNumber == currentNumber) {
            guessSame();
        }
        else if (newNumber > currentNumber) {
            guessNumber();
        } else {
            wrongGuess = true;
            endGame();
        }
    }

    /**
     * Der Spieler hat geraten, dass die nächste Zahl niedriger ist.
     */
    public void guessLower() {
        gui.resetInfo();
        if (newNumber == currentNumber) {
            guessSame();
        }
        else if (newNumber < currentNumber) {
            guessNumber();
        } else {
            wrongGuess = true;
            endGame();
        }
    }

    /**
     * Hilfsmethode, die eine zufällige Zahl im Bereich zwischen
     * lowerLimit und upperLimit zurückgibt.
     * @param lowerLimit die untere Grenze des Bereichs
     * @param upperLimit die obere Grenze des Bereichs
     * @return eine zufällige Zahl im Bereich
     */
    public int getRandomNumber(int lowerLimit, int upperLimit) {
        return (int) (Math.random() * (upperLimit - lowerLimit + 1) +
                lowerLimit);
    }

    /**
     * Getter für den aktuellen Highscore.
     * @return der aktuelle Highscore
     */
    public HighScore getCurrentHighScore() {
        return currentHighScore;
    }

    /**
     * Getter für den aktuellen Punktestand.
     * @return der aktuelle Punktestand
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Getter für die nächste Nummer.
     * @return die nächste Nummer
     */
    public int getNewNumber() {
        return newNumber;
    }

    /**
     * Getter für den aktuellen Spieler.
     * @return Namen des aktuellen Spielers
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter für die Highscore Liste.
     * Setzt die aktuelle Highscore Liste auf die übergebene Liste.
     * @param highScores eine Highscore Liste
     */
    public void setHighScores(HighScoreList highScores) {
        this.highScores = highScores;
    }

    /**
     * Setter für die Grenzen Zufallsbereichs
     * @param lowerLimit die untere Grenze des Bereichs
     * @param upperLimit die obere Grenze des Bereichs
     */
    public void setLimits(int lowerLimit, int upperLimit) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /**
     * Getter für die Highscore Liste.
     * @return die Highscore Liste
     */
    public ArrayList<HighScore> getHighScores() {
        return highScores.getHighScores();
    }

    /**
     * Getter für die untere Grenze des Zufallsbereichs
     * @return die untere Grenze des Bereichs
     */
    public int getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Getter für die obere Grenze des Zufallsbereichs
     * @return die obere Grenze des Bereichs
     */
    public int getUpperLimit() {
        return upperLimit;
    }
}
