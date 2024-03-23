package bakery;

import java.util.List;

public class Layer extends Ingredient{
    private List<Ingredient> recipe;

    private static final long serialVersionUID=42L;
    
    public Layer(String name, List<Ingredient> recipe){
        super(name);
        if(recipe==null || recipe.isEmpty()) {
            throw new WrongIngredientsException("Initialised with empty or null list of Ingredients for recipe");
        }
        this.recipe=recipe;
    }

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
            if(duck_count==count){
                return true;
            }
        }
        return false;
    }

    public List<Ingredient> getRecipe(){
        return this.recipe;
    }

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

    @Override
    public int hashCode(){
        return super.hashCode() * this.recipe.hashCode();
    }
}
