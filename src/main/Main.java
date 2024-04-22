import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;




public class Main extends Application {
    public static void main(String args[]){
        launch(args);
    }

    public void start(Stage primaryStage){

        VBox root = new VBox(10);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40, 20 ,20 , 20));

        Text title = new Text();
        title.setText("Magic Bakery");
        title.setFont(Font.font("Futura", 75));

        Button startButton = new Button("Start Game");
        Button loadButton = new Button("Load Game");

        startButton.setPadding(new Insets(10));
        loadButton.setPadding(new Insets(10));
        startButton.setOnAction(new StartGame());
        loadButton.setOnAction(new LoadGame());

        root.getChildren().addAll(title, startButton, loadButton);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Magic Bakery");
        primaryStage.isMaximized();
        primaryStage.show();
    }


}
