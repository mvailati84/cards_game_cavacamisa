package com.cavacamisa.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("player1", "Test Player");
    }

    @Test
    @DisplayName("Should create player with correct id and name")
    void shouldCreatePlayerWithCorrectIdAndName() {
        assertEquals("player1", player.getId());
        assertEquals("Test Player", player.getName());
    }

    @Test
    @DisplayName("Should have empty deck initially")
    void shouldHaveEmptyDeckInitially() {
        assertTrue(player.getDeck().isEmpty());
        assertEquals(0, player.getDeckSize());
        assertFalse(player.hasCards());
    }

    @Test
    @DisplayName("Should add single card to deck")
    void shouldAddSingleCardToDeck() {
        Card card = new Card(5, Suit.COPPE);
        player.addCardToDeck(card);
        
        assertEquals(1, player.getDeckSize());
        assertTrue(player.hasCards());
        assertEquals(card, player.getDeck().get(0));
    }

    @Test
    @DisplayName("Should add multiple cards to deck")
    void shouldAddMultipleCardsToDeck() {
        List<Card> cards = Arrays.asList(
            new Card(1, Suit.COPPE),
            new Card(2, Suit.ORI),
            new Card(3, Suit.SPADE)
        );
        player.addCardsToDeck(cards);
        
        assertEquals(3, player.getDeckSize());
        assertTrue(player.hasCards());
        assertEquals(cards, player.getDeck());
    }

    @Test
    @DisplayName("Should draw card from top of deck")
    void shouldDrawCardFromTopOfDeck() {
        Card card1 = new Card(1, Suit.COPPE);
        Card card2 = new Card(2, Suit.ORI);
        player.addCardToDeck(card1);
        player.addCardToDeck(card2);
        
        Card drawnCard = player.drawCard();
        
        assertEquals(card1, drawnCard); // Should draw the first card added (top of deck)
        assertEquals(1, player.getDeckSize());
        assertEquals(card2, player.getDeck().get(0)); // Remaining card should be card2
    }

    @Test
    @DisplayName("Should return null when drawing from empty deck")
    void shouldReturnNullWhenDrawingFromEmptyDeck() {
        assertNull(player.drawCard());
    }

    @Test
    @DisplayName("Should capture cards to bottom of deck")
    void shouldCaptureCardsToBottomOfDeck() {
        Card originalCard = new Card(1, Suit.COPPE);
        player.addCardToDeck(originalCard);
        
        List<Card> capturedCards = Arrays.asList(
            new Card(2, Suit.ORI),
            new Card(3, Suit.SPADE)
        );
        player.captureCards(capturedCards);
        
        assertEquals(3, player.getDeckSize());
        assertEquals(originalCard, player.getDeck().get(0)); // Original card still on top
        assertTrue(player.getDeck().containsAll(capturedCards)); // Captured cards added
    }

    @Test
    @DisplayName("Should identify winner when having all 40 cards")
    void shouldIdentifyWinnerWhenHavingAll40Cards() {
        // Add 40 cards to the player
        for (int i = 0; i < 40; i++) {
            player.addCardToDeck(new Card((i % 10) + 1, Suit.values()[i % 4]));
        }
        
        assertTrue(player.isWinner());
        assertEquals(40, player.getDeckSize());
    }

    @Test
    @DisplayName("Should not be winner with less than 40 cards")
    void shouldNotBeWinnerWithLessThan40Cards() {
        player.addCardToDeck(new Card(1, Suit.COPPE));
        
        assertFalse(player.isWinner());
    }

    @Test
    @DisplayName("Should identify loss when deck is empty")
    void shouldIdentifyLossWhenDeckIsEmpty() {
        assertTrue(player.hasLost());
        assertFalse(player.hasCards());
    }

    @Test
    @DisplayName("Should not have lost when deck has cards")
    void shouldNotHaveLostWhenDeckHasCards() {
        player.addCardToDeck(new Card(1, Suit.COPPE));
        
        assertFalse(player.hasLost());
        assertTrue(player.hasCards());
    }

    @Test
    @DisplayName("Should return defensive copy of deck")
    void shouldReturnDefensiveCopyOfDeck() {
        Card card = new Card(1, Suit.COPPE);
        player.addCardToDeck(card);
        
        List<Card> deckCopy = player.getDeck();
        deckCopy.clear(); // Modify the copy
        
        // Original player deck should remain unchanged
        assertEquals(1, player.getDeckSize());
        assertTrue(player.hasCards());
    }

    @Test
    @DisplayName("Should handle multiple draw operations correctly")
    void shouldHandleMultipleDrawOperationsCorrectly() {
        List<Card> cards = Arrays.asList(
            new Card(1, Suit.COPPE),
            new Card(2, Suit.ORI),
            new Card(3, Suit.SPADE)
        );
        player.addCardsToDeck(cards);
        
        assertEquals(cards.get(0), player.drawCard());
        assertEquals(cards.get(1), player.drawCard());
        assertEquals(cards.get(2), player.drawCard());
        
        assertTrue(player.hasLost());
        assertFalse(player.hasCards());
        assertNull(player.drawCard());
    }

    @Test
    @DisplayName("Should return correct string representation")
    void shouldReturnCorrectStringRepresentation() {
        player.addCardToDeck(new Card(1, Suit.COPPE));
        String expected = "Player{id='player1', name='Test Player', deckSize=1}";
        
        assertEquals(expected, player.toString());
    }
}
