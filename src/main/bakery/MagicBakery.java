package bakery;

import java.lang.IllegalArgumentException;
import java.lang.ClassNotFoundException;
import java.util.*;
import java.io.Serializable;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import util.CardUtils;
import util.StringUtils;

/**
 * A Class representing the Magic Bakery game.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class MagicBakery implements Serializable{
    /**
     * An enumeration for the actions the users can take.
     */
    public enum ActionType{
        DRAW_INGREDIENT,
        PASS_INGREDIENT,
        BAKE_LAYER,
        FULFIL_ORDER,
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

    private Player currentPlayer;

    private static final long serialVersionUID=42L;
    

    /**
     * Constructor to initialise the Random object using seed, 
     * storing the ingredient and layer cards from the path 
     * in ingredientDeckFile and layerDeckFile respectively.
     * 
     * @param seed The seed to initialise the Random object, for predictive randomisation.
     * @param ingredientDeckFile The path to the file for the ingredient cards.
     * @param layerDeckFile The path to the file for the layer cards.
     * @throws IOException If there are any problems reading the file.
     */
    public MagicBakery(long seed, String ingredientDeckFile, String layerDeckFile) throws IOException{
        this.action_count=0;
        this.players=new LinkedList<Player>();
        this.pantry=new ArrayList<Ingredient>();
        this.pantryDiscard=new Stack<Ingredient>();
        this.random=new Random(seed);
        this.pantryDeck=new Stack<Ingredient>();
        this.pantryDeck.addAll(CardUtils.readIngredientFile(ingredientDeckFile));
        this.layers=CardUtils.readLayerFile(layerDeckFile);

    }

    /**
     * Bakes a layer if the ingredients are found in current player's hand. 
     * 
     * @param layer The layer to bake with ingredients from the user's hand.
     */
    public void bakeLayer(Layer layer){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
        if(getBakeableLayers().contains(layer)){
            for(Ingredient ingredient:layer.getRecipe()){
                if(this.currentPlayer.hasIngredient(ingredient)){
                    this.currentPlayer.removeFromHand(ingredient);
                    this.pantryDiscard.add(ingredient);
                }
                else {
                    this.currentPlayer.getHand().remove(Ingredient.HELPFUL_DUCK);
                    this.pantryDiscard.add(Ingredient.HELPFUL_DUCK);
                }
            }
            this.layers.remove(layer);
            this.currentPlayer.addToHand(layer);
            this.action_count++;
        }
        else{
            throw new WrongIngredientsException("Ingredients or substitutable number of helpful ducks required to bake not found!");
        }
    }

    private Ingredient drawFromPantryDeck(){
        if(this.pantryDeck.isEmpty()){
            if(this.pantryDiscard.isEmpty()){
                throw new EmptyPantryException("The pantry is empty, use the cards in your hand to bake.", new RuntimeException());
            }
            this.pantryDeck.addAll(this.pantryDiscard);
            this.pantryDiscard.clear();
            Collections.shuffle((List<Ingredient>)this.pantryDeck, this.random);
        }
        return ((Stack<Ingredient>)this.pantryDeck).pop();
    }

    /**
     * Draws an ingredient card with name ingredientName from the pantry and places it in the current player's hand.
     *
     * @param ingredientName Name of the ingredient to be drawn from pantry.
     */
    public void drawFromPantry(String ingredientName){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
        if(this.pantry.contains(new Ingredient(ingredientName))){
            ((ArrayList<Ingredient>)this.pantry).set(((ArrayList<Ingredient>)this.pantry).indexOf(new Ingredient(ingredientName)), drawFromPantryDeck());
            this.currentPlayer.addToHand(new Ingredient(ingredientName));
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
     */
    public void drawFromPantry(Ingredient ingredient){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
        if(this.pantry.contains(ingredient)){
            ((ArrayList<Ingredient>)this.pantry).set(((ArrayList<Ingredient>)this.pantry).indexOf(ingredient), drawFromPantryDeck());
            this.currentPlayer.addToHand(ingredient);
            this.action_count++;
        }
        else{
            throw new WrongIngredientsException("Ingredient card not found in pantry");
        }
    }

    /**
     * Changes turns among players if it is the end of the turn for the current player.
     * 
     * @return True, if it is the end of turn, False, otherwise.
     */
    public boolean endTurn(){
        boolean endTurn = true;
        if(action_count<getActionsPermitted()){
            endTurn = false;
        }
        int index=((LinkedList<Player>)this.players).indexOf(this.currentPlayer)+1;
        if(index < this.players.size()){
            this.currentPlayer = ((LinkedList<Player>)this.players).get(index);
        }
        else{
            this.currentPlayer = ((LinkedList<Player>)this.players).get(0);
            if (this.customers.getCustomerDeck().isEmpty()) {
                this.customers.timePasses();
            }
            else {
                this.customers.addCustomerOrder();
            }
        }
        this.action_count=0;
        return endTurn;
    }

    /**
     * Fulfills the customer order if fulfillable, and garnishes the order if garnish is true and if garnishable.
     * 
     * @param customer Customer Order to fulfill(and/or garnish)
     * @param garnish Whether the order should be garnished
     * @return List of ingredients excluding the ones used to fulfill(and/or garnish) the order.
     */
    public List<Ingredient> fulfillOrder(CustomerOrder customer, boolean garnish){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
        Player currentPlayer = this.currentPlayer;
        List<Ingredient> added = new ArrayList<Ingredient>();
        List<Ingredient> used = customer.fulfill(currentPlayer.getHand(), garnish);
        if(!used.isEmpty()){
            customers.remove(customer);
            currentPlayer.getHand().removeAll(used);
            for(Ingredient ingredient : used){
                if(ingredient instanceof Layer){
                    this.layers.add((Layer)ingredient);
                }
                else{
                    this.pantryDiscard.add(ingredient);
                }
            }
            if(!this.customers.getCustomerDeck().isEmpty() && this.customers.peek() != null){
                this.customers.peek().setStatus(CustomerOrder.CustomerOrderStatus.WAITING);
            }
            if (customer.getStatus() == CustomerOrder.CustomerOrderStatus.GARNISHED){
                added.add(drawFromPantryDeck());
                added.add(drawFromPantryDeck());
                currentPlayer.addToHand(added);
            }
        }
        this.action_count++;
        return added;
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
            if(layer.canBake(this.currentPlayer.getHand())){
                bakeable_layers.add(layer);
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
        return this.currentPlayer;
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
    public Collection<CustomerOrder> getFulfilableCustomers(){
        return customers.getFulfilable(this.currentPlayer.getHand());
    }

    /**
     * Returns a collection of customer orders that can be garnished 
     * by the ingredients in the current player's hand.
     *
     * @return A collection of customer orders that can be garnished.
     */
    public Collection<CustomerOrder> getGarnishableCustomers(){
        Collection<CustomerOrder> garnishable = new ArrayList<CustomerOrder>();
        for(CustomerOrder customer : customers.getActiveCustomers()){
            if(customer.canGarnish(this.currentPlayer.getHand()) && customer.canFulfill(this.currentPlayer.getHand())){
                garnishable.add(customer);
            }
        }
        return garnishable;
    }

    /**
     * Gets the unique set of layers.
     * 
     * @return A collection of unique set of layers.
     */
    public Collection<Layer> getLayers(){
        return new HashSet<Layer>(this.layers);
    }

    /**
     * Gets the collection of ingredients in the pantry.
     *
     * @return The collection of ingredients in the pantry.
     */
    public Collection<Ingredient> getPantry(){
        return this.pantry;
    }

    /**
     * Gets the collection of all the players in the game.
     *
     * @return The collection of players.
     */
    public Collection<Player> getPlayers(){
        return this.players;
    }

    /**
     * Loads an existing game to resume playing.
     * 
     * @param file The file which contains the state of the game.
     * @return An object of MagicBakery.
     * @throws IOException If there are any problems reading the file.
     * @throws ClassNotFoundException If the object loaded does not belong to any class.
     */
    public static MagicBakery loadState(File file) throws IOException, ClassNotFoundException {
        MagicBakery game = null;
        try (
                FileInputStream gamefile = new FileInputStream(file);
                ObjectInputStream read = new ObjectInputStream(gamefile);
        ) {
            game = (MagicBakery) read.readObject();
        }
        return game;
    }

    /**
     * Passes the specified ingredient card from the current player's hand to the recipient's.
     * 
     * @param ingredient The ingredient to pass to the other player.
     * @param recipient The other player bound to receive the card.
     */
    public void passCard(Ingredient ingredient, Player recipient){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
        if(!this.currentPlayer.getHand().contains(ingredient)){
            throw new WrongIngredientsException("Ingredient card not found in current player's hand; Cannot pass to another player");
        }
        this.currentPlayer.removeFromHand(ingredient);
        recipient.addToHand(ingredient);
        this.action_count++;
    }

    /**
     * Prints the customer service record, which includes the number of customers fulfilled(and garnished)
     * and the disappointed customers who left.
     */
    public void printCustomerServiceRecord(){
        int fulfilled = this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.FULFILLED).size();
        int garnished = this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GARNISHED).size();
        int abandoned = this.customers.getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GIVEN_UP).size();

        System.out.println("Happy customers eating baked goods: " + (fulfilled+garnished) +" (" + garnished + " garnished) \n" +
                "Gone to Greggs instead: " + abandoned);
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
        if (!this.customers.isEmpty()) {
            System.out.println("Customers: ");
            for(String line:StringUtils.customerOrdersToStrings(this.customers.getActiveCustomers())){
                System.out.println(line);
            }
        }
        else{
            System.out.println("No customers waiting -- time for a brew :)");
        }
        System.out.println("It's your turn "+this.currentPlayer+"!");
        System.out.println("Your hand contains: "+this.currentPlayer.getHandStr());
    }

    /**
     * Refreshes the pantry by discarding the current contents and drawing new items from the pantry deck.
     * The discarded items are added to the pantry discard pile.
     */
    public void refreshPantry(){
        if(getActionsRemaining()<=0){
            throw new TooManyActionsException();
        }
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
     * @throws IOException If there are any problems writing to the file.
     */
    public void saveState(File file) throws IOException{
        try(
                FileOutputStream gamefile=new FileOutputStream(file);
                ObjectOutputStream write=new ObjectOutputStream(gamefile);
                ){
            write.writeObject(this);
        }

    }

    /**
     * Starts the game with the given player names, 
     * initialising the customers from the customerDeckFile, 
     * shuffling the pantryDeck, setting up the pantry, 
     * and distributing the start hands among the players.
     * 
     * @param playerNames       a list of player names
     * @param customerDeckFile  the file path of the customer deck
     * (if there are less than 2 or more 5 players),
     * @throws IOException If there are any problems reading the file.
     */
    public void startGame(List<String> playerNames, String customerDeckFile) throws IOException{
        if(playerNames.size()>5 || playerNames.size()<2){
            throw new IllegalArgumentException("Invalid number of players!");
        }

        for(String p:playerNames){
            this.players.add(new Player(p));
        }

        this.currentPlayer = ((LinkedList<Player>)this.players).get(0);

        this.customers=new Customers(customerDeckFile, this.random, this.layers, this.players.size());

        Collections.shuffle((List<Ingredient>)this.pantryDeck, this.random);

        for(int i = 0; i < (this.players.size()%2)+1; i++){
            this.customers.addCustomerOrder();
        }


        for(int i=0; i<5; i++){
            this.pantry.add(drawFromPantryDeck());
        }

        for(Player player:this.players){
            for(int i=0; i<3; i++){
                player.addToHand(drawFromPantryDeck());
            }
        }
        
    }
}
