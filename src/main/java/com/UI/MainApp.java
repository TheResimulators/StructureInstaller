package com.UI;

import com.CL.CommandLineApplication;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("scene.fxml"));
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
        stage.setTitle("JavaFX and Gradle");
        stage.setScene(scene);
        stage.show();
        System.out.println("Test");
    }

    public static void main(String[] args) {

        if (args.length > 0){
        if (Objects.equals(args[0], "--console"))
        {
            new CommandLineApplication();
        }
        }
        if (args.length == 0)
        {
            launch(args);
        }
    }

}
