package com.cavacamisa.dto;

import com.cavacamisa.model.Card;

public class CardDto {
    private int rank;
    private String suit;
    private String displayName;
    private boolean winningCard;

    public CardDto() {}

    public CardDto(Card card) {
        this.rank = card.getRank();
        this.suit = card.getSuit().getDisplayName();
        this.displayName = card.getDisplayName();
        this.winningCard = card.isWinningCard();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isWinningCard() {
        return winningCard;
    }

    public void setWinningCard(boolean winningCard) {
        this.winningCard = winningCard;
    }
}
