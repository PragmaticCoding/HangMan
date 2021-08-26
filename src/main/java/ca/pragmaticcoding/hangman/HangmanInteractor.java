package ca.pragmaticcoding.hangman;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
import kong.unirest.GetRequest;
import kong.unirest.HttpRequest;
import kong.unirest.Unirest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HangmanInteractor {


    private final HangmanModel model;
    private List<String> words = List.of("HANGMAN");

    public HangmanInteractor(HangmanModel model) {
        this.model = model;
        this.model.gameWonProperty()
                  .bind(Bindings.createBooleanBinding(() -> hasGameBeenWon() && !model.isGameLost(),
                                                      model.getPickedLetters(),
                                                      model.gameLostProperty(),
                                                      model.getWord()));
        this.model.wrongLetterCountProperty().bind(Bindings.createIntegerBinding(this::countWrongLetters, this.model.getPickedLetters()));
        this.model.gameLostProperty().bind(Bindings.createBooleanBinding(() -> model.wrongLetterCountProperty().get() > 5, model.wrongLetterCountProperty()));
    }

    private int countWrongLetters() {
        return Math.min(Math.toIntExact(model.getPickedLetters().stream().filter(letter -> !model.getWord().contains(letter)).count()),6);
    }

    private boolean hasGameBeenWon() {
        return (model.getPickedLetters().containsAll(model.getWord()));
    }


    void processLetter(String letter) {
        if (!model.isGameWon() && !model.isGameLost()) {
            model.getPickedLetters().add(letter);
        }
    }

    void newWord() {
        model.setWord(wordAsList(words.remove(0).toUpperCase()));
        model.getPickedLetters().clear();
    }

    private List<String> wordAsList(String word) {
        return Arrays.stream(word.split("")).filter(letter -> !letter.isEmpty()).collect(Collectors.toList());
    }

    void fetchWords() {
        try {
            HttpRequest<GetRequest> request = Unirest.get("https://random-word-api.herokuapp.com/word").queryString("number", "100");
            Type collectionType = new TypeToken<Collection<String>>() {}.getType();
            ArrayList<String> wordArray = new Gson().fromJson(request.asString().getBody(), collectionType);
            words = wordArray.stream().filter(word -> word.length() < 10).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
