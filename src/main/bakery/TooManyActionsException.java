package bakery;

import java.lang.IllegalStateException;

/**
 * Exception to be thrown if the current player tries to make moves after their turn.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class TooManyActionsException extends IllegalStateException{
    /**
     * Thrown if the current player tries to make more moves after his turn.
     */
    public TooManyActionsException(){
        super();
    }
    /**
     * Thrown if the current player tries to make more moves after his turn.
     *
     * @param message The message to be printed out when the exception occurs.
     */
    public TooManyActionsException(String message){
        super(message);
    }
}
