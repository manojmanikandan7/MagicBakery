import java.util.ArrayList;

import bakery.Ingredient;
import bakery.MagicBakery;
import bakery.Player;
import util.ConsoleUtils;

public class BakeryDriver {

    public BakeryDriver() {
    }

    public static void main(String[] args)  {
        
        MagicBakery ob=new MagicBakery(10, "../../io/ingredients.csv" , "../../io/layers.csv");
        ConsoleUtils in=new ConsoleUtils();
        ArrayList<String> playerstr=new ArrayList<String>(in.promptForNewPlayers("Who's playing?"));
        ob.startGame(playerstr, null);
        ob.getCurrentPlayer().addToHand(new Ingredient("flour"));
        ob.getCurrentPlayer().addToHand(new Ingredient("chocolate"));        
        ob.getCurrentPlayer().addToHand(new Ingredient("butter"));
        for(int i=0;i<20;i++){
            ob.endTurn();
            Player currentplayer=ob.getCurrentPlayer();
            System.out.println("The current player: "+currentplayer);
            System.out.println(ob.getActionsRemaining());
            Player player=in.promptForExistingPlayer("Enter the name of the user to pass the card to: ", ob);
            ob.passCard(in.promptForIngredient("Enter the ingredient to pass to the other user: ", currentplayer.getHand()), player);
            System.out.println(currentplayer.getHand());
            System.out.println(player.getHand());

        }
    }

}