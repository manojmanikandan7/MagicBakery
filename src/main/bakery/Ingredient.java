package bakery;

public class Ingredient {
    private String name;
    public static final Ingredient HELPFUL_DUCK=new Ingredient("helpful duck 𓅭");
    
    public Ingredient(String name){
        this.name=name;
    }

    public String toString(){
        return name;
    }
}
