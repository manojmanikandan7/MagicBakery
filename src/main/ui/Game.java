package ui;

import bakery.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    public static MagicBakery ob;
    public static Scene scene;

    public static void startGame(Scene scene, MagicBakery ob) {
        Game.ob = ob;
        Game.scene = scene;
        new Components(scene, ob);
        ((Stage) scene.getWindow()).setTitle("Magic Bakery");

        if (ob.getActionsRemaining() == 0) {
            ob.endTurn();
            if (ob.getCurrentPlayer().toString().equals(((LinkedList<Player>) ob.getPlayers()).get(0).toString())) {
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

    public static Parent rightPane() {
        ArrayList<MagicBakery.ActionType> actions = new ArrayList<>();
        ArrayList<String> actionlist = new ArrayList<>();
        ArrayList<RadioButton> choices = new ArrayList<>();
        ToggleGroup group = new ToggleGroup();

        actions.add(MagicBakery.ActionType.DRAW_INGREDIENT);
        actionlist.add("Draw an ingredient from the pantry");

        actions.add(MagicBakery.ActionType.REFRESH_PANTRY);
        actionlist.add("Refresh the pantry");

        if (!ob.getCurrentPlayer().getHand().isEmpty()) {
            actions.add(MagicBakery.ActionType.PASS_INGREDIENT);
            actionlist.add("Pass an ingredient to another player");
        }
        if (!ob.getBakeableLayers().isEmpty()) {
            actions.add(MagicBakery.ActionType.BAKE_LAYER);
            actionlist.add("Bake a layer");
        }
        if (!ob.getFulfilableCustomers().isEmpty()) {
            actions.add(MagicBakery.ActionType.FULFIL_ORDER);
            actionlist.add("Fulfill an order");

        }

        for (String action : actionlist) {
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

        Button save = new Button("Save Game");
        save.setPadding(new Insets(10));
        save.setAlignment(Pos.CENTER);

        Label playersno = new Label("Number of players: " + ob.getPlayers().size());
        playersno.setFont(Font.font("Futura", 20));
        playersno.setAlignment(Pos.CENTER_LEFT);

        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(playersno, new Separator(Orientation.VERTICAL), save);

        Label statuslabel = new Label("Current Status");
        statuslabel.setFont(Font.font("Futura", 27));

        Label actionlabel = new Label("Your Actions");
        actionlabel.setFont(Font.font("Futura", 27));
        actionlabel.setLabelFor(submit);

        int fulfilled = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.FULFILLED)
                .size();
        int garnished = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GARNISHED)
                .size();
        int abandoned = ob.getCustomers().getInactiveCustomersWithStatus(CustomerOrder.CustomerOrderStatus.GIVEN_UP)
                .size();

        String text = "Happy customers eating baked goods:) : " + (fulfilled + garnished) + " (" + garnished
                + " garnished) \n" +
                "Gone to Greggs instead:( : " + abandoned + "\n" +
                "Customers waiting to enter: " + ob.getCustomers().getCustomerDeck().size();

        VBox rightPane = new VBox(15);
        rightPane.setAlignment(Pos.TOP_CENTER);
        rightPane.setPadding(new Insets(10));
        rightPane.getChildren().addAll(header, new Separator(), statuslabel, Components.statCards(text),
                new Separator(), actionlabel, new Separator());

        HBox row1 = new HBox(10);
        row1.setAlignment(Pos.CENTER);
        row1.getChildren().addAll(choices.subList(0, 2));

        HBox row2 = new HBox(10);
        row2.setAlignment(Pos.CENTER);
        choices.subList(2, choices.size()).forEach(choice -> row2.getChildren().add(choice));

        rightPane.getChildren().addAll(row1, row2, new Separator());
        HBox buttons = new HBox(20, submit, cancel);
        buttons.setAlignment(Pos.CENTER);
        rightPane.getChildren().add(buttons);

        cancel.setOnAction(e1 -> startGame(scene, ob));

        save.setOnAction(e -> {
            Label fileLabel = new Label("File path: ");
            TextField file = new TextField();
            file.setMaxWidth(scene.getWidth() / 5);
            fileLabel.setLabelFor(file);

            HBox form = new HBox(20, fileLabel, file, cancel);
            form.setAlignment(Pos.CENTER);
            int index = rightPane.getChildren().indexOf(header);
            rightPane.getChildren().add(index, form);

            save.setOnAction(e1 -> {
                try {
                    ob.saveState(new File(file.getText().trim()));
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Error while writing the file, try again");
                } finally {
                    startGame(scene, ob);
                }

            });
        });

        submit.setOnAction(e -> {
            RadioButton choice = (RadioButton) group.getSelectedToggle();
            if (choice == null) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No Actions chosen");
                warn.setContentText("No Action was chosen, please try again");
                warn.show();
                startGame(scene, ob);
            } else {
                int index = actionlist.indexOf(choice.getText());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                switch (actions.get(index)) {
                    case DRAW_INGREDIENT:
                        System.out.println("Success");
                        rightPane.getChildren().add(rightPane.getChildren().size() - 1, Components
                                .drawIngredient("Which ingredient do you want to draw: ", ob.getPantry(), submit));
                        break;
                    case PASS_INGREDIENT:
                        rightPane.getChildren().add(rightPane.getChildren().size() - 1,
                                Components.passCard("Which player do you want to pass to? ", submit));
                        break;
                    case BAKE_LAYER:
                        rightPane.getChildren().add(rightPane.getChildren().size() - 1, Components
                                .bakeLayer("Which layer do you want to bake? ", ob.getBakeableLayers(), submit));
                        break;
                    case FULFIL_ORDER:
                        rightPane.getChildren().add(rightPane.getChildren().size() - 1, Components.fulfillOrder(
                                "Which order do you want to fulfill? ", ob.getFulfilableCustomers(), submit));
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

    public static Parent leftPane() {
        ArrayList<Ingredient> pantry = new ArrayList<>(ob.getPantry());
        ArrayList<Layer> layers = new ArrayList<>(ob.getLayers());
        ArrayList<CustomerOrder> activeCustomers = new ArrayList<>(ob.getCustomers().getActiveCustomers());

        HBox pantryshelf = new HBox(15);
        pantryshelf.setPadding(new Insets(10));
        pantryshelf.setAlignment(Pos.CENTER_LEFT);

        // For indicating the pantry deck
        pantryshelf.getChildren().add(Components.ingredientToCard(new Ingredient("?")));
        pantry.forEach(ingredient -> pantryshelf.getChildren().add(Components.ingredientToCard(ingredient)));

        HBox layershelf = new HBox(15);
        layershelf.setPadding(new Insets(10));
        layershelf.setAlignment(Pos.CENTER_LEFT);

        layers.forEach(layer -> layershelf.getChildren().add(Components.layerToCard(layer)));

        HBox customershelf = new HBox(15);
        customershelf.setPadding(new Insets(10));
        customershelf.setAlignment(Pos.CENTER_LEFT);

        activeCustomers.forEach(customer -> customershelf.getChildren().add(Components.customerToCard(customer)));

        HBox playerHand = new HBox(15);
        playerHand.setPadding(new Insets(10));
        playerHand.setAlignment(Pos.CENTER_LEFT);

        ob.getCurrentPlayer().getHand()
                .forEach(ingredient -> playerHand.getChildren().add(Components.ingredientToCard(ingredient)));

        ScrollPane hand = new ScrollPane(playerHand);
        hand.setPadding(new Insets(10));
        hand.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        hand.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        hand.setPrefViewportHeight(125);
        hand.setPrefViewportWidth(700);

        // Labels

        Label currentPlayer = new Label(ob.getCurrentPlayer().toString() + "'s Turn");
        currentPlayer.setFont(Font.font("Futura", 25));
        Label actionsRemaining = new Label("You have " + ob.getActionsRemaining() + " actions left for this round");
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
                handLabel, hand, new Separator(),
                pantrytext, pantryshelf, new Separator(),
                layertext, layershelf, new Separator(),
                customertext, customershelf);

        return leftPane;
    }

}