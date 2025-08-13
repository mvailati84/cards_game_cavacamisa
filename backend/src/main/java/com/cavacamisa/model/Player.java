package com.cavacamisa.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String id;
    private final String name;
    private final List<Card> deck; // Cards in hand

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.deck = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getDeck() {
        return new ArrayList<>(deck);
    }


    public int getDeckSize() {
        return deck.size();
    }

    public boolean hasCards() {
        return !deck.isEmpty();
    }

    public Card drawCard() {
        if (deck.isEmpty()) {
            return null;
        }
        return deck.remove(0);
    }

    public void addCardsToDeck(List<Card> cards) {
        deck.addAll(cards);
    }

    public void captureCards(List<Card> cards) {
        // Add captured cards to the bottom of the deck
        deck.addAll(cards);
    }

    public void addCardToDeck(Card card) {
        deck.add(card);
    }

    public boolean isWinner() {
        // Player wins when they have all 40 cards (deck + captured cards)
        return getDeckSize() == 40;
    }

    public boolean hasLost() {
        // Player loses when they have no cards to draw
        return deck.isEmpty();
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", deckSize=" + deck.size() +
                '}';
    }
}
