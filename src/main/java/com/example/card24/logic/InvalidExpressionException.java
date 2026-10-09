package com.example.card24.logic;

/**
 * Signals that a user-provided arithmetic expression is syntactically invalid.
 */
public class InvalidExpressionException extends Exception {

    /**
     * Creates a new exception with an explanatory message.
     *
     * @param message human-readable error details
     */
    public InvalidExpressionException(String message) {
        super(message);
    }
}
