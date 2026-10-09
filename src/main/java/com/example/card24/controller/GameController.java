package com.example.card24.controller;

import com.example.card24.logic.Game24Validator;
import com.example.card24.logic.Game24Validator.ValidationOutcome;
import com.example.card24.logic.Game24Validator.ValidationResult;
import com.example.card24.logic.SolutionFinder;
import com.example.card24.model.Card;
import com.example.card24.model.Deck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller responsible for all Card 24 user-interface interactions.
 *
 * This class coordinates the view controls with game logic classes while keeping
 * arithmetic parsing, validation, and solving in the logic package.
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

    @FXML
    private Button verifyButton;

    private final Deck deck = new Deck();
    private final Game24Validator validator = new Game24Validator();
    private final SolutionFinder solutionFinder = new SolutionFinder();

    private List<Card> currentCards;

    /**
     * Initializes UI behavior and starts the first round.
     */
    @FXML
    public void initialize() {
        // Disable verify while no expression has been entered.
        verifyButton.disableProperty().bind(expressionField.textProperty().isEmpty());

        // Pressing Enter in the expression field triggers verification.
        expressionField.setOnAction(this::onVerify);

        // Load the initial random card set.
        startNewRound();
    }

    /**
     * Handles clicking the "Find a Solution" button.
     *
     * @param event action event from the button
     */
    @FXML
    public void onFindSolution(ActionEvent event) {
        var maybeSolution = solutionFinder.findSolution(currentCards);

        if (maybeSolution.isPresent()) {
            solutionField.setText(maybeSolution.get());
        } else {
            solutionField.setText("No solution");
            showAlert(
                    Alert.AlertType.INFORMATION,
                    "No Solution",
                    "No solution exists for this set. Press Refresh for a new round."
            );
        }
    }

    /**
     * Handles clicking the "Refresh" button.
     *
     * @param event action event from the button
     */
    @FXML
    public void onRefresh(ActionEvent event) {
        startNewRound();
    }

    /**
     * Handles clicking the "Verify" button.
     *
     * @param event action event from the button or Enter key action
     */
    @FXML
    public void onVerify(ActionEvent event) {
        String expression = expressionField.getText() == null ? "" : expressionField.getText().trim();

        if (expression.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Expression", "Please enter an expression before verifying.");
            return;
        }

        ValidationOutcome outcome = validator.validate(expression, currentCards);

        if (outcome.result() == ValidationResult.VALID) {
            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION,
                    "Correct! Your expression equals 24.\n\nPress OK to start a new round.",
                    ButtonType.OK,
                    ButtonType.CANCEL
            );
            alert.setTitle("Valid Expression");
            alert.setHeaderText("Great job!");
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    startNewRound();
                }
            });
            return;
        }

        if (outcome.result() == ValidationResult.WRONG_NUMBERS) {
            showAlert(
                    Alert.AlertType.WARNING,
                    "Wrong Numbers",
                    outcome.message() + "\nCard values: " + cardValuesAsText(currentCards)
            );
            return;
        }

        if (outcome.result() == ValidationResult.NOT_24) {
            showAlert(Alert.AlertType.WARNING, "Not 24", outcome.message());
            return;
        }

        showAlert(Alert.AlertType.ERROR, "Invalid Syntax", outcome.message());
    }

    /**
     * Starts a new round by drawing cards, updating images, and clearing fields.
     */
    private void startNewRound() {
        currentCards = deck.pickFourDistinctCards();
        renderCards(currentCards);
        expressionField.clear();
        solutionField.clear();
    }

    /**
     * Renders four cards into the four image controls.
     *
     * @param cards the current hand to show
     */
    private void renderCards(List<Card> cards) {
        setCardImage(card1, cards.get(0));
        setCardImage(card2, cards.get(1));
        setCardImage(card3, cards.get(2));
        setCardImage(card4, cards.get(3));
    }

    /**
     * Loads a card image into an image view if the image resource exists.
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

    /**
     * Produces a comma-separated text list of current card numeric values.
     *
     * @param cards current cards
     * @return values such as "6, 12, 6, 6"
     */
    private String cardValuesAsText(List<Card> cards) {
        return cards.stream()
                .map(Card::getValue)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

    /**
     * Shows a simple alert dialog with title and message.
     *
     * @param type JavaFX alert type
     * @param title dialog title
     * @param message dialog body text
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
