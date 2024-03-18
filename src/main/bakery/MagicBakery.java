package bakery;

import util.*;

import java.io.File;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;
import java.util.List;


public class MagicBakery {
    //private Customers customers;
    private Collection<Layer> layers;
    private Collection<Player> players;
    private Collection<Ingredient> pantry;
    private Collection<Ingredient> pantryDeck;
    private Collection<Ingredient> pantryDiscard;
    
    private int action_count;
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
        players=new PriorityQueue<Player>();
        action_count=0;
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

    public void bakeLayer(Layer layer){

    }

    private Ingredient drawFromPantryDeck(){
        return null;
    }

    public void drawFromPantry(String IngredientName){

    }

    public void drawFromPantry(Ingredient ingredient){

    }

    public boolean endTurn(){
        return false;
    }

    public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish){
        return null;
    }

    public int getActionsPermitted(){
        if(players.size()<=3){
            return 3;
        }
        else{
            return 2;
        }
    }

    public int getActionsRemaining(){
        return getActionsPermitted()-action_count;
    }

    public Collection<Layer> getBakeableLayers(){
        return null;
    }

    public Player getCurrentPlayer(){
        PriorityQueue<Player> playerqueue=new PriorityQueue<Player>(players);
        return playerqueue.peek();
    }

    /* public Customers getCustomers(){
        return null;
    } */

    public Collection<CustomerOrder> getFulfillableCustomers(){
        return null;
    }


    public Collection<CustomerOrder> getGarnishableCustomers(){
        return null;
    }

    public Collection<Layer> getLayers(){
        return null;
    }

    public Collection<Ingredient> getPantry(){
        return null;
    }

    public Collection<Player> getPlayers(){
        ConsoleUtils in=new ConsoleUtils();
        ArrayList<String> playerstr=in.promptForNewPlayers("Who's playing?");
        for(String p:playerstr){
            players.add(new Player(p));
        }
        return players;
    }

    public static MagicBakery loadState(File file){
        return null;
    }

    public void passCard(Ingredient ingredient, Player recipient){

    }

    public void printCustomerServiceRecord(){

    }

    public void printGameState(){

    }

    public void refreshPantry(){

    }

    public void saveState(File file){

    }

    public void startGame(ArrayList<String> playerNames, String customerDeckFile){

        System.out.println(players.toString());
    }
}
