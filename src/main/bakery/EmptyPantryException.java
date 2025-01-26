package bakery;

import java.lang.RuntimeException;

/**
 * Exception to be thrown if a card is taken from an empty pantry.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class EmptyPantryException extends RuntimeException {
    /**
     * Thrown if the pantry is empty and there is an attempt to take a card from it.
     *
     * @param msg The message to be printed if the exception is thrown.
     * @param e   The exception to throw when this exception is thrown.
     */
    public EmptyPantryException(String msg, Throwable e) {
        super(msg, e);
    }
}
