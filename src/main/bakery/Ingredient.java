package bakery;

import java.lang.Object;
import java.lang.Comparable;
import java.io.Serializable;

public class Ingredient implements Comparable<Ingredient>, Serializable{
    private String name;
    public static final Ingredient HELPFUL_DUCK=new Ingredient("helpful duck 𓅭");

    private static final long serialVersionUID=42L;

    /**
     * Initialises the name of the ingredient.
     * 
     * @param name The name of the ingredient.
     */
    public Ingredient(String name){
        this.name=name;
    }
    
    /**
     * Checks if Object o is equal to the this ingredient.
     * 
     * @param o The object to compare to.
     * @return True, if the object's string representation is equal to the ingredient's name, False, otherwise.
     */
    @Override
    public boolean equals(Object o){
        if((this.toString()).equals(o.toString())){
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Produces a hash code to be used for the hash set or hash map data structures.
     * 
     * @return A hash code based on the name of the ingredient.
     */
    @Override
    public int hashCode(){
        return this.name.hashCode();
    }

    /**
     * Returns the name of the ingredient.
     * 
     * @return The name of the ingredient.
     */
    public String toString(){
        return this.name;
    }

    /**
     * Compares this ingredient with another ingredient o, based on their names.
     * 
     * @return An integer representing the comparison of this ingredient with ingredient o,
     * with respect to their names.
     */
    @Override
    public int compareTo(Ingredient o){
        return (this.toString()).compareTo(o.toString());
    }
}
