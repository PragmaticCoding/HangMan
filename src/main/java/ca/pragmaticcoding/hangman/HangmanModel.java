package ca.pragmaticcoding.hangman;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class HangmanModel {

    private final ObservableList<String> pickedLetters = FXCollections.observableArrayList();
    private final ObservableList<String> word = FXCollections.observableArrayList();
    private final IntegerProperty wrongLetterCount = new SimpleIntegerProperty(0);
    private final BooleanProperty gameWon = new SimpleBooleanProperty(false);
    private final BooleanProperty gameLost = new SimpleBooleanProperty(false);


    public int getWrongLetterCount() {
        return wrongLetterCount.get();
    }

    public IntegerProperty wrongLetterCountProperty() {
        return wrongLetterCount;
    }

    public void setWrongLetterCount(int wrongLetterCount) {
        this.wrongLetterCount.set(wrongLetterCount);
    }

    public ObservableList<String> getPickedLetters() {
        return pickedLetters;
    }

    public void setPickedLetters(List<String> pickedLetters) {
        this.pickedLetters.setAll(pickedLetters);
    }

    public boolean isGameWon() {
        return gameWon.get();
    }

    public BooleanProperty gameWonProperty() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon.set(gameWon);
    }

    public boolean isGameLost() {
        return gameLost.get();
    }

    public BooleanProperty gameLostProperty() {
        return gameLost;
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost.set(gameLost);
    }

    public ObservableList<String> getWord() {
        return word;
    }

    public void setWord(List<String> letters) {
        this.word.setAll(letters);
    }
}
