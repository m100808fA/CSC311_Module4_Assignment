package com.example.card24.logic;

import com.example.card24.logic.Game24Validator.ValidationOutcome;
import com.example.card24.logic.Game24Validator.ValidationResult;
import com.example.card24.model.Card;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for Card 24 logic components.
 */
class Game24LogicTest {

    /**
     * Verifies that a known valid expression with matching card values passes.
     */
    @Test
    void validExpressionWithMatchingCards() {
        Game24Validator validator = new Game24Validator();
        List<Card> cards = List.of(
                new Card(Card.Suit.HEARTS, "6", 6),
                new Card(Card.Suit.SPADES, "Q", 12),
                new Card(Card.Suit.CLUBS, "6", 6),
                new Card(Card.Suit.DIAMONDS, "6", 6)
        );

        ValidationOutcome result = validator.validate("(6+6)*12/6", cards);

        assertEquals(ValidationResult.VALID, result.result());
    }

    /**
     * Verifies that ambiguous whitespace between numbers triggers invalid syntax.
     */
    @Test
    void ambiguousWhitespaceBetweenNumbersIsInvalidSyntax() {
        Game24Validator validator = new Game24Validator();
        List<Card> cards = List.of(
                new Card(Card.Suit.HEARTS, "6", 6),
                new Card(Card.Suit.SPADES, "Q", 12),
                new Card(Card.Suit.CLUBS, "6", 6),
                new Card(Card.Suit.DIAMONDS, "6", 6)
        );

        ValidationOutcome result = validator.validate("(6+6)*1 2/6", cards);

        assertEquals(ValidationResult.INVALID_SYNTAX, result.result());
    }

    /**
     * Verifies that a mathematically-correct 24 expression with wrong numbers is rejected.
     */
    @Test
    void correctValueButWrongNumbersReturnsWrongNumbers() {
        Game24Validator validator = new Game24Validator();
        List<Card> cards = List.of(
                new Card(Card.Suit.HEARTS, "6", 6),
                new Card(Card.Suit.SPADES, "Q", 12),
                new Card(Card.Suit.CLUBS, "6", 6),
                new Card(Card.Suit.DIAMONDS, "6", 6)
        );

        ValidationOutcome result = validator.validate("(8+8)+4+4", cards);

        assertEquals(ValidationResult.WRONG_NUMBERS, result.result());
    }

    /**
     * Verifies that a four-ace hand with value 1 has no generated solution.
     */
    @Test
    void allOnesHasNoSolution() {
        SolutionFinder finder = new SolutionFinder();
        List<Card> cards = List.of(
                new Card(Card.Suit.HEARTS, "A", 1),
                new Card(Card.Suit.SPADES, "A", 1),
                new Card(Card.Suit.CLUBS, "A", 1),
                new Card(Card.Suit.DIAMONDS, "A", 1)
        );

        assertTrue(finder.findSolution(cards).isEmpty());
    }

    /**
     * Verifies division-by-zero is reported safely by expression evaluation.
     */
    @Test
    void divisionByZeroIsHandledSafely() {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        InvalidExpressionException ex = assertThrows(
                InvalidExpressionException.class,
                () -> evaluator.evaluate("6/(3-3)")
        );

        assertTrue(ex.getMessage().toLowerCase().contains("division by zero"));
    }
}
