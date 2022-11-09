package com.example.soutenance2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class musicApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(musicApplication.class.getResource("mainIHM.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 680, 720);
        stage.setTitle("Scrapping");
        stage.getIcons().add(new Image("C:\\Users\\greta2022\\Pictures\\vinyl.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}