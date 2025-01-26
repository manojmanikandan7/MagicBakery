import java.io.IOException;
import java.util.ArrayList;

import bakery.Ingredient;
import bakery.Layer;
import bakery.MagicBakery;
import bakery.Player;
import bakery.MagicBakery.ActionType;
import util.ConsoleUtils;

public class BakeryDriver {

    private static final String IODIR = "io/";
    public BakeryDriver() {
    }

    public static void main(String[] args) throws IOException {
        
        MagicBakery ob=new MagicBakery(27, IODIR + "ingredients.csv" , IODIR + "layers.csv");
        ConsoleUtils in=new ConsoleUtils();
        ArrayList<String> playerstr=new ArrayList<String>(in.promptForNewPlayers("Who's playing?"));
        ob.startGame(playerstr, IODIR + "customers.csv");
        for(int i=0;i<20;i++){
            ob.printGameState();
            Player currentplayer=ob.getCurrentPlayer();
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
                    System.out.println(ingredient+" passed to "+player);
                    break;
                case BAKE_LAYER:
                    Layer layer=in.promptForLayer("Which layer do you want to bake? ", ob.getBakeableLayers());
                    if(layer==null){
                        System.out.println("Sorry! You don't have the ingredients to bake a layer.");
                    }
                    if(in.promptForYesNo("Do you want to bake "+layer+" ?")){
                        ob.bakeLayer(layer);
                        System.out.println(layer+" baked!");
                    }
                case FULFIL_ORDER:
                    break;
                case REFRESH_PANTRY:
                    ob.refreshPantry();
                    System.out.println("Pantry Refreshed!");
                    break;
                default:
                    System.out.println("Wrong choice!");
            }
        }
    }

}