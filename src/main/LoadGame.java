import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import bakery.MagicBakery;


import java.io.File;
import java.io.IOException;

public class LoadGame{

    public static void handle(Scene sc, Parent mainpage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(40, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 75));

        Label existingGameLabel = new Label("Existing game file path ");
        TextField existingGameFile = new TextField();
        existingGameFile.setMaxWidth(sc.getWidth() / 2);
        existingGameLabel.setLabelFor(existingGameFile);

        Button submit = new Button("Start");
        submit.setDefaultButton(true);
        submit.setPadding(new Insets(10));
        submit.setOnAction(e1->loadGame(existingGameFile.getText(), sc));

        Button cancel = new Button("Cancel");
        cancel.setPadding(new Insets(10));
        cancel.setOnAction(e1-> {
            ((Stage) sc.getWindow()).setTitle("Welcome");
            sc.setRoot(mainpage);
        });

        HBox form = new HBox(20, existingGameLabel, existingGameFile);
        form.setAlignment(Pos.CENTER);
        HBox buttons = new HBox(20, submit, cancel);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(title, form, buttons);
        sc.setRoot(root);
        ((Stage) sc.getWindow()).setTitle("Load existing game");

    }

    public static void loadGame(String existingGamePath, Scene scene){
        try {
            MagicBakery ob = MagicBakery.loadState(new File(existingGamePath));
            Game.startGame(scene, ob);
        }

        catch (ClassNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Invalid encoding in file! Cannot load the game");
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