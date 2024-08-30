package logic.exceptions;

/**
 * Fehler der auftritt, wenn der Punktestand eines Eintrags in dem Highscore Array einer geladenen Datei negativ ist.
 *
 * @author nvk
 */
public class NegativScoreException extends PositionException{

    /**
     * Der negative Score.
     */
    private final int negativScore;

    public NegativScoreException(String message, int position, int negativScore) {
        super(message, position);
        this.negativScore = negativScore;
    }

    /**
     * Liefert den negativen Score zurück, der zu dem Fehler geführt hat.
     * @return der negative Score.
     */
    public int getNegativScore() {
        return negativScore;
    }
}
