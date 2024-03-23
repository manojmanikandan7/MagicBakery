package bakery;

import java.util.List;
import java.io.Serializable;

public class CustomerOrder implements Serializable{
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

    public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnish){
        return null;
    }

    public List<Ingredient> getGarnish(){
        return garnish;
    }

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

    public int getLevel(){
        return level;
    }

    public List<Ingredient> getRecipe(){
        return recipe;
    }

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

    public CustomerOrderStatus getStatus(){
        return status;
    }

    public void setStatus(CustomerOrderStatus status){
        this.status=status;
    }

    public String toString(){
        return name;
    }
}
