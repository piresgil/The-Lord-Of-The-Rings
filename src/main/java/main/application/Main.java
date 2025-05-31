package main.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.application.utils.JsonUtils;

import java.io.IOException;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1100, 700);
        stage.setTitle("The Lord Of The Rings");
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        JsonUtils.criarJsonHerois();
        JsonUtils.criarJsonBestas();
    }

    public static void main(String[] args) {
        launch();
    }
}