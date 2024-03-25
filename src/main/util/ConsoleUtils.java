package util;

import java.lang.Object;
import java.lang.IllegalArgumentException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Console;
import java.io.File;

import bakery.CustomerOrder;
import bakery.Ingredient;
import bakery.Layer;
import bakery.MagicBakery;
import bakery.Player;
import bakery.MagicBakery.ActionType;

/**
 * A Class to prompt the users for the options and selections.
 *
 * @author Manoj Manikandan
 * @version %I%, %G%
 *
 */
public class ConsoleUtils {
    private Console console;

    /**
     * Constructor to initialise the console object.
     */
    public ConsoleUtils(){
        this.console=System.console();
    }

    /**
     * Reads a line from the console and return the line as a String.
     * Same as the method of System.console().
     *
     * @return A string of the line read from the console.
     */
    public String readLine(){
        return console.readLine();
    }

    /**
     * Provides a formatted prompt, then reads a single line of text from the console.
     * Same as the method of System.console().
     *
     * @param fmt A format string as described in Format string syntax.
     * @param args Arguments referenced by the format specifiers in the format string.
     * @return A string of the line read from the console.
     */
    public String readLine(String fmt, Object... args){
        return console.readLine(fmt, args);
    }

    /**
     * Prompts the user for the action they should take during their turn.
     *
     * @param prompt The prompt to be printed to the console.
     * @param bakery The MagicBakery object to derive the actions from.
     * @return An ActionType choice the player made.
     */
    //COME BACK
    public ActionType promptForAction(String prompt, MagicBakery bakery){
        ArrayList<ActionType> actions=new ArrayList<ActionType>();
        Collections.addAll(actions, ActionType.values());
        ArrayList<String> actionlist=new ArrayList<String>();
        actionlist.add("Draw an ingredient from the pantry.");
        actionlist.add("Pass an ingredient to another player.");
        actionlist.add("Bake a layer.");
        actionlist.add("Fulfill an order.");
        actionlist.add("Refresh the pantry.");

        String option=(String) promptEnumerateCollection(prompt, new ArrayList<Object>(actionlist));
        return actions.get(actionlist.indexOf(option));
    }

    /**
     * Prompts the user for a customer order to fulfill.
     *
     * @param prompt The prompt to be printed to the console.
     * @param customers The list of customer order.
     * @return A CustomerOrder object the user chose.
     */
    public CustomerOrder promptForCustomer(String prompt, Collection<CustomerOrder> customers){
        
        //ArrayList<CustomerOrder> customer_list=new ArrayList<CustomerOrder>(customers);
        if(customers.isEmpty()){
            return null;
        }
        if(customers.size()==1){
            return ((ArrayList<CustomerOrder>)customers).get(0);
        }
        return (CustomerOrder) promptEnumerateCollection(prompt, new ArrayList<Object>(customers)); 
    }

    /**
     * Prompts the user for another player pass a card to.
     *
     * @param prompt The prompt to be printed to the console.
     * @param bakery The MagicBakery object to get the players from.
     * @return A Player object the user chose.
     */
    public Player promptForExistingPlayer(String prompt, MagicBakery bakery){

        ArrayList<Player> players=new ArrayList<Player>(bakery.getPlayers());
        players.remove(bakery.getCurrentPlayer());
        if(players.size()==1){
            return players.get(0);
        }

        return (Player) promptEnumerateCollection(prompt, new ArrayList<Object>(players));
        
        
    }

    /**
     * Prompts the user for a file path.
     *
     * @param prompt The prompt to be printed to the console.
     * @return A File object representing the file path.
     */
    public File promptForFilePath(String prompt){
        String pathname=readLine(prompt);
        return new File(pathname);
    }

    /**
     * Prompts the user to choose an ingredient.
     *
     * @param prompt The prompt to be printed to the console.
     * @param ingredients The collection of ingredients to choose from.
     * @return An Ingredient object the user chose.
     */
    public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients){

        if(ingredients.isEmpty()){
            return null;
        }
        if(ingredients.size()==1){
            return ((ArrayList<Ingredient>)ingredients).get(0);
        }
        return (Ingredient) promptEnumerateCollection(prompt, new ArrayList<Object>(ingredients));

    }

    /**
     * Prompts the user to choose a layer.
     *
     * @param prompt The prompt to be printed to the console.
     * @param layers The collection of layer to choose from.
     * @return A Layer object the user chose.
     */
    public Layer promptForLayer(String prompt, Collection<Layer> layers){

        if(layers.isEmpty()){
            return null;
        }
        if(layers.size()==1){
            return ((ArrayList<Layer>)layers).get(0);
        }
        return (Layer) promptEnumerateCollection(prompt, new ArrayList<Object>(layers));
    }

    /**
     * Prompts the user for new players playing the game.
     *
     * @param prompt The prompt to be printed to the console.
     * @return A List of player names the user chose.
     */
    public List<String> promptForNewPlayers(String prompt){
        int player_number=1;
        ArrayList<String> players=new ArrayList<String>();
        System.out.println(prompt);
        players.add(readLine("Player 1 name: "));
        boolean more=true;
        do{
            player_number++;
            while(true){
                String player_name=readLine("Player "+player_number+" name: ");
                if(players.contains(player_name)){
                    System.out.println("Imposter detected! Player with the name "+player_name+" already exists. Try again.");
                    continue;
                }
                players.add(player_name);
                break;
            }
            
            if(player_number<5){
                more=promptForYesNo("Add another?"); 
            }
            
        }while(more && player_number<5);

        return players;
    }

    /**
     * Prompts the user whether to start a new game or load an existing one.
     *
     * @param prompt The prompt to be printed to the console.
     * @return True, if the user want to start a new game, false, if they want to load an existing one.
     */
    public boolean promptForStartLoad(String prompt){
        while (true) {
            try{
                char option=readLine(prompt=" [S]tart/[L]oad ").charAt(0);
                if(option=='S'||option=='s'){
                    return true;
                }
                else if(option=='L'||option=='l'){
                    return false;
                }
                else{
                    System.out.println("Invalid Option! Try Again ([S]tart/[L]oad)");
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("No Option given! Enter at least one character ([S]tart/[L]oad) ");
            }
        }
    }

    /**
     * Prompts the user for a yes or a no.
     *
     * @param prompt The prompt to be printed to the console.
     * @return True, if yes, false, if no.
     */
    public boolean promptForYesNo(String prompt){
        while (true) {
            try{
                char option=readLine(prompt+" [Y]es/[N]o ").charAt(0);
                if(option=='Y'||option=='y'){
                    return true;
                }
                else if(option=='N'||option=='n'){
                    return false;
                }
                else{
                    System.out.println("Invalid Option! Try Again ([Y]es/[N]o)");
                }
            }
            catch (IndexOutOfBoundsException e){
                System.out.println("No Option given! Enter at least one character ([Y]es/[N]o)");
            }
        }

    }
    
    private Object promptEnumerateCollection(String prompt, Collection<Object> collection){
        if(collection==null || collection.isEmpty()){
            throw new IllegalArgumentException("Empty or null Collection");
        }
        
        ArrayList<Object> list=new ArrayList<Object>(collection);
        
        System.out.println(prompt);

        int counter=1;
        for(Object object:list){
            System.out.println("["+counter+"] "+object);
            counter++;
        }
        
        while(true){
            try{
                int choice=Integer.parseInt(readLine());
                if(choice<1 || choice>list.size()){
                    System.out.println("Wrong Choice! Try again.");
                    continue;
                }
                return list.get(choice-1);
            }
            catch(NumberFormatException e){
                System.out.println("Please enter a number.");
            }
        }
    }
}
