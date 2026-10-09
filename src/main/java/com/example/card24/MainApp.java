package com.example.card24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX application entry point for the Card 24 game.
 */
public class MainApp extends Application {

    /**
     * Starts the JavaFX application and displays the main game window.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the game view layout from FXML resources.
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/card24/game-view.fxml"));
        Scene scene = new Scene(loader.load(), 760, 420);

        // Apply the external stylesheet for visual styling.
        scene.getStylesheets().add(MainApp.class.getResource("/com/example/card24/style.css").toExternalForm());

        // Configure and show the primary game window.
        stage.setTitle("Card 24 Game");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX runtime.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
