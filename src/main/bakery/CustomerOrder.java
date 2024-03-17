package bakery;

import java.util.ArrayList;

public class CustomerOrder {
    public enum CustomerOrderStatus{
        WAITING, 
        FULFILLED,
        GARNISHED,
        IMPATIENT,
        GIVEN_UP
    }
    private ArrayList<Ingredient> garnish;
    private int level;
    private String name;
    private ArrayList<Ingredient> recipe;
    private CustomerOrderStatus status;

    public CustomerOrder(String name, ArrayList<Ingredient> recipe, ArrayList<Ingredient> garnish, int level){
            this.name=name;
            this.recipe=recipe;
            this.garnish=garnish;
            this.level=level;
            this.status=CustomerOrderStatus.WAITING;
    }

    public void abandon(){
        status=CustomerOrderStatus.GIVEN_UP;
    }
    
    public ArrayList<Ingredient> getGarnish(){
        return garnish;
    }

    public String getGarnishDescription(){

        if(garnish.size()==0){
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

    public ArrayList<Ingredient> getRecipe(){
        return recipe;
    }

    public String getRecipeDescription(){

        if(recipe.size()==0){
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
