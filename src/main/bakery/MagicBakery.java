package bakery;

import java.lang.IllegalArgumentException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.HashSet;
import java.util.Random;
import java.io.File;
import java.io.Serializable;

import util.StringUtils;
import util.CardUtils;

public class MagicBakery implements Serializable{
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

    private static final long serialVersionUID=42L;
    

    /**
     * Constructor to initialise the Random object using seed, 
     * storing the ingredient and layer cards from the path 
     * in ingredientDeckFile and layerDeckFile respectively.
     * 
     * @param seed The seed to initialise the Random object, for predictive randomisation.
     * @param ingredientDeckFile The path to the file for the ingredient cards.
     * @param layerDeckFile The path to the file for the layer cards.
     */
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

    /**
     * Bakes a layer if the ingredients are found in current player's hand. 
     * 
     * @param layer The layer to bake with ingredients from the user's hand.
     * @throws WrongIngredientsException If the sufficient ingredients or helpful ducks are not found.
     */
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
        else{
            throw new WrongIngredientsException("Ingredients or substitutable number of helpful ducks required to bake not found!");
        }
    }

    private Ingredient drawFromPantryDeck(){
        Ingredient top=((Stack<Ingredient>)this.pantryDeck).pop();
        if(this.pantryDeck.isEmpty()){
            if(this.pantryDiscard.isEmpty()){
                throw new EmptyPantryException("The pantry is empty, use the cards in your hand to bake.", new RuntimeException());
            }
            this.pantryDeck=this.pantryDiscard;
            this.pantryDiscard.clear();
            Collections.shuffle((List<Ingredient>)this.pantryDeck, this.random);
        }
        return top;
    }

    /**
     * Draws an ingredient card with name ingredientName from the pantry and places it in the current player's hand.
     * 
     * @param ingredientName Name of the ingredient to be drawn from pantry.
     * @throws WrongIngredientsException If ingredient is not found in pantry.
     */
    public void drawFromPantry(String ingredientName){
        if(this.pantry.contains(new Ingredient(ingredientName))){
            ((ArrayList<Ingredient>)this.pantry).set(((ArrayList<Ingredient>)this.pantry).indexOf(new Ingredient(ingredientName)), drawFromPantryDeck());
            getCurrentPlayer().addToHand(new Ingredient(ingredientName));
            this.action_count++;
        }
        else{
            throw new WrongIngredientsException("Ingredient card not found in pantry");
        }
    }

    /**
     * Draws a card, ingredient, from the pantry and places it in the current player's hand.
     * 
     * @param ingredient ingredient to be drawn from pantry.
     * @throws WrongIngredientsException If ingredient is not found in pantry.
     */
    public void drawFromPantry(Ingredient ingredient){
        if(this.pantry.contains(ingredient)){
            ((ArrayList<Ingredient>)this.pantry).set(((ArrayList<Ingredient>)this.pantry).indexOf(ingredient), drawFromPantryDeck());
            getCurrentPlayer().addToHand(ingredient);
            this.action_count++;
        }
        else{
            throw new WrongIngredientsException("Ingredient card not found in pantry");
        }
    }

    /**
     * Changes turn among players if it is the end of the turn for the current player.
     * 
     * @return True, if it is the end of turn, False, otherwise.
     */
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

    /**
     * Fulfills the customer order if fulfillable, and garnishes the order if garnish is true and if garnishable.
     * 
     * @param customer Customer Order to fulfill(and/or garnish)
     * @param garnish Whether the order order should be garnished
     * @return List of ingredients excluding the ones used to fulfill(and/or garnish) the order.
     */
    public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish){
        return null;
    }

    /**
     * Returns the number of actions permitted based on the number of players.
     * If the number of players is less than or equal to 3, 3 actions are permitted.
     * Otherwise, 2 actions are permitted.
     *
     * @return the number of actions permitted
     */
    public int getActionsPermitted(){
        if(this.players.size()<=3){
            return 3;
        }
        else{
            return 2;
        }
    }

    /**
     * Returns the number of actions remaining for the current player.
     *
     * @return the number of actions remaining
     * @throws TooManyActionsException if the current player tries to make more moves after the end of his turn.
     */
    public int getActionsRemaining(){
        
        int actions_remaining=getActionsPermitted()-this.action_count;

        if(actions_remaining<0){
            throw new TooManyActionsException();
        }
        return actions_remaining;
    }

    /**
     * Returns a collection of layers
     * that can be baked based on the ingredients in the current player's hand.
     *
     * @return A collection of bakeable layers.
     */
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

    /**
     * Gets the current player of the game.
     * 
     * @return The current player of the game.
     */
    public Player getCurrentPlayer(){
        return ((ArrayDeque<Player>)this.players).peek();
    }

    /**
     * Returns the Customers object representing the customers in the game.
     *
     * @return The Customers in the game.
     */
    public Customers getCustomers(){
        return this.customers;
    }

    /**
     * Returns a collection of customer orders that can be fulfilled 
     * by the ingredients in the current player's hand.
     *
     * @return A collection of customer orders that can be fulfilled.
     */
    public Collection<CustomerOrder> getFulfillableCustomers(){
        return null;
    }

    /**
     * Returns a collection of customer orders that can be garnished 
     * by the ingredients in the current player's hand.
     *
     * @return A collection of customer orders that can be garnished.
     */
    public Collection<CustomerOrder> getGarnishableCustomers(){
        return null;
    }

    /**
     * Gets the unique set of layers.
     * 
     * @return A collection of unique set of layers.
     */
    public Collection<Layer> getLayers(){
        HashSet<Layer> unique_layers=new HashSet<Layer>();
        for(Layer layer:this.layers){
            unique_layers.add(layer);
        }
        return unique_layers;
        
    }

    /**
     * Gets the collection of ingredients in the pantry.
     *
     * @return the collection of ingredients in the pantry.
     */
    public Collection<Ingredient> getPantry(){
        return this.pantry;
    }

    /**
     * Gets the collection of all the players in the game.
     *
     * @return the collection of players.
     */
    public Collection<Player> getPlayers(){
        return this.players;
    }

    /**
     * Loads an existing game to resume playing.
     * 
     * @param file The file which contains the state of the game.
     * @return A object of MagicBakery.
     */
    public static MagicBakery loadState(File file){
        return null;
    }

    /**
     * Passes the specified ingredient card from the current player's hand to the recipient's.
     * 
     * @param ingredient The ingredient to pass to the other player.
     * @param recipient The other player bound to receive the card.
     * @throws WrongIngredientsException If ingredient not found in the current player's hand.
     */
    public void passCard(Ingredient ingredient, Player recipient){
        if(!getCurrentPlayer().getHand().contains(ingredient)){
            throw new WrongIngredientsException("Ingredient card not found in current player's hand; Cannot pass to another player");
        }
        getCurrentPlayer().removeFromHand(ingredient);
        recipient.addToHand(ingredient);
        this.action_count++;
    }

    /**
     * Prints the customer service record, which includes the number of customers fulfilled(and garnished)
     * and the disappointed customers who left.
     */
    public void printCustomerServiceRecord(){

    }

    /**
     * Prints the current state of the game, including the layers available to bake, 
     * the pantry, the customer orders yet be fulfilled and the current player's hand.
     */
    public void printGameState(){
        System.out.println("Layers: ");
        for(String line:StringUtils.layersToStrings(getLayers())){
            System.out.println(line);
        }
        System.out.println("Pantry: ");
        for(String line:StringUtils.ingredientsToStrings(this.pantry)){
            System.out.println(line);
        }
        System.out.println("It's your turn "+getCurrentPlayer()+"!");
        System.out.println("Your hand contains: "+getCurrentPlayer().getHandStr());
    }

    /**
     * Refreshes the pantry by discarding the current contents and drawing new items from the pantry deck.
     * The discarded items are added to the pantry discard pile.
     */
    public void refreshPantry(){
        this.pantryDiscard.addAll(this.pantry);
        this.pantry.clear();
        for(int i=0; i<5; i++){
            this.pantry.add(drawFromPantryDeck());
        }
        this.action_count++;
    }

    /**
     * Saves the state of the game by serialising it to a file.
     *
     * @param file the file to save the state to
     */
    public void saveState(File file){

    }

    /**
     * Starts the game with the given player names, 
     * initialising the customers from the customerDeckFile, 
     * shuffling the pantryDeck, setting up the pantry, 
     * and distributing the start hands among the players.
     * 
     * @param playerNames       a list of player names
     * @param customerDeckFile  the file path of the customer deck
     * @throws IllegalArgumentException if the number of players is invalid 
     * (if there are less than 2 or more 5 players),
     */
    public void startGame(List<String> playerNames, String customerDeckFile){
        if(playerNames.size()>5 || playerNames.size()<2){
            throw new IllegalArgumentException("Invalid number of players!");
        }
        this.firstPlayer=new Player(playerNames.get(0));
        for(String p:playerNames){
            this.players.add(new Player(p));
        }

        Collections.shuffle((List<Ingredient>)this.pantryDeck, this.random);

        for(int i=0; i<5; i++){
            this.pantry.add(drawFromPantryDeck());
        }

        /* for(Player player: players){
            player.addToHand(new Ingredient("eggs"));
            //player.addToHand(new Ingredient("sugar"));
            player.addToHand(Ingredient.HELPFUL_DUCK);
            player.addToHand(new Ingredient("butter"));
        } */

        for(Player player:this.players){
            for(int i=0; i<3; i++){
                player.addToHand(drawFromPantryDeck());
            }
        }
        
    }
}
