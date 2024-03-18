import bakery.MagicBakery;

public class BakeryDriver {

    public BakeryDriver() {
    }

    public static void main(String[] args)  {
        
        new MagicBakery(10, "../../io/ingredients.csv" , "../../io/layers.csv");

    }

}