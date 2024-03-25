package bakery;

import java.util.List;

/**
 * A Class representing the layer to make a customer order.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class Layer extends Ingredient{
    private List<Ingredient> recipe;

    private static final long serialVersionUID=42L;
    
    /**
     * Initialises the name and the recipe needed to bake the layer.
     * 
     * @param name The name of the layer.
     * @param recipe The list of ingredients needed to bake this layer.
     */
    public Layer(String name, List<Ingredient> recipe){
        super(name);
        if(recipe==null || recipe.isEmpty()) {
            throw new WrongIngredientsException("Initialised with empty or null list of Ingredients for recipe");
        }
        this.recipe=recipe;
    }

    /**
     * Checks if the given list of ingredients is sufficient to bake the layer.
     * 
     * @param ingredients the list of ingredients to check with the recipe.
     * @return True, if the layer can be baked with the given ingredients, False otherwise
     */
    public boolean canBake(List<Ingredient> ingredients){
        int count=this.recipe.size();
        for(Ingredient ingredient:this.recipe){
            if(ingredients.contains(ingredient)){
                count--;
            }
        }
        if(count==0){
            return true;
        }
        else{
            int duck_count=0;
            for(Ingredient ingredient: ingredients){
                if(ingredient.equals(Ingredient.HELPFUL_DUCK)){
                    duck_count++;
                }
            }
            return duck_count == count;
        }
    }
    /**
     * Gets the recipe for this layer.
     * 
     * @return The list of ingredients needed to bake this layer.
     */
    public List<Ingredient> getRecipe(){
        return this.recipe;
    }

    /**
     * Returns a string representation of the recipe description for this layer.
     * The recipe description is a comma-separated list of ingredients.
     * If the recipe is empty, an empty string is returned.
     *
     * @return the recipe description as a string.
     */
    public String getRecipeDescription(){

        if(this.recipe.isEmpty()){
            return "";
        }

        String description="";
        for(Ingredient ingredient : this.recipe){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }

    /**
     * Produces a hash code to be used for the hash set or hash map data structures.
     *
     * @return A hash code based on the name and the recipe of the layer.
     */
    public int hashCode(){
        return super.hashCode() * this.recipe.hashCode();
    }
}
