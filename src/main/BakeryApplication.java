import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import ui.LoadGame;
import ui.StartGame;


public class BakeryApplication extends Application {
    /*Recommended Screen Resolution: 2560 x 1440*/
    public static void main(String[] args){
        launch(args);
    }

    public void start(Stage primaryStage){

        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 75));

        Button startButton = new Button("Start Game");
        Button loadButton = new Button("Load Game");

        startButton.setPadding(new Insets(10));
        loadButton.setPadding(new Insets(10));


        root.getChildren().addAll(title, startButton, loadButton);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        startButton.setOnAction(e-> StartGame.handle(scene, root));
        loadButton.setOnAction(e-> LoadGame.handle(scene, root));
        primaryStage.setTitle("Welcome");
        primaryStage.setMinHeight(1440);
        primaryStage.setMinWidth(2560);
        primaryStage.show();
    }


}
