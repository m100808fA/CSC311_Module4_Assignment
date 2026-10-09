package com.example.card24.logic;

import com.example.card24.model.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validates whether a user expression is a correct Card 24 solution.
 */
public final class Game24Validator {

    /**
     * Result categories for expression validation outcomes.
     */
    public enum ValidationResult {
        VALID,
        WRONG_NUMBERS,
        NOT_24,
        INVALID_SYNTAX
    }

    /**
     * Structured output for validation status and message.
     *
     * @param result validation result enum
     * @param message human-readable explanation
     */
    public record ValidationOutcome(ValidationResult result, String message) {
    }

    private static final double TARGET = 24.0;
    private static final double EPSILON = 1e-6;

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * Validates expression syntax, card number usage, and value equals 24.
     *
     * @param expression user-entered arithmetic expression
     * @param cards exactly four game cards
     * @return validation outcome with status and message
     */
    public ValidationOutcome validate(String expression, List<Card> cards) {
        ExpressionEvaluator.EvaluationResult evaluation;

        try {
            evaluation = evaluator.evaluate(expression);
        } catch (InvalidExpressionException ex) {
            return new ValidationOutcome(ValidationResult.INVALID_SYNTAX, "Invalid expression: " + ex.getMessage());
        }

        if (!hasMatchingMultiset(evaluation.integerLiterals(), cards)) {
            return new ValidationOutcome(
                    ValidationResult.WRONG_NUMBERS,
                    "Expression must use exactly the four card values once each."
            );
        }

        if (Math.abs(evaluation.value() - TARGET) > EPSILON) {
            return new ValidationOutcome(
                    ValidationResult.NOT_24,
                    "Expression value is " + evaluation.value() + ", not 24."
            );
        }

        return new ValidationOutcome(ValidationResult.VALID, "Correct! The expression equals 24.");
    }

    /**
     * Checks whether expression literals and card values match exactly as multisets.
     *
     * @param literals numbers extracted from expression
     * @param cards displayed cards
     * @return true if both collections have identical value counts
     */
    private boolean hasMatchingMultiset(List<Integer> literals, List<Card> cards) {
        if (literals.size() != cards.size()) {
            return false;
        }

        Map<Integer, Integer> expectedCounts = new HashMap<>();
        for (Card card : cards) {
            expectedCounts.merge(card.getValue(), 1, Integer::sum);
        }

        Map<Integer, Integer> actualCounts = new HashMap<>();
        for (Integer literal : literals) {
            actualCounts.merge(literal, 1, Integer::sum);
        }

        return expectedCounts.equals(actualCounts);
    }
}
