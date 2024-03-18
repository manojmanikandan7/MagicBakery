package bakery;

import java.util.ArrayList;
import java.util.List;

public class Layer extends Ingredient{
    private List<Ingredient> recipe;
    
    public Layer(String name, ArrayList<Ingredient> recipe){
        super(name);
        this.recipe=recipe;
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
}
