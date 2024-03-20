package util;

import java.lang.Object;
import java.util.List;


import java.util.ArrayList;
import java.util.Collection;
import java.io.Console;
import java.io.File;

import bakery.CustomerOrder;
import bakery.Ingredient;
import bakery.MagicBakery;
import bakery.Player;
import bakery.MagicBakery.ActionType;

public class ConsoleUtils {
    private Console console;

    public ConsoleUtils(){
        console=System.console();
    }

    public String readLine(){
        String line=console.readLine();
        return line;
    }

    public String readLine(String fmt, Object... args){
        String line=console.readLine(fmt, args);
        return line;
    }

    //COME BACK
    public ActionType promptForAction(String prompt, MagicBakery bakery){
        ArrayList<ActionType> actions=new ArrayList<ActionType>();
        for(ActionType action:ActionType.values()){
            actions.add(action);
        }

        return (ActionType) promptEnumerateCollection(prompt, new ArrayList<Object>(actions));
    }

    public CustomerOrder promptForCustomer(String prompt, Collection<CustomerOrder> customers){
        
        //ArrayList<CustomerOrder> customer_list=new ArrayList<CustomerOrder>(customers);
        if(customers.size()==0){
            return null;
        }
        if(customers.size()==1){
            return ((ArrayList<CustomerOrder>)customers).get(0);
        }
        return (CustomerOrder) promptEnumerateCollection(prompt, new ArrayList<Object>(customers)); 
    }

    public Player promptForExistingPlayer(String prompt, MagicBakery bakery){

        ArrayList<Player> players=new ArrayList<Player>(bakery.getPlayers());
        players.remove(bakery.getCurrentPlayer());
        if(players.size()==1){
            return players.get(0);
        }

        return (Player) promptEnumerateCollection(prompt, new ArrayList<Object>(players));
        
        
    }

    public File promptForFilePath(String prompt){
        String pathname=readLine(prompt);
        File path=new File(pathname);
        return path;
    }

    public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients){
        //ArrayList<Ingredient> ingredient_list=new ArrayList<Ingredient>(ingredients);
        if(ingredients.size()==0){
            return null;
        }
        if(ingredients.size()==1){
            return ((ArrayList<Ingredient>)ingredients).get(0);
        }
        return (Ingredient) promptEnumerateCollection(prompt, new ArrayList<Object>(ingredients));

    }

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
