package com.cavacamisa.dto;

import com.cavacamisa.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameDtoTest {

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
    @DisplayName("Should create GameDto with correct basic properties")
    void shouldCreateGameDtoWithCorrectBasicProperties() {
        GameDto dto = new GameDto(game);
        
        assertEquals("test-game", dto.getId());
        assertEquals("Waiting for players", dto.getState());
        assertEquals(0, dto.getCurrentPlayerIndex());
        assertEquals(0, dto.getCardsToPlay());
        assertFalse(dto.isGameFinished());
        assertNull(dto.getWinner());
        assertNull(dto.getLastWinningPlayer());
    }

    @Test
    @DisplayName("Should create GameDto with players correctly")
    void shouldCreateGameDtoWithPlayersCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        GameDto dto = new GameDto(game);
        
        assertEquals(2, dto.getPlayers().size());
        assertEquals("player1", dto.getPlayers().get(0).getId());
        assertEquals("Player 1", dto.getPlayers().get(0).getName());
        assertEquals("player2", dto.getPlayers().get(1).getId());
        assertEquals("Player 2", dto.getPlayers().get(1).getName());
    }

    @Test
    @DisplayName("Should create GameDto with table cards correctly")
    void shouldCreateGameDtoWithTableCardsCorrectly() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Play a card to add it to the table
        game.playCard("player1");
        
        GameDto dto = new GameDto(game);
        
        assertEquals(1, dto.getTableCards().size());
        assertNotNull(dto.getTableCards().get(0));
    }

    @Test
    @DisplayName("Should create GameDto with winner when game is finished")
    void shouldCreateGameDtoWithWinnerWhenGameIsFinished() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Remove all cards from player1 to make player2 the winner
        player1.getDeck().clear();
        game.playCard("player1");
        
        GameDto dto = new GameDto(game);
        
        assertTrue(dto.isGameFinished());
        assertEquals("Game finished", dto.getState());
        assertNotNull(dto.getWinner());
        assertEquals("player2", dto.getWinner().getId());
        assertNotNull(dto.getLoser());
        assertEquals("player1", dto.getLoser().getId());
    }

    @Test
    @DisplayName("Should create GameDto with last winning player")
    void shouldCreateGameDtoWithLastWinningPlayer() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        // Force player1 to play a winning card (Asso)
        player1.getDeck().clear();
        player1.addCardToDeck(new Card(1, Suit.COPPE));
        game.playCard("player1");
        
        GameDto dto = new GameDto(game);
        
        assertNotNull(dto.getLastWinningPlayer());
        assertEquals("player1", dto.getLastWinningPlayer().getId());
        assertEquals(1, dto.getCardsToPlay());
    }

    @Test
    @DisplayName("Should handle null last winning player")
    void shouldHandleNullLastWinningPlayer() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        GameDto dto = new GameDto(game);
        
        assertNull(dto.getLastWinningPlayer());
    }

    @Test
    @DisplayName("Should handle null winner")
    void shouldHandleNullWinner() {
        game.addPlayer(player1);
        game.addPlayer(player2);
        
        GameDto dto = new GameDto(game);
        
        assertNull(dto.getWinner());
        assertFalse(dto.isGameFinished());
    }

    @Test
    @DisplayName("Should create empty GameDto with default constructor")
    void shouldCreateEmptyGameDtoWithDefaultConstructor() {
        GameDto dto = new GameDto();
        
        assertNull(dto.getId());
        assertNull(dto.getState());
        assertNull(dto.getPlayers());
        assertNull(dto.getTableCards());
        assertEquals(0, dto.getCurrentPlayerIndex());
        assertEquals(0, dto.getCardsToPlay());
        assertNull(dto.getLastWinningPlayer());
        assertNull(dto.getWinner());
        assertFalse(dto.isGameFinished());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetPropertiesCorrectly() {
        GameDto dto = new GameDto();
        
        dto.setId("test-id");
        dto.setState("test-state");
        dto.setCurrentPlayerIndex(1);
        dto.setCardsToPlay(3);
        dto.setGameFinished(true);
        
        assertEquals("test-id", dto.getId());
        assertEquals("test-state", dto.getState());
        assertEquals(1, dto.getCurrentPlayerIndex());
        assertEquals(3, dto.getCardsToPlay());
        assertTrue(dto.isGameFinished());
    }

    @Test
    @DisplayName("Should handle players list correctly")
    void shouldHandlePlayersListCorrectly() {
        GameDto dto = new GameDto();
        PlayerDto playerDto = new PlayerDto();
        playerDto.setId("test-player");
        playerDto.setName("Test Player");
        
        dto.setPlayers(Arrays.asList(playerDto));
        
        assertEquals(1, dto.getPlayers().size());
        assertEquals("test-player", dto.getPlayers().get(0).getId());
    }

    @Test
    @DisplayName("Should handle table cards list correctly")
    void shouldHandleTableCardsListCorrectly() {
        GameDto dto = new GameDto();
        CardDto cardDto = new CardDto();
        cardDto.setRank(5);
        cardDto.setSuit("Coppe");
        
        dto.setTableCards(Arrays.asList(cardDto));
        
        assertEquals(1, dto.getTableCards().size());
        assertEquals(5, dto.getTableCards().get(0).getRank());
    }

    @Test
    @DisplayName("Should handle winner and last winning player correctly")
    void shouldHandleWinnerAndLastWinningPlayerCorrectly() {
        GameDto dto = new GameDto();
        PlayerDto winnerDto = new PlayerDto();
        winnerDto.setId("winner");
        PlayerDto lastWinningDto = new PlayerDto();
        lastWinningDto.setId("last-winner");
        
        dto.setWinner(winnerDto);
        dto.setLastWinningPlayer(lastWinningDto);
        
        assertEquals("winner", dto.getWinner().getId());
        assertEquals("last-winner", dto.getLastWinningPlayer().getId());
    }
}
