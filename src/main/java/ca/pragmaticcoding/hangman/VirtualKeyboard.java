package ca.pragmaticcoding.hangman;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class VirtualKeyboard extends VBox {
    private static final List<String> row1Keys = List.of("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P");
    private static final List<String> row2Keys = List.of("A", "S", "D", "F", "G", "H", "J", "K", "L");
    private static final String restart = "Restart";
    private static final List<String> row3Keys = List.of("Z", "X", "C", "V", "B", "N", "M");
    private final Consumer<String> keystrokeConsumer;
    private Runnable restartHandler;
    private List<Button> allButtons = new ArrayList<>();


    public VirtualKeyboard(Consumer<String> keystrokeConsumer, Runnable restartHandler) {
        this.keystrokeConsumer = keystrokeConsumer;
        this.restartHandler = restartHandler;
        setSpacing(4);
        getChildren().addAll(createRow(row1Keys, 0d, false), createRow(row2Keys, 20d, false), createRow(row3Keys, 40d, true));
    }

    private HBox createRow(List<String> letters, Double leftPadding, boolean includeRestart) {
        HBox hBox = new HBox(6);
        hBox.getChildren().addAll(letters.stream().map(this::buttonSetup).collect(Collectors.toList()));
        if (includeRestart) {
            hBox.getChildren().add(createRestartKey());
        }
        hBox.setPadding(new Insets(0, 0, 0, leftPadding));
        return hBox;
    }

    private Node buttonSetup(String letter) {
        Button button = createButton(letter);
        button.setOnMouseClicked(mouseEvent -> {
            keystrokeConsumer.accept(letter);
            button.setDisable(true);
        });
        allButtons.add(button);
        return button;
    }

    private Button createButton(String letter) {
        Button button = new Button(letter);
        button.setMinSize(60.0, 60.0);
        button.setStyle("-fx-font-size: 22; -fx-font-weight: bold;");
        return button;
    }
    
    private Node createRestartKey() {
        Button button = createButton("Restart");
        button.setOnAction(evt -> {
            allButtons.forEach(eachButton -> eachButton.setDisable(false));
            restartHandler.run();
        });
        return button;
    }
}

