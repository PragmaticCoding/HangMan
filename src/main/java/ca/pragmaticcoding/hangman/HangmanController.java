package ca.pragmaticcoding.hangman;

import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HangmanController {

    HangmanInteractor interactor;
    HangmanModel model;
    Builder<Region> viewBuilder;

    public HangmanController() {
        model = new HangmanModel();
        interactor = new HangmanInteractor(model);
        viewBuilder = new HangmanViewBuilder(model, interactor::processLetter, interactor::newWord);
        fetchWords();
    }

    private void fetchWords() {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() {
                interactor.fetchWords();
                return null;
            }
        };
        fetchTask.setOnSucceeded(evt -> interactor.newWord());
        Thread fetchThread = new Thread(fetchTask);
        fetchThread.start();
    }

    public Region getView() {
        return viewBuilder.build();
    }
}
