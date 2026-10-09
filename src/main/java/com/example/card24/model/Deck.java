package com.example.card24.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a standard 52-card deck and provides random card selection.
 */
public class Deck {

    private final List<Card> cards;

    /**
     * Creates a full deck containing all 52 unique playing cards.
     */
    public Deck() {
        this.cards = buildFullDeck();
    }

    /**
     * Builds a standard deck with all ranks for all suits.
     *
     * @return immutable list containing 52 cards
     */
    private List<Card> buildFullDeck() {
        // Build cards suit-by-suit and rank-by-rank.
        List<Card> allCards = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 1; value <= 13; value++) {
                allCards.add(new Card(suit, rankLabelForValue(value), value));
            }
        }

        return List.copyOf(allCards);
    }

    /**
     * Maps numeric card values to display labels.
     *
     * @param value card value from 1 to 13
     * @return rank label string
     */
    private String rankLabelForValue(int value) {
        return switch (value) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(value);
        };
    }

    /**
     * Returns a shuffled list of 4 distinct random cards.
     *
     * @return list containing exactly four unique cards
     */
    public List<Card> pickFourDistinctCards() {
        // Shuffle a mutable copy so original deck order remains unchanged.
        List<Card> shuffled = new ArrayList<>(cards);
        Collections.shuffle(shuffled);
        return shuffled.stream().limit(4).collect(Collectors.toList());
    }

    /**
     * Returns an unmodifiable view of all cards in this deck.
     *
     * @return list of all 52 cards
     */
    public List<Card> getCards() {
        return cards;
    }
}
