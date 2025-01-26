package ui;

import bakery.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

public class Components {
    public static Scene scene;
    public static MagicBakery ob;

    public Components(Scene scene, MagicBakery ob) {
        Components.scene = scene;
        Components.ob = ob;

    }

    public static Node ingredientToCard(Ingredient ingredient) {
        Label titleLabel = new Label((ingredient.equals(Ingredient.HELPFUL_DUCK)) ? "HELPFUL DUCK \uD83D\uDC24"
                : ingredient.toString().toUpperCase());
        titleLabel.setFont(Font.font("Verdana", 22));

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().add(titleLabel);
        contentBox.setPadding(new Insets(10));
        contentBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(200, 100);
        cardLayout.setMaxSize(250, 100);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node layerToCard(Layer layer) {
        Label titleLabel = new Label(StringUtils.toTitleCase(layer.toString()));
        titleLabel.setFont(Font.font("Verdana", 25));
        Label bodyLabel = new Label("RECIPE");
        bodyLabel.setFont(Font.font("Verdana", 20));
        ArrayList<Label> recipes = new ArrayList<>();

        layer.getRecipe().forEach(recipe -> {
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
        contentBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(200, 250);
        cardLayout.setMaxSize(250, 275);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node customerToCard(CustomerOrder customer) {
        if (customer == null) {
            return ingredientToCard(new Ingredient("[EMPTY]"));
        }
        String patience = "";
        if (customer.getStatus() == CustomerOrder.CustomerOrderStatus.IMPATIENT) {
            patience = " ⌛";
        }
        Label levelLabel = new Label("Lvl " + customer.getLevel());
        levelLabel.setFont(Font.font("Verdana", 20));
        Label titleLabel = new Label(customer.toString().toUpperCase() + patience);
        titleLabel.setFont(Font.font("Verdana", 20));
        HBox header = new HBox(7);
        header.setAlignment(Pos.CENTER);
        header.getChildren().addAll(levelLabel, new Separator(Orientation.VERTICAL), titleLabel);

        Label recipeLabel = new Label("RECIPE");
        recipeLabel.setFont(Font.font("Verdana", 18));

        ArrayList<Label> recipes = new ArrayList<>();

        customer.getRecipe().forEach(recipe -> {
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

        if (customer.getGarnish() != null && !customer.getGarnish().isEmpty()) {
            Label garnishLabel = new Label("GARNISH");
            garnishLabel.setFont(Font.font("Verdana", 18));
            ArrayList<Label> garnishes = new ArrayList<>();
            customer.getGarnish().forEach(garnish -> {
                Label garnishlabel = new Label(StringUtils.toTitleCase(garnish.toString()));
                garnishlabel.setFont(Font.font("Verdana", 15));
                garnishes.add(garnishlabel);
            });
            contentBox.getChildren().addAll(garnishLabel, new Separator());
            contentBox.getChildren().addAll(garnishes);
            contentBox.getChildren().add(new Separator());
        }

        contentBox.setPadding(new Insets(15));
        contentBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(400, 350);
        cardLayout.setMaxSize(450, 450);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node statCards(String text) {
        Label titleLabel = new Label(text);
        titleLabel.setFont(Font.font("Verdana", 22));

        VBox contentBox = new VBox(10);
        contentBox.setAlignment(Pos.CENTER);

        contentBox.getChildren().add(titleLabel);
        contentBox.setPadding(new Insets(10));
        contentBox.setBorder(new Border(
                new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));

        BorderPane cardLayout = new BorderPane();
        cardLayout.setMinSize(400, 350);
        cardLayout.setMaxSize(700, 350);
        cardLayout.setCenter(contentBox);

        return cardLayout;
    }

    public static Node drawIngredient(String prompt, Collection<Ingredient> ingredients, Button submit) {
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 20));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        ingredients.forEach(ingredient -> {
            RadioButton radio = new RadioButton(
                    (ingredient.equals(Ingredient.HELPFUL_DUCK)) ? "HELPFUL DUCK \uD83D\uDC24"
                            : ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });

        submit.setOnAction(e -> {
            String choice = null;
            try {
                choice = ((RadioButton) toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                Game.startGame(scene, ob);
            }
            if (choice.equals("HELPFUL DUCK \uD83D\uDC24")) {
                System.out.println("test");
                choice = Ingredient.HELPFUL_DUCK.toString().toUpperCase();
            }
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ((ArrayList<Ingredient>) ingredients).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.drawFromPantry(ingredient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Draw Ingredient");
                    alert.setContentText(ingredient.toString().toUpperCase() + " drawn from the pantry.");
                    alert.show();
                    Game.startGame(scene, ob);
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

    public static Node passIngredient(String prompt, Collection<Ingredient> ingredients, Player recipient,
            Button submit) {
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 20));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        ingredients.forEach(ingredient -> {
            RadioButton radio = new RadioButton(ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });

        submit.setOnAction(e -> {

            String choice = null;
            try {
                choice = ((RadioButton) toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                Game.startGame(scene, ob);
            }
            if (choice.equals("HELPFUL DUCK \uD83D\uDC24")) {
                choice = Ingredient.HELPFUL_DUCK.toString().toUpperCase();
            }

            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ((ArrayList<Ingredient>) ingredients).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.passCard(ingredient, recipient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Pass Ingredient");
                    alert.setContentText(ingredient.toString().toUpperCase() + " passed to " + recipient);
                    alert.show();
                    Game.startGame(scene, ob);
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

    public static Node passCard(String prompt, Button submit) {
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

        submit.setOnAction(e -> {
            Player selectedPlayer = null;
            String choice = null;
            try {
                choice = ((RadioButton) toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                Game.startGame(scene, ob);
            }
            for (Player player : ob.getPlayers()) {
                if (player.toString().equals(choice)) {
                    selectedPlayer = player;
                }
            }
            assert selectedPlayer != null;
            optionBox.getChildren().add(passIngredient("Enter the ingredient to pass to " + selectedPlayer + ": ",
                    ob.getCurrentPlayer().getHand(), selectedPlayer, submit));
        });

        return optionBox;
    }

    public static Node bakeLayer(String prompt, Collection<Layer> layers, Button submit) {
        Label promptText = new Label(prompt);
        promptText.setFont(Font.font("Verdana", 23));

        ArrayList<RadioButton> options = new ArrayList<>();
        ToggleGroup toggle = new ToggleGroup();
        layers.forEach(ingredient -> {
            RadioButton radio = new RadioButton(ingredient.toString().toUpperCase());
            radio.setToggleGroup(toggle);
            options.add(radio);
        });

        submit.setOnAction(e -> {
            String choice = null;
            try {
                choice = ((RadioButton) toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                Game.startGame(scene, ob);
            }
            for (int i = 0; i < layers.size(); i++) {
                Layer ingredient = ((ArrayList<Layer>) layers).get(i);
                if (ingredient.toString().toUpperCase().equals(choice)) {
                    ob.bakeLayer(ingredient);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Bake layer");
                    alert.setContentText(ingredient.toString().toUpperCase() + " baked.");
                    alert.show();
                    Game.startGame(scene, ob);
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

    public static Node fulfillOrder(String prompt, Collection<CustomerOrder> customers, Button submit) {
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

        submit.setOnAction(e -> {
            String choice = null;
            try {
                choice = ((RadioButton) toggle.getSelectedToggle()).getText();
            } catch (NullPointerException ex) {
                Alert warn = new Alert(Alert.AlertType.WARNING);
                warn.setTitle("No options chosen");
                warn.setContentText("No option was chosen, please try again");
                warn.show();
                Game.startGame(scene, ob);
            }
            for (int i = 0; i < customers.size(); i++) {
                CustomerOrder customer = ((ArrayList<CustomerOrder>) customers).get(i);
                if (customer.toString().toUpperCase().equals(choice)) {
                    if (!customer.getGarnish().isEmpty()) {
                        ArrayList<Ingredient> copy = new ArrayList<>(ob.getCurrentPlayer().getHand());
                        ob.getCurrentPlayer().getHand().forEach(ingredient -> {
                            if (customer.getRecipe().contains(ingredient)) {
                                copy.remove(ingredient);
                            }
                        });

                        if (customer.canGarnish(copy)) {
                            Alert garnish = new Alert(Alert.AlertType.NONE, "Garnish", ButtonType.YES, ButtonType.NO);
                            garnish.showAndWait().ifPresent(event -> {
                                ButtonType result = garnish.getResult();
                                if (result == ButtonType.YES) {
                                    ob.fulfillOrder(customer, true);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Fulfill and Garnish order");
                                    alert.setContentText(
                                            customer.toString().toUpperCase() + " Fulfilled and Garnished!");
                                    alert.show();
                                } else {
                                    ob.fulfillOrder(customer, false);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Fulfill order");
                                    alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                                    alert.show();
                                }
                                Game.startGame(scene, ob);
                            });
                            break;
                        } else {
                            ob.fulfillOrder(customer, false);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Fulfill order");
                            alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                            alert.show();
                            Game.startGame(scene, ob);
                            break;
                        }
                    } else {
                        ob.fulfillOrder(customer, false);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Fulfill order");
                        alert.setContentText(customer.toString().toUpperCase() + " Fulfilled!");
                        alert.show();
                        Game.startGame(scene, ob);
                        break;
                    }

                }
            }
        });

        return optionBox;
    }
}
