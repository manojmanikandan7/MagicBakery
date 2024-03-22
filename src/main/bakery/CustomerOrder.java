package bakery;

import java.util.List;

public class CustomerOrder {
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

    private static long serialVersionUID;

    public CustomerOrder(String name, List<Ingredient> recipe, List<Ingredient> garnish, int level){
            this.name=name;
            this.recipe=recipe;
            this.garnish=garnish;
            this.level=level;
            this.status=CustomerOrderStatus.WAITING;
    }

    public void abandon(){
        setStatus(CustomerOrderStatus.GIVEN_UP);
    }
    
    public boolean canFulfill(List<Ingredient> ingredients){
        return false;
    }

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
