package bakery;

import java.lang.Object;
import java.lang.Comparable;
import java.io.Serializable;

/**
 * A Class representing the ingredient to make a customer order.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class Ingredient implements Comparable<Ingredient>, Serializable {
    private String name;

    /**
     * A constant representing the "Helpful Duck" card in the game.
     */
    public static final Ingredient HELPFUL_DUCK = new Ingredient("helpful duck 𓅭");

    private static final long serialVersionUID = 42L;

    /**
     * Initialises the name of the ingredient.
     * 
     * @param name The name of the ingredient.
     */
    public Ingredient(String name) {
        this.name = name;
    }

    /**
     * Checks if Object o is equal to this ingredient.
     * 
     * @param o The object to compare to.
     * @return True, if the object's string representation is equal to the
     *         ingredient's name, False, otherwise.
     */
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        return (this.toString()).equals(o.toString());
    }

    /**
     * Produces a hash code to be used for the hash set or hash map data structures.
     * 
     * @return A hash code based on the name of the ingredient.
     */
    public int hashCode() {
        return this.name.hashCode();
    }

    /**
     * Returns the name of the ingredient.
     * 
     * @return The name of the ingredient.
     */
    public String toString() {
        return this.name;
    }

    /**
     * Compares this ingredient with another ingredient o, based on their names.
     *
     * @param o The other object to be compared with.
     * @return An integer representing the comparison of this ingredient with
     *         ingredient o,
     *         with respect to their names.
     */
    public int compareTo(Ingredient o) {
        return (this.toString()).compareTo(o.toString());
    }
}
