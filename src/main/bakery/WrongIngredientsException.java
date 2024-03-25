package bakery;

import java.lang.IllegalArgumentException;

/**
 * Exception to be thrown if a wrong or missing ingredient is used.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class WrongIngredientsException extends IllegalArgumentException{
    /**
     * Thrown if the ingredient used is invalid for the particular case.
     */
    public WrongIngredientsException(){
        super();
    }
    /**
     * Thrown if the ingredient used is invalid for the particular case.
     *
     * @param message The message to be printed out when the exception occurs.
     */
    public WrongIngredientsException(String message){
        super(message);
    }
}
