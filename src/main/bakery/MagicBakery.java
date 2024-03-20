package bakery;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.Random;
import java.io.File;



public class MagicBakery {
    public enum ActionType{
        DRAW_INGREDIENT,
        PASS_INGREDIENT,
        BAKE_LAYER,
        FULFIL_ORDER, //Minor typo
        REFRESH_PANTRY
    }

    //private Customers customers;
    private Collection<Layer> layers;
    private Collection<Player> players;
    private Collection<Ingredient> pantry;
    private Collection<Ingredient> pantryDeck;
    private Collection<Ingredient> pantryDiscard;
    private Random random;
    
    private int action_count;
    public Player firstPlayer;
    public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile){
        players=new ArrayDeque<Player>();
        action_count=0;
        random=new Random(seed);
        pantryDeck=new Stack<Ingredient>();
        pantry=new ArrayList<Ingredient>();
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
        

        

        /* startGame(null, "");
        File path=in.promptForFilePath("Enter the file path for ingredients: ");
        ingredientDeckFile=path.toString();
        ArrayList<Ingredient> ingredientslist=CardUtils.readIngredientFile(ingredientDeckFile);


        Player choice=in.promptForExistingPlayer("Choose one: ", this);
        System.out.println(choice); */
        /* System.out.println("Size of ingredients list: "+ingredientslist.size());
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
        } */

    }

    public void bakeLayer(Layer layer){

    }

    private Ingredient drawFromPantryDeck(){
        return ((Stack<Ingredient>)pantryDeck).pop();
    }

    public void drawFromPantry(String ingredientName){
        if(pantry.contains(new Ingredient(ingredientName))){
            pantry.remove(new Ingredient(ingredientName));
            action_count++;
        }
    }

    public void drawFromPantry(Ingredient ingredient){
        if(pantry.contains(ingredient)){
            pantry.remove(ingredient);
            action_count++;
        }
    }

    public boolean endTurn(){
        if(getActionsRemaining()>0){
            return false;
        }
        Player currentPlayer=getCurrentPlayer();
        players.remove(currentPlayer);
        if(getCurrentPlayer().toString().equals(firstPlayer.toString())){
            System.out.println("New Round");
        }
        players.add(currentPlayer);
        action_count=0;
        return true;
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
        
        int actions_remaining=getActionsPermitted()-action_count;

        if(actions_remaining<0){
            throw new TooManyActionsException();
        }
        return actions_remaining;
    }

    public Collection<Layer> getBakeableLayers(){
        return null;
    }

    public Player getCurrentPlayer(){
        return ((ArrayDeque<Player>)players).peek();
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
        return pantry;
    }

    public Collection<Player> getPlayers(){
        return players;
    }

    public static MagicBakery loadState(File file){
        return null;
    }

    public void passCard(Ingredient ingredient, Player recipient){
        recipient.addToHand(ingredient);
        action_count++;
    }

    public void printCustomerServiceRecord(){

    }

    public void printGameState(){

    }

    public void refreshPantry(){
        pantry.clear();
        for(int i=0; i<5; i++){
            pantry.add(drawFromPantryDeck());
        }
    }

    public void saveState(File file){

    }

    public void startGame(List<String> playerNames, String customerDeckFile){
        firstPlayer=new Player(playerNames.get(0));
        for(String p:playerNames){
            players.add(new Player(p));
        }

        Collections.shuffle((List<Ingredient>)pantryDeck, random);

        for(int i=0; i<5; i++){
            pantry.add(drawFromPantryDeck());
        }

        for(Player player:players){
            for(int i=0; i<3; i++){
                player.addToHand(drawFromPantryDeck());
            }
        }
        
    }
}
