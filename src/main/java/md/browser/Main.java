package md.browser;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load( ClassLoader.getSystemResourceAsStream("Scene.fxml") );

        //Controller controller = loader.getController();
        Scene scene = new Scene(root);

        //stage.getIcons().add(new Image("icon.png"));
        stage.setTitle("Local Machine HTML Browser");
        stage.setScene(scene);

        stage.getIcons().add(
                new Image(
                        Objects.requireNonNull(
                                ClassLoader.getSystemResourceAsStream("iconXFAB.png")
                        )
                )
        );


        stage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
