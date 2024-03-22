package bakery;

import java.lang.RuntimeException;

public class EmptyPantryException extends RuntimeException{

    public EmptyPantryException(String msg, Throwable e){
        super(msg, e);
    }
}
