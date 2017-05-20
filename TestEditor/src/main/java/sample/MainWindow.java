package sample;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import model.Hint;
import ui.HintBox;
import util.Resources;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class MainWindow extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Resources.MAIN_WINDOW));
        Parent root = loader.load();
        MainController mainController = loader.getController();
        mainController.setStage(primaryStage);
        primaryStage.setTitle("Test editor");
        Scene scene = new Scene(root, 1000, 600);
        //scene.getStylesheets().add(Resources.MAIN_STYLESHEET);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
