import java.util.ArrayList;

import bakery.Ingredient;
import bakery.MagicBakery;
import bakery.Player;
import bakery.MagicBakery.ActionType;
import util.ConsoleUtils;

public class BakeryDriver {

    public BakeryDriver() {
    }

    public static void main(String[] args)  {
        
        MagicBakery ob=new MagicBakery(27, "./io/ingredients.csv" , "./io/layers.csv");
        ConsoleUtils in=new ConsoleUtils();
        ArrayList<String> playerstr=new ArrayList<String>(in.promptForNewPlayers("Who's playing?"));
        ob.startGame(playerstr, null);
        for(int i=0;i<20;i++){
            Player currentplayer=ob.getCurrentPlayer();
            System.out.println("It's your turn "+currentplayer+"!");
            System.out.println("Your hand contains: "+currentplayer.getHandStr());
            System.out.println("Actions remaining for this round: "+ob.getActionsRemaining());
            ActionType action=in.promptForAction("What action do you want do?", ob);
            Ingredient ingredient;
            switch(action){
                case DRAW_INGREDIENT:
                    ingredient=in.promptForIngredient("Which ingredient do you want to draw: ",ob.getPantry());
                    ob.drawFromPantry(ingredient);
                    break;
                case PASS_INGREDIENT:
                    Player player=in.promptForExistingPlayer("Enter the name of the user to pass the card to: ", ob);
                    ingredient=in.promptForIngredient("Enter the ingredient to pass to the other user: ", currentplayer.getHand());
                    ob.passCard(ingredient, player);
                    break;
                case BAKE_LAYER:
                    System.out.println(ob.getBakeableLayers());
                case FULFIL_ORDER:
                    break;
                case REFRESH_PANTRY:
                    ob.refreshPantry();
                    System.out.println("Pantry Refreshed!");
                    break;
                default:
                    System.out.println("Wrong choice!");
            }
            ob.printGameState();
        }
    }

}