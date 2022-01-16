package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/** Main thread that starts a GUI instance */

public class CMain extends Application {

    /**
     * A method that uses parameter passed from main to open the GUI window
     *
     * @param stage Parameter passed from main. Location to show GUI window.
     * @throws IOException Is thrown when an error in passing information occurred.
     */

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CMain.class.getResource("GameGUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chinese checkers");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    /**
     * Method starts the GUI
     *
     * @param args Parameters passed while creating an instance of a program. Are empty.
     */

    public static void main(String[] args) {
        launch();
    }

}