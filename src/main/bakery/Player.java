package bakery;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Ingredient> hand;
    private String name;

    public Player(String name){
        this.name=name;
        hand=new ArrayList<Ingredient>();
    }

    public void addToHand(List<Ingredient> ingredients){
        hand.addAll(ingredients);
    }
    
    public void addToHand(Ingredient ingredient){
        hand.add(ingredient);
    }
    
    public boolean hasIngredient(Ingredient ingredient){
        if(hand.contains(ingredient)){
            return true;
        }
        else{
            return false;
        }
    }

    public void removeFromHand(Ingredient ingredient){
        if(hasIngredient(ingredient)){
            hand.remove(ingredient);
        }
    }

    public List<Ingredient> getHand(){
        return hand;
    }

    public String getHandStr(){
        if(hand.size()==0){
            return "";
        }

        String description="";
        for(Ingredient ingredient : hand){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }

    public String toString(){
        return name;
    }
}
