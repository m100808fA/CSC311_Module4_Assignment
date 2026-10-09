package com.example.card24.controller;

import com.example.card24.model.Card;
import com.example.card24.model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * Controller for the Card 24 game UI.
 *
 * This controller currently provides only the Part 1 skeleton behavior and
 * placeholders for button actions.
 */
public class GameController {

    @FXML
    private TextField solutionField;

    @FXML
    private TextField expressionField;

    @FXML
    private ImageView card1;

    @FXML
    private ImageView card2;

    @FXML
    private ImageView card3;

    @FXML
    private ImageView card4;

    private final Deck deck = new Deck();

    /**
     * Initializes the controller after FXML loading.
     */
    @FXML
    public void initialize() {
        // Show an initial random hand when the UI starts.
        refreshDisplayedCards();
    }

    /**
     * Handles clicking the "Find a Solution" button.
     *
     * @param event action event from the button
     */
    @FXML
    public void onFindSolution(ActionEvent event) {
        // Placeholder to be implemented in a later part.
        solutionField.setText("Part 2: solution finder goes here");
    }

    /**
     * Handles clicking the "Refresh" button.
     *
     * @param event action event from the button
     */
    @FXML
    public void onRefresh(ActionEvent event) {
        // Clear fields and load a new random set of cards.
        expressionField.clear();
        solutionField.clear();
        refreshDisplayedCards();
    }

    /**
     * Handles clicking the "Verify" button.
     *
     * @param event action event from the button
     */
    @FXML
    public void onVerify(ActionEvent event) {
        // Placeholder to be implemented in a later part.
        solutionField.setText("Part 2: verifier goes here");
    }

    /**
     * Selects and displays four random distinct cards.
     */
    private void refreshDisplayedCards() {
        List<Card> hand = deck.pickFourDistinctCards();
        setCardImage(card1, hand.get(0));
        setCardImage(card2, hand.get(1));
        setCardImage(card3, hand.get(2));
        setCardImage(card4, hand.get(3));
    }

    /**
     * Loads a card image into an image view if the resource exists.
     *
     * @param target image view to update
     * @param card card whose image should be displayed
     */
    private void setCardImage(ImageView target, Card card) {
        var imageStream = getClass().getResourceAsStream(card.getImagePath());

        if (imageStream != null) {
            target.setImage(new Image(imageStream));
        } else {
            target.setImage(null);
        }
    }
}
