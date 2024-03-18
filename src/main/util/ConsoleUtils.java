package util;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;

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

    public File promptForFilePath(String prompt){
        String pathname=readLine(prompt);
        File path=new File(pathname);
        return path;
    }

    public ArrayList<String> promptForNewPlayers(String prompt){
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
                    System.out.println("Player with the name "+player_name+" exists! Try again.");
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
