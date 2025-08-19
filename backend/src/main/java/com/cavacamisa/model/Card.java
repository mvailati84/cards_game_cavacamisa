package com.cavacamisa.model;

import java.util.Objects;

public class Card {
    private final int rank; // 1-10 (Asso=1, Due=2, Tre=3, ..., Re=10)
    private final Suit suit;

    public Card(int rank, Suit suit) {
        if (rank < 1 || rank > 10) {
            throw new IllegalArgumentException("Rank must be between 1 and 10");
        }
        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isWinningCard() {
        return rank == 1 || rank == 2 || rank == 3; // Asso, Due, Tre
    }

    public int getCardsToPlay() {
        if (!isWinningCard()) {
            return 0;
        }
        return rank; // Asso=1, Due=2, Tre=3
    }

    public String getDisplayName() {
        String rankName;
        switch (rank) {
            case 1: rankName = "Asso"; break;
            case 2: rankName = "Due"; break;
            case 3: rankName = "Tre"; break;
            case 4: rankName = "Quattro"; break;
            case 5: rankName = "Cinque"; break;
            case 6: rankName = "Sei"; break;
            case 7: rankName = "Sette"; break;
            case 8: rankName = "Fante"; break;
            case 9: rankName = "Cavallo"; break;
            case 10: rankName = "Re"; break;
            default: rankName = String.valueOf(rank);
        }
        return rankName + " di " + suit.getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
