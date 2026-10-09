package com.example.card24.logic;

import com.example.card24.logic.ExpressionTokenizer.Token;
import com.example.card24.logic.ExpressionTokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses and evaluates arithmetic expressions using recursive-descent parsing.
 */
public final class ExpressionEvaluator {

    /**
     * Parsed evaluation output containing numeric value and integer literals used.
     *
     * @param value computed expression value
     * @param integerLiterals integer literals encountered in source order
     */
    public record EvaluationResult(double value, List<Integer> integerLiterals) {
    }

    private static final double DIVISION_EPSILON = 1e-12;

    private final ExpressionTokenizer tokenizer = new ExpressionTokenizer();

    private List<Token> tokens;
    private int position;
    private final List<Integer> usedLiterals = new ArrayList<>();

    /**
     * Evaluates a textual arithmetic expression.
     *
     * @param expression expression text
     * @return computed result and list of integer literals used
     * @throws InvalidExpressionException if syntax is invalid or division by zero occurs
     */
    public EvaluationResult evaluate(String expression) throws InvalidExpressionException {
        tokens = tokenizer.tokenize(expression);
        position = 0;
        usedLiterals.clear();

        if (tokens.isEmpty()) {
            throw new InvalidExpressionException("Expression is empty.");
        }

        double value = parseExpression();

        if (!isAtEnd()) {
            throw new InvalidExpressionException("Unexpected token: '" + peek().text() + "'.");
        }

        return new EvaluationResult(value, List.copyOf(usedLiterals));
    }

    /**
     * Parses addition/subtraction level grammar.
     *
     * @return evaluated value for the additive expression
     * @throws InvalidExpressionException if parsing fails
     */
    private double parseExpression() throws InvalidExpressionException {
        double value = parseTerm();

        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token operator = previous();
            double right = parseTerm();

            if (operator.type() == TokenType.PLUS) {
                value += right;
            } else {
                value -= right;
            }
        }

        return value;
    }

    /**
     * Parses multiplication/division level grammar.
     *
     * @return evaluated value for the multiplicative expression
     * @throws InvalidExpressionException if parsing fails or division by zero occurs
     */
    private double parseTerm() throws InvalidExpressionException {
        double value = parseFactor();

        while (match(TokenType.MULTIPLY) || match(TokenType.DIVIDE)) {
            Token operator = previous();
            double right = parseFactor();

            if (operator.type() == TokenType.MULTIPLY) {
                value *= right;
            } else {
                if (Math.abs(right) < DIVISION_EPSILON) {
                    throw new InvalidExpressionException("Division by zero is not allowed.");
                }
                value /= right;
            }
        }

        return value;
    }

    /**
     * Parses factors: NUMBER, parenthesized expression, or unary minus.
     *
     * @return evaluated factor value
     * @throws InvalidExpressionException if factor syntax is invalid
     */
    private double parseFactor() throws InvalidExpressionException {
        if (match(TokenType.MINUS)) {
            return -parseFactor();
        }

        if (match(TokenType.NUMBER)) {
            int literal = previous().numberValue();
            usedLiterals.add(literal);
            return literal;
        }

        if (match(TokenType.LEFT_PAREN)) {
            double nested = parseExpression();
            consume(TokenType.RIGHT_PAREN, "Missing closing parenthesis.");
            return nested;
        }

        throw new InvalidExpressionException("Expected a number, unary minus, or '('.");
    }

    /**
     * Ensures the next token matches the required type.
     *
     * @param type token type that must appear next
     * @param message error message when token is absent
     * @throws InvalidExpressionException if token is missing
     */
    private void consume(TokenType type, String message) throws InvalidExpressionException {
        if (match(type)) {
            return;
        }

        throw new InvalidExpressionException(message);
    }

    /**
     * Tries to match and consume the next token type.
     *
     * @param type token type to match
     * @return true if matched and consumed, otherwise false
     */
    private boolean match(TokenType type) {
        if (check(type)) {
            position++;
            return true;
        }
        return false;
    }

    /**
     * Checks whether the next token has the given type.
     *
     * @param type token type to inspect
     * @return true if next token type matches
     */
    private boolean check(TokenType type) {
        return !isAtEnd() && peek().type() == type;
    }

    /**
     * Returns the current token without consuming it.
     *
     * @return current token
     */
    private Token peek() {
        return tokens.get(position);
    }

    /**
     * Returns the previously consumed token.
     *
     * @return previous token
     */
    private Token previous() {
        return tokens.get(position - 1);
    }

    /**
     * Checks whether all tokens have been consumed.
     *
     * @return true when parser is at end of token stream
     */
    private boolean isAtEnd() {
        return position >= tokens.size();
    }
}
