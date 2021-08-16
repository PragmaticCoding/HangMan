package ca.pragmaticcoding.hangman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HangManApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new HangmanController().getView());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
