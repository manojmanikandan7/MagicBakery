package bakery;

import java.lang.IllegalArgumentException;

public class WrongIngredientsException extends IllegalArgumentException{
    public WrongIngredientsException(){
        super();
    }
    public WrongIngredientsException(String message){
        super(message);
    }
}
