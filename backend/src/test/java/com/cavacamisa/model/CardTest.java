package com.cavacamisa.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    @DisplayName("Should create card with valid rank and suit")
    void shouldCreateCardWithValidRankAndSuit() {
        Card card = new Card(5, Suit.COPPE);
        
        assertEquals(5, card.getRank());
        assertEquals(Suit.COPPE, card.getSuit());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11, -1, 100})
    @DisplayName("Should throw exception for invalid rank")
    void shouldThrowExceptionForInvalidRank(int invalidRank) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Card(invalidRank, Suit.COPPE);
        });
    }

    @Test
    @DisplayName("Should identify winning cards correctly")
    void shouldIdentifyWinningCardsCorrectly() {
        // Winning cards: Asso (1), Due (2), Tre (3)
        assertTrue(new Card(1, Suit.COPPE).isWinningCard());
        assertTrue(new Card(2, Suit.COPPE).isWinningCard());
        assertTrue(new Card(3, Suit.COPPE).isWinningCard());
        
        // Non-winning cards
        assertFalse(new Card(4, Suit.COPPE).isWinningCard());
        assertFalse(new Card(5, Suit.COPPE).isWinningCard());
        assertFalse(new Card(10, Suit.COPPE).isWinningCard());
    }

    @Test
    @DisplayName("Should return correct number of cards to play for winning cards")
    void shouldReturnCorrectCardsToPlayForWinningCards() {
        assertEquals(1, new Card(1, Suit.COPPE).getCardsToPlay()); // Asso
        assertEquals(2, new Card(2, Suit.COPPE).getCardsToPlay()); // Due
        assertEquals(3, new Card(3, Suit.COPPE).getCardsToPlay()); // Tre
    }

    @Test
    @DisplayName("Should return zero cards to play for non-winning cards")
    void shouldReturnZeroCardsToPlayForNonWinningCards() {
        assertEquals(0, new Card(4, Suit.COPPE).getCardsToPlay());
        assertEquals(0, new Card(5, Suit.COPPE).getCardsToPlay());
        assertEquals(0, new Card(10, Suit.COPPE).getCardsToPlay());
    }

    @ParameterizedTest
    @MethodSource("cardDisplayNameProvider")
    @DisplayName("Should return correct display name")
    void shouldReturnCorrectDisplayName(int rank, String expectedRankName, Suit suit) {
        Card card = new Card(rank, suit);
        String expectedDisplayName = expectedRankName + " di " + suit.getDisplayName();
        
        assertEquals(expectedDisplayName, card.getDisplayName());
    }

    static Stream<Arguments> cardDisplayNameProvider() {
        return Stream.of(
            Arguments.of(1, "Asso", Suit.COPPE),
            Arguments.of(2, "Due", Suit.ORI),
            Arguments.of(3, "Tre", Suit.SPADE),
            Arguments.of(4, "Quattro", Suit.BASTONI),
            Arguments.of(5, "Cinque", Suit.COPPE),
            Arguments.of(6, "Sei", Suit.ORI),
            Arguments.of(7, "Sette", Suit.SPADE),
            Arguments.of(8, "Fante", Suit.BASTONI),
            Arguments.of(9, "Cavallo", Suit.COPPE),
            Arguments.of(10, "Re", Suit.ORI)
        );
    }

    @Test
    @DisplayName("Should be equal to another card with same rank and suit")
    void shouldBeEqualToAnotherCardWithSameRankAndSuit() {
        Card card1 = new Card(5, Suit.COPPE);
        Card card2 = new Card(5, Suit.COPPE);
        
        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to card with different rank")
    void shouldNotBeEqualToCardWithDifferentRank() {
        Card card1 = new Card(5, Suit.COPPE);
        Card card2 = new Card(6, Suit.COPPE);
        
        assertNotEquals(card1, card2);
    }

    @Test
    @DisplayName("Should not be equal to card with different suit")
    void shouldNotBeEqualToCardWithDifferentSuit() {
        Card card1 = new Card(5, Suit.COPPE);
        Card card2 = new Card(5, Suit.ORI);
        
        assertNotEquals(card1, card2);
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        Card card = new Card(5, Suit.COPPE);
        
        assertNotEquals(null, card);
    }

    @Test
    @DisplayName("Should not be equal to different object type")
    void shouldNotBeEqualToDifferentObjectType() {
        Card card = new Card(5, Suit.COPPE);
        String string = "not a card";
        
        assertNotEquals(card, string);
    }

    @Test
    @DisplayName("Should return correct string representation")
    void shouldReturnCorrectStringRepresentation() {
        Card card = new Card(5, Suit.COPPE);
        String expected = "Cinque di Coppe";
        
        assertEquals(expected, card.toString());
    }
}
