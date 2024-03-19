package util;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.io.Console;
import java.io.File;

import bakery.Ingredient;
import bakery.MagicBakery;
import bakery.Player;

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

    public Player promptForExistingPlayer(String prompt, MagicBakery bakery){

        ArrayList<Player> players=new ArrayList<Player>(bakery.getPlayers());
        players.remove(bakery.getCurrentPlayer());
        if(players.size()==1){
            return players.get(0);
        }

        System.out.println(prompt);

        int counter=1;
        for(Player player:players){
            System.out.println("["+counter+"] "+player);
            counter++;
        }
        
        while(true){
            try{
                int choice=Integer.parseInt(readLine());
                if(choice<1 || choice>players.size()){
                    System.out.println("Wrong Choice! Try again.");
                    continue;
                }
                return players.get(choice-1);
            }
            catch(NumberFormatException e){
                System.out.println("Please enter a number.");
            }
        }
    }

    public File promptForFilePath(String prompt){
        String pathname=readLine(prompt);
        File path=new File(pathname);
        return path;
    }

    public Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients){
        ArrayList<Ingredient> ingredient_list=new ArrayList<Ingredient>(ingredients);
        if(ingredient_list.size()==0){
            return null;
        }
        if(ingredient_list.size()==1){
            return ingredient_list.get(0);
        }
        System.out.println(prompt);

        int counter=1;
        for(Ingredient ingredient:ingredient_list){
           System.out.println("["+ counter +"] "+ingredient); 
           counter++;
        }

        while(true){
            try{
                int choice=Integer.parseInt(readLine());
                if(choice<1 || choice>ingredient_list.size()){
                    System.out.println("Wrong Choice! Try again.");
                    continue;
                }
                return ingredient_list.get(choice-1);
            }
            catch(NumberFormatException e){
                System.out.println("Please enter a number.");
            }
        }

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
    
}
