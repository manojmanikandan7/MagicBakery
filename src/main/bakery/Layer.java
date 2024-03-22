package bakery;

import java.util.List;

public class Layer extends Ingredient{
    private List<Ingredient> recipe;

    private static long serialVersionUID;
    
    public Layer(String name, List<Ingredient> recipe){
        super(name);
        this.recipe=recipe;
    }

    public boolean canBake(List<Ingredient> ingredients){
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
