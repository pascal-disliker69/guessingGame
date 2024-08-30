package logic.exceptions;

/**
 * Fehler der auftritt, wenn der Name eines Eintrags in dem Highscore Array einer geladenen Datei leer ist.
 *
 * @author nvk
 */
public class EmptyNameException extends PositionException {

    public EmptyNameException(String message, int position) {
        super(message, position);
    }

}
