import bakery.MagicBakery;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class StartGame implements EventHandler<ActionEvent>{

    @Override
    public void handle(ActionEvent e) {
        Scene scene = ((Button)e.getSource()).getScene();
        GridPane root = new GridPane();
        root.setPadding(new Insets(40, 20, 20, 20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(20);
        root.setHgap(10);

        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 75));

        Label ingredientlabel = new Label("Ingredients file path ");
        Label layerlabel = new Label("Layers file path ");
        Label customerlabel = new Label("Customers file path ");
        Label seedlabel = new Label("Seed (To shuffle the cards): ");
        TextField ingredientFile = new TextField();
        TextField layerFile = new TextField();
        TextField customerFile = new TextField();
        TextField seed = new TextField();
        ingredientFile.setMaxWidth(scene.getWidth()/2);
        layerFile.setMaxWidth(scene.getWidth()/2);
        customerFile.setMaxWidth(scene.getWidth()/2);
        seed.setMaxWidth(scene.getWidth()/2);
        ingredientlabel.setLabelFor(ingredientFile);
        layerlabel.setLabelFor(layerFile);
        customerlabel.setLabelFor(customerFile);
        seedlabel.setLabelFor(seed);
        Label players = new Label("Select the number of players");

        ToggleGroup group = new ToggleGroup();
        RadioButton player2 = new RadioButton();
        player2.setText("2 Players");
        player2.setToggleGroup(group);
        player2.setSelected(true);
        RadioButton player3 = new RadioButton();
        player3.setText("3 Players");
        player3.setToggleGroup(group);
        RadioButton player4 = new RadioButton();
        player4.setText("4 Players");
        player4.setToggleGroup(group);
        RadioButton player5 = new RadioButton();
        player5.setText("5 Players");
        player5.setToggleGroup(group);



        Button submit = new Button("Start");
        submit.setDefaultButton(true);
        submit.setPadding(new Insets(10));

        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setPadding(new Insets(10));
        cancel.setOnAction(e1-> new Main().start((Stage)scene.getWindow()));


        submit.setOnAction(e1->{
            int choice = Character.getNumericValue(((RadioButton)group.getSelectedToggle()).getText().charAt(0));
            ArrayList<TextField> playernames = new ArrayList<TextField>();
            ArrayList<Label> playerlabel = new ArrayList<Label>();
            for(int i=0; i<choice; i++){
                playerlabel.add(new Label("Player "+(i+1)+" "));
                playernames.add(new TextField("Player "+(i+1)));
            }
            playernames.forEach(field->field.setMaxWidth(scene.getWidth()/2));
            GridPane pn = new GridPane();
            pn.setPadding(new Insets(40, 20, 20, 20));
            pn.setAlignment(Pos.TOP_CENTER);
            pn.setVgap(20);
            pn.setHgap(10);

            pn.add(title, 0, 0, 2, 1);
            int i;
            for(i=0; i<choice; i++){
                pn.add(playerlabel.get(i), 0, i+1);
                pn.add(playernames.get(i), 1, i+1);
            }
            pn.add(submit, 0, i+1, 1, 1);
            pn.add(cancel, 1, i+1, 1, 1);
            submit.setOnAction(e2->{
                ArrayList<String> pnames = new ArrayList<String>();
                playernames.forEach(name->pnames.add(name.getText().trim()));
                if((new HashSet<String>(pnames)).size() != pnames.size()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Warning");
                    alert.setContentText("Imposter Detected! Please enter different names.");
                    alert.show();
                }
                newGame(pnames, ingredientFile.getText(), layerFile.getText(), seed.getText(), customerFile.getText(), scene);
            });

            scene.setRoot(pn);

        });



        root.add(title, 0, 0, 2, 1);
        root.add(ingredientlabel, 0,1);
        root.add(ingredientFile, 1,1);
        root.add(layerlabel, 0, 2);
        root.add(layerFile, 1, 2);
        root.add(customerlabel, 0, 3);
        root.add(customerFile, 1, 3);
        root.add(seedlabel, 0, 4);
        root.add(seed, 1, 4);
        root.add(new Separator(), 0, 5, 2, 1);
        root.add(players, 0, 6);
        root.add(player2, 0, 7);
        root.add(player3, 0, 8);
        root.add(player4, 0, 9);
        root.add(player5, 0, 10);
        root.add(submit, 0,11, 2, 1);
        root.add(cancel, 2,11, 2, 1);
        scene.setRoot(root);
        ((Stage)scene.getWindow()).setTitle("Start new game");

    }

    public void newGame(List<String> player_names, String ingredientfile, String layerfile, String seedline, String customerfile, Scene scene) {
        try{
            int seed = Integer.parseInt(seedline);
            MagicBakery ob = new MagicBakery(seed, ingredientfile.trim(), layerfile.trim());
            ob.startGame(player_names, customerfile);
            Game.startGame(scene, ob);
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Please enter a number for the seed");
            alert.show();
        }
        catch (FileNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("File not Found!");
            alert.show();
        }
        catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error occurred while trying to open the file. Try again");
            alert.show();
        }
    }

}
