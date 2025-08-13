package com.cavacamisa.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        game = new Game("test-game");
        player1 = new Player("player1", "Player 1");
        player2 = new Player("player2", "Player 2");
    }

    @Test
    @DisplayName("Should create game with correct initial state")
    void shouldCreateGameWithCorrectInitialState() {
        assertEquals("test-game", game.getId());
        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());
        assertTrue(game.getPlayers().isEmpty());
        assertTrue(game.getTableCards().isEmpty());
        assertEquals(0, game.getCurrentPlayerIndex());
        assertEquals(0, game.getCardsToPlay());
        assertNull(game.getCurrentPlayer());
        assertNull(game.getLastWinningPlayer());
    }

    @Test
    @DisplayName("Should add first player successfully")
    void shouldAddFirstPlayerSuccessfully() {
        assertTrue(game.addPlayer(player1));
        assertEquals(1, game.getPlayers().size());
        assertEquals(player1, game.getPlayers().get(0));
        assertEquals(GameState.WAITING_FOR_PLAYERS, game.getState());
    }

    @Test
    @DisplayName("Should add second player and start game")
    void shouldAddSecondPlayerAndStartGame() {
        game.addPlayer(player1);
        assertTrue(game.addPlayer(player2));
        
        assertEquals(2, game.getPlayers().size());
        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(player1, game.getCurrentPlayer());
        
        // Both players should have 20 cards each
        assertEquals(20, player1.getDeckSize());
        assertEquals(20, player2.getDeckSize());
    }

    @Test
    @DisplayName("Should not add more than 2 players")
    void shouldNotAddMoreThan2Players() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        Player player3 = new Player("player3", "Player 3");
        assertFalse(game.addPlayer(player3));
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    @DisplayName("Should not play card when game is not in playing state")
    void shouldNotPlayCardWhenGameIsNotInPlayingState() {
        assertFalse(game.playCard("player1"));
    }

    @Test
    @DisplayName("Should not play card when it's not player's turn")
    void shouldNotPlayCardWhenItIsNotPlayerTurn() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Current player is player1, but player2 tries to play
        assertFalse(game.playCard("player2"));
    }

    @Test
    @DisplayName("Should play normal card successfully")
    void shouldPlayNormalCardSuccessfully() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        int initialDeckSize = player1.getDeckSize();
        int initialTableCards = game.getTableCards().size();
        
        assertTrue(game.playCard("player1"));
        
        assertEquals(initialDeckSize - 1, player1.getDeckSize());
        assertEquals(initialTableCards + 1, game.getTableCards().size());
        assertEquals(player2, game.getCurrentPlayer()); // Turn should pass to player2
    }

    @Test
    @DisplayName("Should handle winning card (Asso) correctly")
    void shouldHandleWinningCardAssoCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Force player1 to have an Asso (rank 1) as their top card
        player1.getDeck().clear();
        player1.addCardToDeck(new Card(1, Suit.COPPE)); // Asso
        
        assertTrue(game.playCard("player1"));
        
        assertEquals(player1, game.getLastWinningPlayer());
        assertEquals(1, game.getCardsToPlay());
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should handle winning card (Due) correctly")
    void shouldHandleWinningCardDueCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Force player1 to have a Due (rank 2) as their top card
        player1.getDeck().clear();
        player1.addCardToDeck(new Card(2, Suit.COPPE)); // Due
        
        assertTrue(game.playCard("player1"));
        
        assertEquals(player1, game.getLastWinningPlayer());
        assertEquals(2, game.getCardsToPlay());
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should handle winning card (Tre) correctly")
    void shouldHandleWinningCardTreCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Force player1 to have a Tre (rank 3) as their top card
        player1.getDeck().clear();
        player1.addCardToDeck(new Card(3, Suit.COPPE)); // Tre
        
        assertTrue(game.playCard("player1"));
        
        assertEquals(player1, game.getLastWinningPlayer());
        assertEquals(3, game.getCardsToPlay());
        assertEquals(player2, game.getCurrentPlayer());
    }

    @Test
    @DisplayName("Should handle obligation to play cards")
    void shouldHandleObligationToPlayCards() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Player1 plays a Due, player2 must play 2 cards
        player1.getDeck().clear();
        player1.addCardToDeck(new Card(2, Suit.COPPE)); // Due
        game.playCard("player1");
        
        // Player2 plays first obligated card
        assertTrue(game.playCard("player2"));
        assertEquals(1, game.getCardsToPlay());
        assertEquals(player1, game.getCurrentPlayer());
        
        // Player1 plays normal card
        game.playCard("player1");
        
        // Player2 plays second obligated card
        assertTrue(game.playCard("player2"));
        assertEquals(0, game.getCardsToPlay());
        assertEquals(player1, game.getCurrentPlayer());
        
        // Player2 should have captured the table cards
        assertTrue(player2.getDeckSize() > 20); // More than initial 20 cards
        assertTrue(game.getTableCards().isEmpty());
    }

    @Test
    @DisplayName("Should end game when current player has no cards")
    void shouldEndGameWhenCurrentPlayerHasNoCards() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Remove all cards from player1
        player1.getDeck().clear();
        
        assertTrue(game.playCard("player1"));
        assertEquals(GameState.FINISHED, game.getState());
        assertTrue(game.isGameFinished());
    }

    @Test
    @DisplayName("Should identify winner when player has no cards")
    void shouldIdentifyWinnerWhenPlayerHasNoCards() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Remove all cards from player1
        player1.getDeck().clear();
        
        game.playCard("player1");
        
        assertEquals(player2, game.getWinner());
        assertEquals(player1, game.getLoser());
    }

    @Test
    @DisplayName("Should identify winner when player has all 40 cards")
    void shouldIdentifyWinnerWhenPlayerHasAll40Cards() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Give all 40 cards to player1
        player1.getDeck().clear();
        for (int i = 0; i < 40; i++) {
            player1.addCardToDeck(new Card((i % 10) + 1, Suit.values()[i % 4]));
        }
        
        // Remove all cards from player2
        player2.getDeck().clear();
        
        // Play a card to trigger game end check
        game.playCard("player1");
        
        assertEquals(player1, game.getWinner());
        assertEquals(player2, game.getLoser());
    }

    @Test
    @DisplayName("Should return null winner/loser when game is not finished")
    void shouldReturnNullWinnerLoserWhenGameIsNotFinished() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        assertNull(game.getWinner());
        assertNull(game.getLoser());
    }

    @Test
    @DisplayName("Should handle player turn rotation correctly")
    void shouldHandlePlayerTurnRotationCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(0, game.getCurrentPlayerIndex());
        
        game.playCard("player1");
        assertEquals(player2, game.getCurrentPlayer());
        assertEquals(1, game.getCurrentPlayerIndex());
        
        game.playCard("player2");
        assertEquals(player1, game.getCurrentPlayer());
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    @DisplayName("Should return defensive copies of collections")
    void shouldReturnDefensiveCopiesOfCollections() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        List<Player> playersCopy = game.getPlayers();
        playersCopy.clear(); // Modify the copy
        
        // Original game players should remain unchanged
        assertEquals(2, game.getPlayers().size());
        
        List<Card> tableCardsCopy = game.getTableCards();
        tableCardsCopy.add(new Card(1, Suit.COPPE)); // Modify the copy
        
        // Original game table cards should remain unchanged
        assertEquals(0, game.getTableCards().size());
    }

    @Test
    @DisplayName("Should return correct string representation")
    void shouldReturnCorrectStringRepresentation() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        String expected = "Game{id='test-game', state=PLAYING, players=2, tableCards=0, currentPlayer=Player 1}";
        assertEquals(expected, game.toString());
    }
}
