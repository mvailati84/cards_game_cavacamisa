package com.cavacamisa.dto;

import com.cavacamisa.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class CardDtoTest {

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card(5, Suit.COPPE);
    }

    @Test
    @DisplayName("Should create CardDto with correct properties")
    void shouldCreateCardDtoWithCorrectProperties() {
        CardDto dto = new CardDto(card);
        
        assertEquals(5, dto.getRank());
        assertEquals("Coppe", dto.getSuit());
        assertEquals("Cinque di Coppe", dto.getDisplayName());
    }

    @Test
    @DisplayName("Should create CardDto for winning card")
    void shouldCreateCardDtoForWinningCard() {
        Card winningCard = new Card(1, Suit.ORI); // Asso
        CardDto dto = new CardDto(winningCard);
        
        assertEquals(1, dto.getRank());
        assertEquals("Ori", dto.getSuit());
        assertEquals("Asso di Ori", dto.getDisplayName());
    }

    @Test
    @DisplayName("Should create empty CardDto with default constructor")
    void shouldCreateEmptyCardDtoWithDefaultConstructor() {
        CardDto dto = new CardDto();
        
        assertEquals(0, dto.getRank());
        assertNull(dto.getSuit());
        assertNull(dto.getDisplayName());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetPropertiesCorrectly() {
        CardDto dto = new CardDto();
        
        dto.setRank(7);
        dto.setSuit("Spade");
        dto.setDisplayName("Sette di Spade");
        
        assertEquals(7, dto.getRank());
        assertEquals("Spade", dto.getSuit());
        assertEquals("Sette di Spade", dto.getDisplayName());
    }

    @Test
    @DisplayName("Should handle all card ranks correctly")
    void shouldHandleAllCardRanksCorrectly() {
        CardDto dto1 = new CardDto(new Card(1, Suit.COPPE));
        CardDto dto2 = new CardDto(new Card(2, Suit.ORI));
        CardDto dto3 = new CardDto(new Card(3, Suit.SPADE));
        CardDto dto8 = new CardDto(new Card(8, Suit.BASTONI));
        CardDto dto9 = new CardDto(new Card(9, Suit.COPPE));
        CardDto dto10 = new CardDto(new Card(10, Suit.ORI));
        
        assertEquals(1, dto1.getRank());
        assertEquals(2, dto2.getRank());
        assertEquals(3, dto3.getRank());
        assertEquals(8, dto8.getRank());
        assertEquals(9, dto9.getRank());
        assertEquals(10, dto10.getRank());
    }

    @Test
    @DisplayName("Should handle all suits correctly")
    void shouldHandleAllSuitsCorrectly() {
        CardDto dto1 = new CardDto(new Card(5, Suit.COPPE));
        CardDto dto2 = new CardDto(new Card(5, Suit.ORI));
        CardDto dto3 = new CardDto(new Card(5, Suit.SPADE));
        CardDto dto4 = new CardDto(new Card(5, Suit.BASTONI));
        
        assertEquals("Coppe", dto1.getSuit());
        assertEquals("Ori", dto2.getSuit());
        assertEquals("Spade", dto3.getSuit());
        assertEquals("Bastoni", dto4.getSuit());
    }

    @Test
    @DisplayName("Should display correct names for all cards")
    void shouldDisplayCorrectNamesForAllCards() {
        CardDto asso = new CardDto(new Card(1, Suit.COPPE));
        CardDto due = new CardDto(new Card(2, Suit.ORI));
        CardDto tre = new CardDto(new Card(3, Suit.SPADE));
        CardDto fante = new CardDto(new Card(8, Suit.BASTONI));
        CardDto cavallo = new CardDto(new Card(9, Suit.COPPE));
        CardDto re = new CardDto(new Card(10, Suit.ORI));
        
        assertEquals("Asso di Coppe", asso.getDisplayName());
        assertEquals("Due di Ori", due.getDisplayName());
        assertEquals("Tre di Spade", tre.getDisplayName());
        assertEquals("Fante di Bastoni", fante.getDisplayName());
        assertEquals("Cavallo di Coppe", cavallo.getDisplayName());
        assertEquals("Re di Ori", re.getDisplayName());
    }
}
