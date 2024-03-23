package bakery;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Player implements Serializable{
    private List<Ingredient> hand;
    private String name;

    private static final long serialVersionUID=42L;

    public Player(String name){
        this.name=name;
        hand=new ArrayList<Ingredient>();
    }

    public void addToHand(List<Ingredient> ingredients){
        this.hand.addAll(ingredients);
    }
    
    public void addToHand(Ingredient ingredient){
       this.hand.add(ingredient);
    }
    
    public boolean hasIngredient(Ingredient ingredient){
        if(this.hand.contains(ingredient)){
            return true;
        }
        else{
            return false;
        }
    }

    public void removeFromHand(Ingredient ingredient){
        if(hasIngredient(ingredient)){
            this.hand.remove(ingredient);
        }
        else{
            throw new WrongIngredientsException("Ingredient card not found in player's hand");
        }
    }

    public List<Ingredient> getHand(){
        this.hand.sort(null);
        return this.hand;
    }

    public String getHandStr(){
        if(this.hand.size()==0){
            return "";
        }

        String description="";
        for(Ingredient ingredient : getHand()){
            description+=ingredient+", ";
        }
        return description.substring(0, description.length()-2);
    }

    public String toString(){
        return name;
    }
}
