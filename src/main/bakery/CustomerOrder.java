package bakery;

import java.util.List;
import java.io.Serializable;

/**
 * A Class representing the customer order of a customer.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class CustomerOrder implements Serializable{
    /**
     * The enumeration representing the status of the customer order.
     */
    public enum CustomerOrderStatus{
        WAITING, 
        FULFILLED,
        GARNISHED,
        IMPATIENT,
        GIVEN_UP
    }
    private List<Ingredient> garnish;
    private int level;
    private String name;
    private List<Ingredient> recipe;
    private CustomerOrderStatus status;

    private static final long serialVersionUID=42L;
    
    /**
     * Constructor to initialise the name, recipe, garnish and level of a Customer Order.
     * Throws a WrongIngredientsException if initialised with empty or null list of Ingredients for recipe.
     *
     * @param name The name of the Dish for the Customer Order.
     * @param recipe The List of Ingredients needed to make the the Customer Order.
     * @param garnish The List of Ingredients needed to garnish the Customer Order.
     * @param level The difficulty level of the Customer Order.
     */
    public CustomerOrder(String name, List<Ingredient> recipe, List<Ingredient> garnish, int level){
        if(recipe==null || recipe.isEmpty()) {
            throw new WrongIngredientsException("Initialised with empty or null list of Ingredients for the recipe");
        }
        this.name=name;
        this.recipe=recipe;
        this.garnish=garnish;
        this.level=level;
        this.status=CustomerOrderStatus.WAITING;
    }

    /**
     * Sets the status of the customer order to "GIVEN_UP".
     */
    public void abandon(){
        setStatus(CustomerOrderStatus.GIVEN_UP);
    }
    
    /**
     * Checks if the Customer Order can be fulfilled with the given list of ingredients.
     *
     * @param ingredients the list of ingredients to check against the Customer Order
     * @return true if the Customer Order can be fulfilled, false otherwise
     */
    public boolean canFulfill(List<Ingredient> ingredients){
        return false;
    }

    /**
     * Checks if the Customer Order can be garnished with the given list of ingredients.
     *
     * @param ingredients the list of ingredients to check against the Customer Order
     * @return true if the Customer Order can be fulfilled, false otherwise
     */
    public boolean canGarnish(List<Ingredient> ingredients){
        return false;
    }
    /**
     * Sets the status to FULFILLED if this customer order if fulfillable, or to GARNISHED if garnish is true and if garnishable.
     * 
     * @param ingredients The list of ingredients from which the order must fulfilled or garnished.
     * @param garnish Garnishes the order if set to True, otherwise, just fulfills it.
     * @return The subset of ingredients used to fulfill the order.
     */
    public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnish){
        return null;
    }

    /**
     * Returns the list of garnish ingredients for this customer order.
     *
     * @return The list of garnish ingredients.
     */
    public List<Ingredient> getGarnish(){
        return garnish;
    }

    /**
     * Returns the string representation of the garnish for this customer order.
     * The garnish description is a comma-separated list of ingredients.
     * If there is no garnish, an empty string is returned.
     *
     * @return The description of the garnish.
     */
    public String getGarnishDescription(){

        if(garnish.isEmpty()){
            return "";
        }

        String description="";
        for(Ingredient ingredient : garnish){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }

    /**
     * Gets the level of the customerOrder.
     *
     * @return The level of the customer order.
     */
    public int getLevel(){
        return level;
    }

    /**
     * Returns the list of recipe ingredients for this customer order.
     *
     * @return The list of recipe ingredients.
     */
    public List<Ingredient> getRecipe(){
        return recipe;
    }

    /**
     * Returns the string representation of the recipe for this customer order.
     * The recipe description is a comma-separated list of ingredients.
     * If there is no recipe, an empty string is returned.
     *
     * @return The description of the recipe.
     */
    public String getRecipeDescription(){

        if(recipe.isEmpty()){
            return "";
        }

        String description="";
        for(Ingredient ingredient : recipe){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }

    /**
     * Gets the status of a customer order.
     * 
     * @return The customer order status enumeration.
     */
    public CustomerOrderStatus getStatus(){
        return status;
    }

    /**
     * Sets the status of a customer order.
     * 
     * @param status The customer order status to be set.
     */
    public void setStatus(CustomerOrderStatus status){
        this.status=status;
    }

    /**
     * Returns the name of the customer order.
     * 
     * @return The name of the customer order.
     */
    public String toString(){
        return name;
    }
}
