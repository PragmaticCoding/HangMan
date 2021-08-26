package ca.pragmaticcoding.hangman;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Builder;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HangmanViewBuilder implements Builder<Region> {

    private HangmanModel model;
    private final Consumer<String> letterConsumer;
    private Runnable restartHandler;
    private HBox wordBox = new HBox(10);

    HangmanViewBuilder(HangmanModel model, Consumer<String> letterConsumer, Runnable restartHandler) {
        this.model = model;
        this.letterConsumer = letterConsumer;
        this.restartHandler = restartHandler;
    }

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.setLeft(createHangedMan());
        results.setBottom(new VirtualKeyboard(letterConsumer, restartHandler));
        results.setCenter(createCentre());
        model.getWord().addListener((InvalidationListener) invalidation -> buildWord());
        results.setPadding(new Insets(8));
        return results;
    }

    public Node createHangedMan() {
        Image image = new Image("/images/hangman.png");
        ImageView imageView = new ImageView(image);
        double cellWidth = image.getWidth() / 7;
        List<Rectangle2D> clippingShapes = IntStream.range(0, 7)
                                                    .mapToObj(i -> new Rectangle2D(i * cellWidth, 0, cellWidth, image.getHeight()))
                                                    .collect(Collectors.toList());
        imageView.viewportProperty().bind(Bindings.valueAt(FXCollections.observableList(clippingShapes), model.wrongLetterCountProperty()));
        StackPane results = new StackPane(imageView);
        results.setPadding(new Insets(30));
        return results;
    }

    private Node createCentre() {
        StackPane winLosePane = new StackPane(createImageView("/images/win.png", model.gameWonProperty()),
                                              createImageView("/images/lose.png", model.gameLostProperty()));
        wordBox.setAlignment(Pos.BOTTOM_CENTER);
        wordBox.setMinHeight(70);
        wordBox.setPadding(new Insets(0, 0, 10, 0));
        return new VBox(10, winLosePane, wordBox);
    }

    private ImageView createImageView(String imagePath, ObservableBooleanValue visibilityProperty) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        imageView.visibleProperty().bind(visibilityProperty);
        return imageView;
    }

    public void buildWord() {
        wordBox.getChildren().setAll(model.getWord().stream().map(this::createLetterBox).collect(Collectors.toList()));
    }

    private Node createLetterBox(String letter) {
        VBox vBox = new VBox();
        Node letterText = bigText(letter);
        letterText.visibleProperty()
                  .bind(Bindings.createBooleanBinding(() -> model.getPickedLetters().contains(letter) || model.isGameLost(),
                                                      model.getPickedLetters(),
                                                      model.gameLostProperty()));
        vBox.getChildren().addAll(letterText, new Line(0, 10, 40, 10));
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        return vBox;
    }

    private Node bigText(String letter) {
        Text results = new Text(letter);
        results.setStyle("-fx-font-size: 45; -fx-font-weight: bold");
        return results;
    }
}
