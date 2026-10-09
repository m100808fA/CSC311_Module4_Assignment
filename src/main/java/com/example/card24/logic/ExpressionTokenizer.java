package com.example.card24.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts an arithmetic expression string into a sequence of lexical tokens.
 */
public final class ExpressionTokenizer {

    /**
     * Token type categories recognized by the tokenizer.
     */
    public enum TokenType {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        LEFT_PAREN,
        RIGHT_PAREN
    }

    /**
     * Immutable lexical token value object.
     *
     * @param type token category
     * @param text source text of token
     * @param numberValue parsed integer value for NUMBER tokens, otherwise null
     */
    public record Token(TokenType type, String text, Integer numberValue) {
    }

    /**
     * Tokenizes an input expression into numbers, operators, and parentheses.
     *
     * @param expression user expression text
     * @return ordered list of tokens
     * @throws InvalidExpressionException if illegal characters are present or
     *                                  if ambiguous whitespace-separated numbers are found
     */
    public List<Token> tokenize(String expression) throws InvalidExpressionException {
        List<Token> tokens = new ArrayList<>();

        int i = 0;
        Token previousToken = null;

        while (i < expression.length()) {
            char current = expression.charAt(i);

            if (Character.isWhitespace(current)) {
                i++;
                continue;
            }

            if (Character.isDigit(current)) {
                int start = i;

                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    i++;
                }

                String numberText = expression.substring(start, i);
                int value = Integer.parseInt(numberText);
                Token currentToken = new Token(TokenType.NUMBER, numberText, value);

                if (previousToken != null && previousToken.type() == TokenType.NUMBER) {
                    String between = expression.substring(findTokenEndIndex(expression, previousToken, start), start);
                    if (between.chars().allMatch(Character::isWhitespace)) {
                        throw new InvalidExpressionException("Ambiguous expression: numbers cannot be separated only by whitespace.");
                    }
                }

                tokens.add(currentToken);
                previousToken = currentToken;
                continue;
            }

            Token opToken = switch (current) {
                case '+' -> new Token(TokenType.PLUS, "+", null);
                case '-' -> new Token(TokenType.MINUS, "-", null);
                case '*' -> new Token(TokenType.MULTIPLY, "*", null);
                case '/' -> new Token(TokenType.DIVIDE, "/", null);
                case '(' -> new Token(TokenType.LEFT_PAREN, "(", null);
                case ')' -> new Token(TokenType.RIGHT_PAREN, ")", null);
                default -> null;
            };

            if (opToken == null) {
                throw new InvalidExpressionException("Illegal character in expression: '" + current + "'.");
            }

            tokens.add(opToken);
            previousToken = opToken;
            i++;
        }

        return tokens;
    }

    /**
     * Finds an approximate prior token end index to validate intervening whitespace.
     *
     * @param expression source expression
     * @param previousToken previous token
     * @param currentStart start index of current number token
     * @return index from which to inspect intervening text
     */
    private int findTokenEndIndex(String expression, Token previousToken, int currentStart) {
        int search = currentStart - 1;
        while (search >= 0 && Character.isWhitespace(expression.charAt(search))) {
            search--;
        }

        if (search < 0) {
            return 0;
        }

        if (previousToken.type() == TokenType.NUMBER) {
            while (search >= 0 && Character.isDigit(expression.charAt(search))) {
                search--;
            }
            return search + 1 + previousToken.text().length();
        }

        return search + 1;
    }
}
