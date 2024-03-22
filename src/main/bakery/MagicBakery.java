package bakery;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.HashSet;
import java.util.Random;
import java.io.File;

import util.StringUtils;
import util.CardUtils;

public class MagicBakery {
    public enum ActionType{
        DRAW_INGREDIENT,
        PASS_INGREDIENT,
        BAKE_LAYER,
        FULFIL_ORDER, //Minor typo
        REFRESH_PANTRY
    }

    private Customers customers;
    private Collection<Layer> layers;
    private Collection<Player> players;
    private Collection<Ingredient> pantry;
    private Collection<Ingredient> pantryDeck;
    private Collection<Ingredient> pantryDiscard;
    private Random random;
    
    private int action_count;
    public Player firstPlayer;

    private static long serialVersionUID;
    
    public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile){
        this.action_count=0;
        this.players=new ArrayDeque<Player>(); 
        this.pantry=new ArrayList<Ingredient>();
        this.pantryDiscard=new Stack<Ingredient>();
        this.random=new Random(seed);
        this.pantryDeck=new Stack<Ingredient>();
        this.pantryDeck.addAll(CardUtils.readIngredientFile(ingredientDeckFile));
        this.layers=CardUtils.readLayerFile(layerDeckFile);
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
        if(getBakeableLayers().contains(layer)){
            for(Ingredient ingredient:layer.getRecipe()){
                getCurrentPlayer().removeFromHand(ingredient);
                this.pantryDiscard.add(ingredient);
            }
            this.layers.remove(layer);
            getCurrentPlayer().addToHand(layer);
            this.action_count++;
        }
    }

    private Ingredient drawFromPantryDeck(){
        return ((Stack<Ingredient>)this.pantryDeck).pop();
    }

    public void drawFromPantry(String ingredientName){
        if(this.pantry.contains(new Ingredient(ingredientName))){
            this.pantry.remove(new Ingredient(ingredientName));
            getCurrentPlayer().addToHand(new Ingredient(ingredientName));
            this.action_count++;
        }
    }

    public void drawFromPantry(Ingredient ingredient){
        if(this.pantry.contains(ingredient)){
            this.pantry.remove(ingredient);
            getCurrentPlayer().addToHand(ingredient);
            this.action_count++;
        }
    }

    public boolean endTurn(){
        if(getActionsRemaining()>0){
            return false;
        }
        Player currentPlayer=getCurrentPlayer();
        this.players.remove(currentPlayer);
        if(getCurrentPlayer().toString().equals(this.firstPlayer.toString())){
            System.out.println("New Round");
        }
        this.players.add(currentPlayer);
        this.action_count=0;
        return true;
    }

    public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish){
        return null;
    }

    public int getActionsPermitted(){
        if(this.players.size()<=3){
            return 3;
        }
        else{
            return 2;
        }
    }

    public int getActionsRemaining(){
        
        int actions_remaining=getActionsPermitted()-this.action_count;

        if(actions_remaining<0){
            throw new TooManyActionsException();
        }
        return actions_remaining;
    }

    public Collection<Layer> getBakeableLayers(){
        Collection<Layer> bakeable_layers=new ArrayList<Layer>();
        for(Layer layer:getLayers()){
            List<Ingredient> recipe=layer.getRecipe();
            int count=recipe.size();
            for(Ingredient ingredient:getCurrentPlayer().getHand()){
                if(recipe.contains(ingredient)){
                    count--;
                }
            }
            if(count==0){
                bakeable_layers.add(layer);
            }
            else{
                int duck_count=0;
                for(Ingredient ingredient: getCurrentPlayer().getHand()){
                    if(ingredient.equals(Ingredient.HELPFUL_DUCK)){
                        duck_count++;
                    }
                }
                if(duck_count==count){
                    bakeable_layers.add(layer);
                }
            }
        }
        return bakeable_layers;
    }

    public Player getCurrentPlayer(){
        return ((ArrayDeque<Player>)this.players).peek();
    }

    public Customers getCustomers(){
        return null;
    }

    public Collection<CustomerOrder> getFulfillableCustomers(){
        return null;
    }


    public Collection<CustomerOrder> getGarnishableCustomers(){
        return null;
    }

    public Collection<Layer> getLayers(){
        HashSet<Layer> unique_layers=new HashSet<Layer>();
        for(Layer layer:this.layers){
            unique_layers.add(layer);
        }
        return unique_layers;
        
    }

    public Collection<Ingredient> getPantry(){
        return this.pantry;
    }

    public Collection<Player> getPlayers(){
        return this.players;
    }

    public static MagicBakery loadState(File file){
        return null;
    }

    public void passCard(Ingredient ingredient, Player recipient){
        getCurrentPlayer().removeFromHand(ingredient);
        recipient.addToHand(ingredient);
        this.action_count++;
    }

    public void printCustomerServiceRecord(){

    }

    public void printGameState(){
        System.out.println("Layers: ");
        for(String line:StringUtils.layersToStrings(getLayers())){
            System.out.println(line);
        }
        System.out.println("Pantry: ");
        for(String line:StringUtils.ingredientsToStrings(this.pantry)){
            System.out.println(line);
        }
    }

    public void refreshPantry(){
        this.pantryDiscard.addAll(this.pantry);
        this.pantry.clear();
        for(int i=0; i<5; i++){
            this.pantry.add(drawFromPantryDeck());
        }
        this.action_count++;
    }

    public void saveState(File file){

    }

    public void startGame(List<String> playerNames, String customerDeckFile){
        this.firstPlayer=new Player(playerNames.get(0));
        for(String p:playerNames){
            this.players.add(new Player(p));
        }

        Collections.shuffle((List<Ingredient>)this.pantryDeck, this.random);

        for(int i=0; i<5; i++){
            this.pantry.add(drawFromPantryDeck());
        }

        for(Player player: players){
            player.addToHand(new Ingredient("eggs"));
            //player.addToHand(new Ingredient("sugar"));
            player.addToHand(Ingredient.HELPFUL_DUCK);
            player.addToHand(new Ingredient("butter"));
        }

        /* for(Player player:this.players){
            for(int i=0; i<3; i++){
                player.addToHand(drawFromPantryDeck());
            }
        } */
        
    }
}
