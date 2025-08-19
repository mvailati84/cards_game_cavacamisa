package com.cavacamisa.dto;

import com.cavacamisa.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PlayerDtoTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("player1", "Test Player");
    }

    @Test
    @DisplayName("Should create PlayerDto with correct basic properties")
    void shouldCreatePlayerDtoWithCorrectBasicProperties() {
        PlayerDto dto = new PlayerDto(player);
        
        assertEquals("player1", dto.getId());
        assertEquals("Test Player", dto.getName());
        assertEquals(0, dto.getDeckSize());
        assertTrue(dto.getDeck().isEmpty());
    }

    @Test
    @DisplayName("Should create PlayerDto with cards in deck")
    void shouldCreatePlayerDtoWithCardsInDeck() {
        player.addCardToDeck(new Card(1, Suit.COPPE));
        player.addCardToDeck(new Card(2, Suit.ORI));
        
        PlayerDto dto = new PlayerDto(player);
        
        assertEquals(2, dto.getDeckSize());
        assertEquals(2, dto.getDeck().size());
        assertEquals(1, dto.getDeck().get(0).getRank());
        assertEquals("Coppe", dto.getDeck().get(0).getSuit());
        assertEquals(2, dto.getDeck().get(1).getRank());
        assertEquals("Ori", dto.getDeck().get(1).getSuit());
    }

    @Test
    @DisplayName("Should create empty PlayerDto with default constructor")
    void shouldCreateEmptyPlayerDtoWithDefaultConstructor() {
        PlayerDto dto = new PlayerDto();
        
        assertNull(dto.getId());
        assertNull(dto.getName());
        assertEquals(0, dto.getDeckSize());
        assertNull(dto.getDeck());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetPropertiesCorrectly() {
        PlayerDto dto = new PlayerDto();
        
        dto.setId("test-id");
        dto.setName("Test Name");
        dto.setDeckSize(5);
        
        assertEquals("test-id", dto.getId());
        assertEquals("Test Name", dto.getName());
        assertEquals(5, dto.getDeckSize());
    }

    @Test
    @DisplayName("Should handle deck list correctly")
    void shouldHandleDeckListCorrectly() {
        PlayerDto dto = new PlayerDto();
        CardDto card1 = new CardDto();
        card1.setRank(1);
        card1.setSuit("Coppe");
        CardDto card2 = new CardDto();
        card2.setRank(2);
        card2.setSuit("Ori");
        
        dto.setDeck(Arrays.asList(card1, card2));
        
        assertEquals(2, dto.getDeck().size());
        assertEquals(1, dto.getDeck().get(0).getRank());
        assertEquals(2, dto.getDeck().get(1).getRank());
    }

    @Test
    @DisplayName("Should return defensive copy of deck")
    void shouldReturnDefensiveCopyOfDeck() {
        player.addCardToDeck(new Card(1, Suit.COPPE));
        
        PlayerDto dto = new PlayerDto(player);
        dto.getDeck().clear(); // Modify the copy
        
        // Original player deck should remain unchanged
        assertEquals(1, player.getDeckSize());
        assertEquals(1, dto.getDeckSize());
    }
}
