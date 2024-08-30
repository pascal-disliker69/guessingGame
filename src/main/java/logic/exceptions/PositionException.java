package logic.exceptions;

/**
 * Eine abstrakte Exception für Exceptions, die sich auf eine bestimmte Position beziehen.
 *
 * @author nvk
 */
public abstract class PositionException extends Exception {

    /**
     * Die Position, an der der Fehler aufgetreten ist.
     */
    private final int position;

    public PositionException(String message, int position) {
        super(message);
        this.position = position;
    }

    /**
     * Liefert die Position, an der der Fehler aufgetreten ist.
     * @return die Position.
     */
    public int getPosition() {
        return position;
    }
}
