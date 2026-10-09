package com.example.card24.model;

/**
 * Represents a standard playing card used in the Card 24 game.
 */
public class Card {

    /**
     * Enumeration of all supported card suits.
     */
    public enum Suit {
        SPADES,
        HEARTS,
        DIAMONDS,
        CLUBS
    }

    private final Suit suit;
    private final String rankLabel;
    private final int value;

    /**
     * Creates a new immutable playing card instance.
     *
     * @param suit the suit of the card
     * @param rankLabel the display rank label (A, 2-10, J, Q, K)
     * @param value the numeric game value of the card
     */
    public Card(Suit suit, String rankLabel, int value) {
        this.suit = suit;
        this.rankLabel = rankLabel;
        this.value = value;
    }

    /**
     * Returns the numeric value used by the game logic.
     *
     * @return card value where A=1, J=11, Q=12, K=13
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the suit of this card.
     *
     * @return the card suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Returns the human-readable rank label of this card.
     *
     * @return one of A, 2-10, J, Q, or K
     */
    public String getRankLabel() {
        return rankLabel;
    }

    /**
     * Returns the resource path for this card image.
     *
     * @return image path like /png/1_of_spades.png
     */
    public String getImagePath() {
        // Convert suit name to lowercase to match required file naming convention.
        String suitPart = suit.name().toLowerCase();
        String rankPart = rankPathForValue(value);
        return "/png/" + rankPart + "_of_" + suitPart + ".png";
    }

    /**
     * Converts numeric value to the image filename rank token.
     *
     * @param value card value from 1 to 13
     * @return rank token used in image filenames
     */
    private String rankPathForValue(int value) {
        return switch (value) {
            case 1 -> "ace";
            case 11 -> "jack";
            case 12 -> "queen";
            case 13 -> "king";
            default -> String.valueOf(value);
        };
    }
}
