package logic;

import logic.highscore.HighScore;

import java.util.List;

/**
 * Interface das von der Logik genutzt wird um mit der GUI zu kommunizieren.
 *
 * @author nvk
 */
public interface GUIConnector {

    /**
     * Zeigt die aktuelle Nummer von der aus geraten werden soll an.
     *
     * @param number die aktuelle Nummer.
     */
    void showNumber(int number);

    /**
     * Aktualisiert die Anzeige der HighScores auf der GUI.
     *
     * @param highScores nach Punkten absteigend sortierte Liste mit den Namen und Punkten der Spieler.
     */
    void updateHighScores(List<HighScore> highScores);

    /**
     * Aktualisiert den aktuellen Punktestand auf der GUI.
     *
     * @param score der aktuelle Punktestand.
     */
    void updateCurrentScore(int score);

    /**
     * Aktualisiert die Anzeige des Bereichs aus dem die Zufallszahlen stammen und in dem der Spieler raten muss.
     *
     * @param lowerLimit untere Grenze des Ratebereichs (inklusive)
     * @param upperLimit obere Grenze des Ratebereichs (inklusive)
     */
    void updateRange(int lowerLimit, int upperLimit);

    /**
     * Beendet das Spiel und zeigt das Ergebnis an. Die angezeigte Nachricht variiert je nachdem ob der Spieler falsch
     * geraten hat oder das Spiel auf andere Weise beendet wurde.
     *
     * @param score das Endergebnis.
     * @param wrongGuess Gibt an ob der Spieler falsch geraten hat (true) oder das Spiel anders beendet wurde.
     * @param number die nächste Zahl.
     */
    void handleEndOfGame(int score, boolean wrongGuess, int number);

    /**
     * Behandelt den Fall, dass die gleiche Zahl nochmal gewürfelt wurde. Der Spieler erhält dann keine Punkte, darf aber nochmal raten.
     *
     * @param number die Zahl, die erneut gewürfelt wurde.
     */
    void handleSameNumber(int number);

    /**
     * Zeigt eine geeignete Fehlermeldung für den Nutzer an, basierend auf der Typ der übergebenen Exception. Dabei wird
     * NICHT der Detailtext der Exception verwendet.
     *
     * @param e der Fehler, der aufgetreten ist.
     */
    void showException(Exception e);

    void resetInfo();
}
