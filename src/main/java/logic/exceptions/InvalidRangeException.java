package logic.exceptions;

/**
 * Fehler der auftritt, wenn der Bereich für die Zufallszahl einer geladenen Datei invalide ist.
 *
 * @author nvk
 */
public class InvalidRangeException extends  Exception {

    /**
     * Die untere Grenze, bei der der Fehler aufgetreten ist.
     */
    private final int lowerLimit;

    /**
     * Die obere Grenze, bei der der Fehler aufgetreten ist.
     */
    private final int upperLimit;

    public InvalidRangeException(String message, int lowerLimit, int upperLimit) {
        super(message);
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    /**
     * Gibt die untere Grenze, bei der der Fehler aufgetreten ist, zurück.
     * @return die untere Grenze.
     */
    public int getLowerLimit() {
        return lowerLimit;
    }

    /**
     * Gibt die obere Grenze, bei der der Fehler aufgetreten ist, zurück.
     * @return die obere Grenze.
     */
    public int getUpperLimit() {
        return upperLimit;
    }
}
