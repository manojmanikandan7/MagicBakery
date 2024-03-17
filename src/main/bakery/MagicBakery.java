package bakery;

import util.*;

import java.io.File;
import java.util.ArrayList;


public class MagicBakery {
    public ArrayList<Player> players;

    public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile){
        /* Ingredient Flour=new Ingredient("Flour");
        Ingredient Sugar=new Ingredient("Sugar");
        Ingredient Eggs=new Ingredient("Eggs");
        Ingredient Milk=new Ingredient("Milk");
        ArrayList<Ingredient> ingredients=new ArrayList<Ingredient>();

        ingredients.add(Flour);
        ingredients.add(Sugar);
        ingredients.add(Eggs);
        ingredients.add(Milk);


        Layer Layer1=new Layer("Layer1", ingredients);

        CustomerOrder order1=new CustomerOrder("order1", Layer1.getRecipe(), ingredients, 1);

        System.out.println(order1.getRecipeDescription()+"\n"+order1.getGarnishDescription()); */
        players=new ArrayList<Player>();
        ConsoleUtils in=new ConsoleUtils();
        startGame(null, "");
        File path=in.promptForFilePath("Enter the file path for ingredients: ");
        ingredientDeckFile=path.toString();
        ArrayList<Ingredient> ingredientslist=CardUtils.readIngredientFile(ingredientDeckFile);

        System.out.println("Size of ingredients list: "+ingredientslist.size());
        System.out.println("The Ingredients: ");
        for(Ingredient ingredient:ingredientslist){
            System.out.println(ingredient.toString());
        }

        ArrayList<Layer> layerlist=CardUtils.readLayerFile(layerDeckFile);

        System.out.println("Size of layer list: "+layerlist.size());
        System.out.println("The Layers: ");
        for(Layer layer:layerlist){
            System.out.println(layer.toString()+"\n"+layer.getRecipeDescription());
        }

        ArrayList<CustomerOrder> customers=CardUtils.readCustomerFile("../../io/customers.csv", layerlist);

        System.out.println("Size of customers list: "+customers.size());
        System.out.println("The customers: ");
        for(CustomerOrder customer:customers){
            System.out.println("Name of dish: " + customer.toString()+"\n"+
                                "The ingredients required for the recipe: "+customer.getRecipeDescription()+"\n"+
                                "The ingredients required for the garnish: "+customer.getGarnishDescription());
        }

    }

    public void startGame(ArrayList<String> playerNames, String customerDeckFile){
        ConsoleUtils in=new ConsoleUtils();
        ArrayList<String> playerstr=in.promptForNewPlayers("Who's playing?");
        for(String p:playerstr){
            players.add(new Player(p));
        }
        System.out.println(players.toString());
    }
}
