package bakery;

import util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

/**
 * A Class representing the player of the game.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class Player implements Serializable {
    private List<Ingredient> hand;
    private String name;

    private static final long serialVersionUID = 42L;

    /**
     * Initialises the name of the player.
     * 
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        hand = new ArrayList<Ingredient>();
    }

    /**
     * Adds the list of ingredients to the player's hand.
     *
     * @param ingredients The list of ingredients to be added to the hand.
     */
    public void addToHand(List<Ingredient> ingredients) {
        this.hand.addAll(ingredients);
    }

    /**
     * Adds the ingredient to the player's hand.
     *
     * @param ingredient The ingredient to be added to the hand.
     */
    public void addToHand(Ingredient ingredient) {
        this.hand.add(ingredient);
    }

    /**
     * Checks if the player has a specific ingredient in their hand.
     *
     * @param ingredient The ingredient to check for.
     * @return True, if the player has the ingredient in their hand, False,
     *         otherwise
     */
    public boolean hasIngredient(Ingredient ingredient) {
        return this.hand.contains(ingredient);
    }

    /**
     * Removes the ingredient from the player's hand, if present.
     *
     * @param ingredient The ingredient to be removed from the hand.
     */
    public void removeFromHand(Ingredient ingredient) {
        if (hasIngredient(ingredient)) {
            this.hand.remove(ingredient);
        } else {
            throw new WrongIngredientsException("Ingredient card not found in player's hand");
        }
    }

    /**
     * Returns the list of ingredients in the player's hand, sorted by name in
     * ascending order.
     *
     * @return the list of ingredients in the player's hand.
     */
    public List<Ingredient> getHand() {
        this.hand.sort(null);
        return this.hand;
    }

    /**
     * Returns a string representation of the player's hand.
     * The hand description is a comma-separated list of ingredients.
     * If the hand is empty, an empty string is returned.
     *
     * @return the hand description as a string.
     */
    public String getHandStr() {
        if (this.hand.isEmpty()) {
            return "";
        }

        HashMap<Ingredient, Integer> number = new HashMap<Ingredient, Integer>();
        ArrayList<String> description = new ArrayList<String>();
        for (Ingredient ingredient : getHand()) {
            if (number.containsKey(ingredient)) {
                number.replace(ingredient, number.get(ingredient) + 1);
            } else {
                number.put(ingredient, 1);
            }
        }
        for (Map.Entry<Ingredient, Integer> entry : number.entrySet()) {
            Ingredient ingredient = entry.getKey();
            Integer integer = entry.getValue();
            if (integer != 1) {
                description.add(StringUtils.toTitleCase(ingredient.toString()) + " (x" + integer + ")");
            } else {
                description.add(StringUtils.toTitleCase(ingredient.toString()));
            }
        }

        description.sort(null); // Sorting entries alphabetically
        String string_description = "";
        for (String part : description) {
            string_description += part + ", ";
        }
        return string_description.substring(0, string_description.length() - 2);
    }

    /**
     * Returns the name of the player.
     * 
     * @return The name of the player.
     */
    public String toString() {
        return name;
    }
}
