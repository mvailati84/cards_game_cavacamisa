package com.cavacamisa.dto;

import com.cavacamisa.model.Player;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerDto {
    private String id;
    private String name;
    private List<CardDto> deck;
    private int deckSize;
    private int capturedCardsCount;
    private boolean hasCards;

    public PlayerDto() {}

    public PlayerDto(Player player) {
        this.id = player.getId();
        this.name = player.getName();
        this.deck = player.getDeck().stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
        this.deckSize = player.getDeckSize();
        this.hasCards = player.hasCards();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CardDto> getDeck() {
        return deck;
    }

    public void setDeck(List<CardDto> deck) {
        this.deck = deck;
    }

    public int getDeckSize() {
        return deckSize;
    }

    public void setDeckSize(int deckSize) {
        this.deckSize = deckSize;
    }

    public int getCapturedCardsCount() {
        return capturedCardsCount;
    }

    public void setCapturedCardsCount(int capturedCardsCount) {
        this.capturedCardsCount = capturedCardsCount;
    }

    public boolean isHasCards() {
        return hasCards;
    }

    public void setHasCards(boolean hasCards) {
        this.hasCards = hasCards;
    }
}
