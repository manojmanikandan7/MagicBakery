import bakery.Ingredient;
import bakery.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.StringUtils;


import javafx.scene.Scene;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class Game extends Application {
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        MagicBakery ob1=new MagicBakery(10000, "./io/ingredients.csv", "./io/layers.csv");
        List<String> playernames = new ArrayList<>();
        playernames.add("Player A");
        playernames.add("Player B");

        ob1.startGame(playernames, "./io/customers.csv");
        VBox ph = new VBox();
        Scene sc = new Scene(ph);
        startGame(sc, ob1);
        stage.setScene(sc);
        stage.setTitle("Test");
        stage.show();
    }

    public static void startGame(Scene scene, MagicBakery ob){
        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 50));
        Parent leftPane = leftPane(ob);
        Parent rightPane = rightPane(ob);

        GridPane root = new GridPane();
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(10));
        root.add(title, 0, 0, 2, 1);
        root.add(leftPane, 0, 1, 1, 1);
        root.add(rightPane, 1, 1, 1, 1);
        scene.setRoot(root);
    }
    public static Parent rightPane(MagicBakery ob){
        ArrayList<MagicBakery.ActionType> actions=new ArrayList<MagicBakery.ActionType>();
        ArrayList<String> actionlist=new ArrayList<String>();
        ArrayList<RadioButton> choices = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();

        actions.add(MagicBakery.ActionType.DRAW_INGREDIENT);
        actionlist.add("Draw an ingredient from the pantry.");
        if(!ob.getCurrentPlayer().getHand().isEmpty()){
            actions.add(MagicBakery.ActionType.PASS_INGREDIENT);
            actionlist.add("Pass an ingredient to another player.");
        }
        if(ob.getBakeableLayers().isEmpty()){
            actions.add(MagicBakery.ActionType.BAKE_LAYER);
            actionlist.add("Bake a layer.");
        }
        if(!ob.getFulfilableCustomers().isEmpty()){
            actions.add(MagicBakery.ActionType.FULFIL_ORDER);
            actionlist.add("Fulfill an order.");

        }
        actions.add(MagicBakery.ActionType.REFRESH_PANTRY);
        actionlist.add("Refresh the pantry.");

        for(String action : actionlist){
            RadioButton radio = new RadioButton(action);
            radio.setToggleGroup(group);
            choices.add(radio);
        }

        Button submit = new Button("Select Option");
        submit.setDefaultButton(true);
        submit.setPadding(new Insets(10));
        submit.setOnAction(e->{
            RadioButton choice = (RadioButton) group.getSelectedToggle();
            int index = actionlist.indexOf(choice.getText());
            Ingredient ingredient = null;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            switch(actions.get(index)) {
                case DRAW_INGREDIENT:
                    ingredient = promptForIngredient("Which ingredient do you want to draw: ", ob.getPantry());
                    ob.drawFromPantry(ingredient);
                    break;
                case PASS_INGREDIENT:
                    Player player = promptForExistingPlayer("Enter the name of the user to pass the card to: ", ob);
                    ingredient = promptForIngredient("Enter the ingredient to pass to the other user: ", ob.getCurrentPlayer().getHand());
                    ob.passCard(ingredient, player);
                    alert.setTitle("Pass Ingredient");
                    alert.setContentText(ingredient + " passed to " + player);
                    alert.show();
                    break;
                case BAKE_LAYER:
                    Layer layer = promptForLayer("Which layer do you want to bake? ", ob.getBakeableLayers());
                    if (layer == null) {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle("Error while baking");
                        alert.setContentText("Sorry! You don't have the ingredients to bake a layer.");
                    }
                    if (promptForYesNo("Do you want to bake " + layer + " ?")) {
                        ob.bakeLayer(layer);
                        alert.setTitle("Bake Layer");
                        alert.setContentText(layer + " baked!");
                    }
                case FULFIL_ORDER:
                    //TODO
                    break;
                case REFRESH_PANTRY:
                    ob.refreshPantry();
                    alert.setTitle("Refresh Pantry");
                    alert.setContentText("Pantry Refreshed!");
                    break;
                default:
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Wrong Choice!");
            }
        });

        Label actionlabel = new Label("Choose your action");
        actionlabel.setFont(Font.font("Futura", 25));
        actionlabel.setLabelFor(submit);

        VBox rightPane = new VBox(10);
        rightPane.setAlignment(Pos.CENTER);
        rightPane.setPadding(new Insets(10));
        rightPane.getChildren().addAll(actionlabel, new Separator());
        HBox choicesbox = new HBox(10);
        choicesbox.getChildren().addAll(choices);
        rightPane.getChildren().add(choicesbox);
        rightPane.getChildren().add(submit);

        return rightPane;

    }

    public static Parent leftPane(MagicBakery ob){
        List<Ingredient> pantry = new ArrayList<>(ob.getPantry());
        List<Layer> layers = new ArrayList<>(ob.getLayers());
        List<CustomerOrder> activeCustomers = new ArrayList<>(ob.getCustomers().getActiveCustomers());


        HBox pantryshelf = new HBox(15);
        pantryshelf.setPadding(new Insets(10));
        pantryshelf.setAlignment(Pos.CENTER_LEFT);

        //For indicating the pantry deck
        pantryshelf.getChildren().add(ingredientToCard(new Ingredient("?")));
        pantry.forEach(ingredient -> pantryshelf.getChildren().add(ingredientToCard(ingredient)));

        HBox layershelf = new HBox(15);
        layershelf.setPadding(new Insets(10));
        layershelf.setAlignment(Pos.CENTER_LEFT);

        layers.forEach(layer -> layershelf.getChildren().add(layerToCard(layer)));

        HBox customershelf = new HBox(15);
        customershelf.setPadding(new Insets(10));
        customershelf.setAlignment(Pos.CENTER_LEFT);

        activeCustomers.forEach(customer -> customershelf.getChildren().add(customerToCard(customer)));

        HBox playerHand = new HBox(15);
        playerHand.setPadding(new Insets(10));
        playerHand.setAlignment(Pos.CENTER_LEFT);

        ob.getCurrentPlayer().getHand().forEach(ingredient -> playerHand.getChildren().add(ingredientToCard(ingredient)));


        //Labels

        Label currentPlayer = new Label(ob.getCurrentPlayer().toString()+"'s Turn");
        currentPlayer.setFont(Font.font("Futura", 25));
        Label actionsRemaining = new Label("You have "+ob.getActionsRemaining()+" actions left for this round");
        actionsRemaining.setFont(Font.font("Futura", 25));

        Label pantrytext = new Label("Pantry");
        pantrytext.setFont(Font.font("Futura", 23));
        pantrytext.setLabelFor(pantryshelf);

        Label layertext = new Label("Layers");
        layertext.setFont(Font.font("Futura", 23));
        layertext.setLabelFor(layershelf);

        Label customertext = new Label("Customers");
        customertext.setFont(Font.font("Futura", 23));
        customertext.setLabelFor(customershelf);

        Label handLabel = new Label("Your hand");
        handLabel.setFont(Font.font("Futura", 23));
        handLabel.setLabelFor(playerHand);



        VBox leftPane = new VBox(10);
        leftPane.setAlignment(Pos.TOP_LEFT);
        leftPane.setPadding(new Insets(10));
        leftPane.getChildren().addAll(currentPlayer, actionsRemaining, new Separator(),
                handLabel, playerHand, new Separator(),
                pantrytext, pantryshelf, new Separator(),
                layertext, layershelf, new Separator(),
                customertext, customershelf);

        return leftPane;
    }

    public static Node ingredientToCard(Ingredient ingredient){
        Label titleLabel = new Label(ingredient.toString().toUpperCase());
        titleLabel.setFont(Font.font("Verdana", 22));


        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().add(titleLabel);
        contentBox.setPadding(new Insets(10));
        contentBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));


        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(200, 100);
        cardLayout.setMaxSize(250, 100);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node layerToCard(Layer layer){
        Label titleLabel = new Label(StringUtils.toTitleCase(layer.toString()));
        titleLabel.setFont(Font.font("Verdana", 25));
        Label bodyLabel = new Label("RECIPE");
        bodyLabel.setFont(Font.font("Verdana", 20));
        ArrayList<Label> recipes = new ArrayList<>();

        layer.getRecipe().forEach(recipe->{
            Label recipelabel = new Label(StringUtils.toTitleCase(recipe.toString()));
            recipelabel.setFont(Font.font("Verdana", 18));
            recipes.add(recipelabel);
        });



        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(titleLabel, new Separator(), bodyLabel, new Separator());
        contentBox.getChildren().addAll(recipes);
        contentBox.getChildren().add(new Separator());


        contentBox.setPadding(new Insets(15));
        contentBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(200, 250);
        cardLayout.setMaxSize(250, 275);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node customerToCard(CustomerOrder customer){
        String patience="";
        if(customer.getStatus() == CustomerOrder.CustomerOrderStatus.IMPATIENT){
            patience="\t ⌛";
        }
        Label levelLabel = new Label("Lvl " +customer.getLevel());
        levelLabel.setFont(Font.font("Verdana", 23));
        Label titleLabel = new Label(customer.toString().toUpperCase()+patience);
        titleLabel.setFont(Font.font("Verdana", 23));
        HBox header = new HBox(20);
        header.getChildren().addAll(levelLabel, new Separator(Orientation.VERTICAL), titleLabel);

        Label recipeLabel = new Label("RECIPE");
        recipeLabel.setFont(Font.font("Verdana", 20));



        ArrayList<Label> recipes = new ArrayList<>();


        customer.getRecipe().forEach(recipe->{
            Label recipelabel = new Label(StringUtils.toTitleCase(recipe.toString()));
            recipelabel.setFont(Font.font("Verdana", 18));
            recipes.add(recipelabel);
        });



        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(10));
        contentBox.getChildren().addAll(header, new Separator(), recipeLabel, new Separator());
        contentBox.getChildren().addAll(recipes);
        contentBox.getChildren().add(new Separator());

        if(customer.getGarnish() != null && !customer.getGarnish().isEmpty()){
            Label garnishLabel = new Label("GARNISH");
            garnishLabel.setFont(Font.font("Verdana", 20));
            ArrayList<Label> garnishes = new ArrayList<>();
            customer.getGarnish().forEach(garnish->{
                Label garnishlabel = new Label(StringUtils.toTitleCase(garnish.toString()));
                garnishlabel.setFont(Font.font("Verdana", 18));
                garnishes.add(garnishlabel);
            });
            contentBox.getChildren().addAll(garnishLabel, new Separator());
            contentBox.getChildren().addAll(garnishes);
            contentBox.getChildren().add(new Separator());
        }


        contentBox.setPadding(new Insets(15));
        contentBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(400, 350);
        cardLayout.setMaxSize(450, 450);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Ingredient promptForIngredient(String prompt, Collection<Ingredient> ingredients){
        //TODO
        return null;
    }

    public static Layer promptForLayer(String prompt, Collection<Layer> layers){
        //TODO
        return null;
    }

    public static Player promptForExistingPlayer(String prompt, MagicBakery ob){
        //TODO
        return null;
    }

    public static boolean promptForYesNo(String prompt){
        //TODO
        return false;
    }



}

