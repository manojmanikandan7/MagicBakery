package bakery;

import java.util.ArrayList;

public class Layer extends Ingredient{
    private ArrayList<Ingredient> recipe;
    
    public Layer(String name, ArrayList<Ingredient> recipe){
        super(name);
        this.recipe=recipe;
    }

    public ArrayList<Ingredient> getRecipe(){
        return recipe;
    }

    public String getRecipeDescription(){
        String description="";
        for(Ingredient ingredient : recipe){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }
}
