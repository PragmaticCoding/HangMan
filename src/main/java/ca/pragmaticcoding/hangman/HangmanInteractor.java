package ca.pragmaticcoding.hangman;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.Bindings;
import kong.unirest.HttpRequest;
import kong.unirest.Unirest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HangmanInteractor {


    private HangmanModel model;
    private List<String> words = List.of("HANGMAN");

    public HangmanInteractor(HangmanModel model) {
        this.model = model;
        this.model.gameLostProperty().bind(model.wrongLetterCountProperty().greaterThan(5));
        this.model.gameWonProperty()
                  .bind(Bindings.createBooleanBinding(() -> hasGameBeenWon() && !model.isGameLost(),
                                                      model.getPickedLetters(),
                                                      model.gameLostProperty(),
                                                      model.getWord()));
        this.model.wrongLetterCountProperty().bind(Bindings.createIntegerBinding(this::countWrongLetters, this.model.getPickedLetters()));
    }

    private int countWrongLetters() {
        return Math.toIntExact(model.getPickedLetters().stream().filter(letter -> !model.getWord().contains(letter)).count());
    }

    private boolean hasGameBeenWon() {
        return (model.getWord().stream().allMatch(model.getPickedLetters()::contains));
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
        return Arrays.asList(word.split("")).stream().filter(letter -> !letter.isEmpty()).collect(Collectors.toList());
    }

    void fetchWords() {
        try {
            HttpRequest request = Unirest.get("https://random-word-api.herokuapp.com/word").queryString("number", "100");
            Type collectionType = new TypeToken<Collection<String>>() {}.getType();
            ArrayList<String> wordArray = new Gson().fromJson(request.asString().getBody().toString(), collectionType);
            words = wordArray.stream().filter(word -> word.length() < 10).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
