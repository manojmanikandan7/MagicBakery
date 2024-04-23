package ui;

import bakery.*;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.StringUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
public class Game {
    public static MagicBakery ob;
    public static Scene scene;
//    public static void main(String[] args){
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage stage) throws IOException {
//
//        MagicBakery ob1=new MagicBakery(10000, "./io/ingredients.csv", "./io/layers.csv");
//        List<String> playernames = new ArrayList<>();
//        playernames.add("Player A");
//        playernames.add("Player B");
//
//
//        ob1.startGame(playernames, "./io/customers.csv");
//        VBox ph = new VBox();
//        Scene sc = new Scene(ph);
//        startGame(sc, ob1);
//        stage.setScene(scene);
//        stage.setTitle("Test");
//        stage.show();
//    }

    public static void startGame(Scene scene, MagicBakery ob){
        Game.ob = ob;
        Game.scene = scene;
        ((Stage)scene.getWindow()).setTitle("Magic Bakery");

        if(ob.getActionsRemaining()==0){
            ob.endTurn();
            if(ob.getCurrentPlayer().toString().equals(((LinkedList<Player>)ob.getPlayers()).get(0).toString())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("New Round");
                alert.setContentText("New Round! \n More Customers incoming!");
                alert.show();
            }
        }


        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 50));
        Parent leftPane = leftPane();
        Parent rightPane = rightPane();

        HBox panes = new HBox(20);
        panes.setAlignment(Pos.CENTER);
        panes.setPadding(new Insets(10));
        panes.getChildren().addAll(leftPane, new Separator(Orientation.VERTICAL), rightPane);

        VBox root = new VBox(title, new Separator(), panes);
        root.setAlignment(Pos.TOP_CENTER);

        scene.setRoot(root);
    }
    public static Parent rightPane(){
        ArrayList<MagicBakery.ActionType> actions= new ArrayList<>();
        ArrayList<String> actionlist=new ArrayList<>();
        ArrayList<RadioButton> choices = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();

        actions.add(MagicBakery.ActionType.DRAW_INGREDIENT);
        actionlist.add("Draw an ingredient from the pantry");
        if(!ob.getCurrentPlayer().getHand().isEmpty()){
            actions.add(MagicBakery.ActionType.PASS_INGREDIENT);
            actionlist.add("Pass an ingredient to another player");
        }
        if(!ob.getBakeableLayers().isEmpty()){
            actions.add(MagicBakery.ActionType.BAKE_LAYER);
            actionlist.add("Bake a layer");
        }
        if(!ob.getFulfilableCustomers().isEmpty()){
            actions.add(MagicBakery.ActionType.FULFIL_ORDER);
            actionlist.add("Fulfill an order");

        }
        actions.add(MagicBakery.ActionType.REFRESH_PANTRY);
        actionlist.add("Refresh the pantry");

        for(String action : actionlist){
            RadioButton radio = new RadioButton(action);
            radio.setFont(Font.font("Verdana", 18));
            radio.setToggleGroup(group);
            choices.add(radio);
        }

        Button submit = new Button("Select Option");
        submit.setDefaultButton(true);
        submit.setPadding(new Insets(10));

        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setPadding(new Insets(10));

        Button save = new Button("Save ui.Game");
        save.setPadding(new Insets(10));


        Label statuslabel = new Label("Current Status");
        statuslabel.setFont(Font.font("Futura", 27));

        Label actionlabel = new Label("Your Actions");
        actionlabel.setFont(Font.font("Futura", 27));
        actionlabel.setLabelFor(submit);

        int fulfilled = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.FULFILLED).size();
        int garnished = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GARNISHED).size();
        int abandoned = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GIVEN_UP).size();

        String text = "Happy customers eating baked goods:) : " + (fulfilled+garnished) +" (" + garnished + " garnished) \n" +
                "Gone to Greggs instead:( : " + abandoned + "\n" +
                "Customers waiting to enter: " + ob.getCustomers().getCustomerDeck().size();


        VBox rightPane = new VBox(15);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(10));
        rightPane.getChildren().addAll(save, new Separator(), statuslabel, statCards(text), new Separator(), actionlabel, new Separator());
        HBox choicesbox = new HBox(10);
        choicesbox.getChildren().addAll(choices);
        rightPane.getChildren().add(choicesbox);
        HBox buttons = new HBox(20, submit, cancel);
        buttons.setAlignment(Pos.CENTER);
        rightPane.getChildren().add(buttons);

        cancel.setOnAction(e1->startGame(scene, ob));

        save.setOnAction(e->{
            Label fileLabel = new Label("File path: ");
            TextField file = new TextField();
            file.setMaxWidth(scene.getWidth() / 5);
            fileLabel.setLabelFor(file);


            HBox form = new HBox(20, fileLabel, file, cancel);
            form.setAlignment(Pos.CENTER);
            int index = rightPane.getChildren().indexOf(save);
            rightPane.getChildren().add(index, form);

            save.setOnAction(e1->{
                try {
                    ob.saveState(new File(file.getText().trim()));
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error while writing the file, try again");
                }
                finally {
                    startGame(scene, ob);
                }

            });
        });

        submit.setOnAction(e->{
            RadioButton choice = (RadioButton) group.getSelectedToggle();
            if(choice == null){
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No Actions chosen");
                warn.setContentText("No Action was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
            else {
                int index = actionlist.indexOf(choice.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                switch(actions.get(index)) {
                    case DRAW_INGREDIENT:
                        System.out.println("Success");
                        rightPane.getChildren().add(rightPane.getChildren().size()-1, drawIngredient("Which ingredient do you want to draw: ", ob.getPantry(), submit));
                        break;
                    case PASS_INGREDIENT:
                        rightPane.getChildren().add(rightPane.getChildren().size()-1, passCard("Which player do you want to pass to? ", submit));
                        break;
                    case BAKE_LAYER:
                        rightPane.getChildren().add(rightPane.getChildren().size()-1, bakeLayer("Which layer do you want to bake? ", ob.getBakeableLayers(), submit));
                        break;
                    case FULFIL_ORDER:
                        rightPane.getChildren().add(rightPane.getChildren().size()-1, fulfillOrder("Which order do you want to fulfill? ", ob.getFulfilableCustomers(), submit));
                        break;
                    case REFRESH_PANTRY:
                        ob.refreshPantry();
                        alert.setTitle("Refresh Pantry");
                        alert.setContentText("Pantry Refreshed!");
                        alert.show();
                        startGame(scene, ob);
                        break;
                    default:
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Wrong Choice!");
                        alert.show();
                }
            }
        });

        return rightPane;

    }

    public static Parent leftPane(){
        ArrayList<Ingredient> pantry = new ArrayList<>(ob.getPantry());
        ArrayList<Layer> layers = new ArrayList<>(ob.getLayers());
        ArrayList<CustomerOrder> activeCustomers = new ArrayList<>(ob.getCustomers().getActiveCustomers());


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
        Label titleLabel = new Label((ingredient.equals(Ingredient.HELPFUL_DUCK))?
                "HELPFUL DUCK \uD83D\uDC24":
                ingredient.toString().toUpperCase());
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
        if(customer == null){
            return ingredientToCard(new Ingredient("[EMPTY]"));
        }
        String patience="";
        if(customer.getStatus() == CustomerOrder.CustomerOrderStatus.IMPATIENT){
            patience=" ⌛";
        }
        Label levelLabel = new Label("Lvl " +customer.getLevel());
        levelLabel.setFont(Font.font("Verdana", 20));
        Label titleLabel = new Label(customer.toString().toUpperCase()+patience);
        titleLabel.setFont(Font.font("Verdana", 20));
        HBox header = new HBox(7);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(levelLabel, new Separator(Orientation.VERTICAL), titleLabel);

        Label recipeLabel = new Label("RECIPE");
        recipeLabel.setFont(Font.font("Verdana", 18));



        ArrayList<Label> recipes = new ArrayList<>();


        customer.getRecipe().forEach(recipe->{
            Label recipelabel = new Label(StringUtils.toTitleCase(recipe.toString()));
            recipelabel.setFont(Font.font("Verdana", 15));
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
            garnishLabel.setFont(Font.font("Verdana", 18));
            ArrayList<Label> garnishes = new ArrayList<>();
            customer.getGarnish().forEach(garnish->{
                Label garnishlabel = new Label(StringUtils.toTitleCase(garnish.toString()));
                garnishlabel.setFont(Font.font("Verdana", 15));
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

    public static Node statCards(String text){
        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font("Verdana", 22));


        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().add(titleLabel);
        contentBox.setPadding(new Insets(10));
        contentBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));


        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(400, 350);
        cardLayout.setMaxSize( 700, 350);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }
    public static Node drawIngredient(String prompt, Collection<Ingredient> ingredients, Button submit){
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 20));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        ingredients.forEach(ingredient -> {
            RadioButton radio = new RadioButton((ingredient.equals(Ingredient.HELPFUL_DUCK))?
                    "HELPFUL DUCK \uD83D\uDC24":
                    ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });

        submit.setOnAction(e->{
            String choice = null;
            try {
                choice = ((RadioButton)toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
            if(choice.equals("HELPFUL DUCK \uD83D\uDC24")){
                System.out.println("test");
                choice=Ingredient.HELPFUL_DUCK.toString().toUpperCase();
            }
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ((ArrayList<Ingredient>)ingredients).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.drawFromPantry(ingredient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Draw Ingredient");
                    alert.setContentText(ingredient.toString().toUpperCase() + " drawn from the pantry.");
                    alert.show();
                    startGame(scene, ob);
                    break;
                }
            }
        });

        VBox optionBox = new VBox(15);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.getChildren().addAll(promptText, new Separator());
        optionBox.getChildren().addAll(options);
        optionBox.getChildren().add(submit);
        return optionBox;
    }
    public static Node passIngredient(String prompt, Collection<Ingredient> ingredients, Player recipient, Button submit){
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 20));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        ingredients.forEach(ingredient -> {
            RadioButton radio = new RadioButton(ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });


        submit.setOnAction(e->{

            String choice = null;
            try {
                choice = ((RadioButton)toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
            if(choice.equals("HELPFUL DUCK \uD83D\uDC24")){
                choice=Ingredient.HELPFUL_DUCK.toString().toUpperCase();
            }

            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ((ArrayList<Ingredient>)ingredients).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.passCard(ingredient, recipient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pass Ingredient");
                    alert.setContentText(ingredient.toString().toUpperCase() + " passed to " + recipient);
                    alert.show();
                    startGame(scene, ob);
                    break;
                }
            }

        });

        VBox optionBox = new VBox(15);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.getChildren().addAll(promptText, new Separator());
        optionBox.getChildren().addAll(options);
        optionBox.getChildren().add(submit);
        return optionBox;
    }
    public static Node passCard(String prompt, Button submit){
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 20));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        ob.getPlayers().forEach(player -> {
            if (!player.equals(ob.getCurrentPlayer())) {
                RadioButton radio = new RadioButton(player.toString());
                radio.setToggleGroup(toggle);
                options.add(radio);
            }
        });

        VBox optionBox = new VBox(15);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.getChildren().addAll(promptText, new Separator());
        optionBox.getChildren().addAll(options);
        optionBox.getChildren().add(submit);


        submit.setOnAction(e->{
            Player selectedPlayer = null;
            String choice = null;
            try {
                choice = ((RadioButton)toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
                for(Player player : ob.getPlayers()){
                    if(player.toString().equals(choice)){
                        selectedPlayer = player;
                    }
                }
                assert selectedPlayer != null;
                optionBox.getChildren().add(passIngredient("Enter the ingredient to pass to "+ selectedPlayer +": ", ob.getCurrentPlayer().getHand(), selectedPlayer, submit));
        });


        return optionBox;
    }
    public static Node bakeLayer(String prompt, Collection<Layer> layers, Button submit){
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 23));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        layers.forEach(ingredient -> {
            RadioButton radio = new RadioButton(ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });


        submit.setOnAction(e->{
            String choice = null;
            try {
                choice = ((RadioButton)toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
            for (int i = 0; i < layers.size(); i++) {
                Layer ingredient = ((ArrayList<Layer>)layers).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.bakeLayer(ingredient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Bake layer");
                    alert.setContentText(ingredient.toString().toUpperCase() + " baked.");
                    alert.show();
                    startGame(scene, ob);
                    break;
                }
            }
        });

        VBox optionBox = new VBox(15);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.getChildren().addAll(promptText, new Separator());
        optionBox.getChildren().addAll(options);
        return optionBox;
    }
    public static Node fulfillOrder(String prompt, Collection<CustomerOrder> customers, Button submit){
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 23));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        customers.forEach(ingredient -> {
            RadioButton radio = new RadioButton(ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });

        VBox optionBox = new VBox(15);
        optionBox.setAlignment(Pos.CENTER);
        optionBox.getChildren().addAll(promptText, new Separator());
        optionBox.getChildren().addAll(options);


        submit.setOnAction(e->{
            String choice = null;
            try {
                choice = ((RadioButton)toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            }
            for (int i = 0; i < customers.size(); i++) {
                CustomerOrder customer = ((ArrayList<CustomerOrder>)customers).get(i);
                if (customer.toString().toUpperCase().equals(choice)) {
                    if(!customer.getGarnish().isEmpty()){
                        ArrayList<Ingredient> copy = new ArrayList<>(ob.getCurrentPlayer().getHand());
                        ob.getCurrentPlayer().getHand().forEach(ingredient -> {
                            if(customer.getRecipe().contains(ingredient)){
                                copy.remove(ingredient);
                            }
                        });

                        if(customer.canGarnish(copy)){
                            Alert garnish = new Alert(Alert.AlertType.NONE, "Garnish", ButtonType.YES, ButtonType.NO);
                            garnish.showAndWait().ifPresent(event->{
                                ButtonType result = garnish.getResult();
                                if(result == ButtonType.YES){
                                    ob.fulfillOrder(customer, true);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Fulfill and Garnish order");
                                    alert.setContentText(customer.toString().toUpperCase() + " Fulfilled and Garnished!");
                                    alert.show();
                                }
                                else{
                                    ob.fulfillOrder(customer, false);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Fulfill order");
                                    alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                                    alert.show();
                                }
                                startGame(scene, ob);
                            });
                            break;
                        }
                        else {
                            ob.fulfillOrder(customer, false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Fulfill order");
                            alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                            alert.show();
                            startGame(scene, ob);
                            break;
                        }
                    }
                    else {
                        ob.fulfillOrder(customer, false);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Fulfill order");
                        alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                        alert.show();
                        startGame(scene, ob);
                        break;
                    }


                }
            }
        });


        return optionBox;
    }
}