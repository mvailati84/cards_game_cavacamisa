package com.cavacamisa.service;

import com.cavacamisa.dto.*;
import com.cavacamisa.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    @DisplayName("Should create game successfully")
    void shouldCreateGameSuccessfully() {
        GameDto game = gameService.createGame();
        
        assertNotNull(game.getId());
        assertEquals("Waiting for players", game.getState());
        assertTrue(game.getPlayers().isEmpty());
        assertFalse(game.isGameFinished());
    }

    @Test
    @DisplayName("Should get game by id")
    void shouldGetGameById() {
        GameDto createdGame = gameService.createGame();
        GameDto retrievedGame = gameService.getGame(createdGame.getId());
        
        assertEquals(createdGame.getId(), retrievedGame.getId());
        assertEquals(createdGame.getState(), retrievedGame.getState());
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent game")
    void shouldThrowExceptionWhenGettingNonExistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.getGame("non-existent-id");
        });
    }

    @Test
    @DisplayName("Should get all games")
    void shouldGetAllGames() {
        GameDto game1 = gameService.createGame();
        GameDto game2 = gameService.createGame();
        
        List<GameDto> allGames = gameService.getAllGames();
        
        assertEquals(2, allGames.size());
        assertTrue(allGames.stream().anyMatch(g -> g.getId().equals(game1.getId())));
        assertTrue(allGames.stream().anyMatch(g -> g.getId().equals(game2.getId())));
    }

    @Test
    @DisplayName("Should create player successfully")
    void shouldCreatePlayerSuccessfully() {
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        
        PlayerDto player = gameService.createPlayer(request);
        
        assertNotNull(player.getId());
        assertEquals("Test Player", player.getName());
        assertEquals(0, player.getDeckSize());
    }

    @Test
    @DisplayName("Should get player by id")
    void shouldGetPlayerById() {
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        PlayerDto createdPlayer = gameService.createPlayer(request);
        
        PlayerDto retrievedPlayer = gameService.getPlayer(createdPlayer.getId());
        
        assertEquals(createdPlayer.getId(), retrievedPlayer.getId());
        assertEquals(createdPlayer.getName(), retrievedPlayer.getName());
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent player")
    void shouldThrowExceptionWhenGettingNonExistentPlayer() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.getPlayer("non-existent-id");
        });
    }

    @Test
    @DisplayName("Should join game successfully")
    void shouldJoinGameSuccessfully() {
        GameDto game = gameService.createGame();
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        PlayerDto player = gameService.createPlayer(request);
        
        GameDto updatedGame = gameService.joinGame(game.getId(), player.getId());
        
        assertEquals(1, updatedGame.getPlayers().size());
        assertEquals(player.getId(), updatedGame.getPlayers().get(0).getId());
        assertEquals("Waiting for players", updatedGame.getState());
    }

    @Test
    @DisplayName("Should join game with second player and start game")
    void shouldJoinGameWithSecondPlayerAndStartGame() {
        GameDto game = gameService.createGame();
        
        CreatePlayerRequest request1 = new CreatePlayerRequest();
        request1.setName("Player 1");
        PlayerDto player1 = gameService.createPlayer(request1);
        
        CreatePlayerRequest request2 = new CreatePlayerRequest();
        request2.setName("Player 2");
        PlayerDto player2 = gameService.createPlayer(request2);
        
        gameService.joinGame(game.getId(), player1.getId());
        GameDto updatedGame = gameService.joinGame(game.getId(), player2.getId());
        
        assertEquals(2, updatedGame.getPlayers().size());
        assertEquals("Game in progress", updatedGame.getState());
        assertEquals(20, updatedGame.getPlayers().get(0).getDeckSize());
        assertEquals(20, updatedGame.getPlayers().get(1).getDeckSize());
    }

    @Test
    @DisplayName("Should throw exception when joining non-existent game")
    void shouldThrowExceptionWhenJoiningNonExistentGame() {
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        PlayerDto player = gameService.createPlayer(request);
        
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.joinGame("non-existent-game", player.getId());
        });
    }

    @Test
    @DisplayName("Should throw exception when joining with non-existent player")
    void shouldThrowExceptionWhenJoiningWithNonExistentPlayer() {
        GameDto game = gameService.createGame();
        
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.joinGame(game.getId(), "non-existent-player");
        });
    }

    @Test
    @DisplayName("Should throw exception when joining full game")
    void shouldThrowExceptionWhenJoiningFullGame() {
        GameDto game = gameService.createGame();
        
        CreatePlayerRequest request1 = new CreatePlayerRequest();
        request1.setName("Player 1");
        PlayerDto player1 = gameService.createPlayer(request1);
        
        CreatePlayerRequest request2 = new CreatePlayerRequest();
        request2.setName("Player 2");
        PlayerDto player2 = gameService.createPlayer(request2);
        
        CreatePlayerRequest request3 = new CreatePlayerRequest();
        request3.setName("Player 3");
        PlayerDto player3 = gameService.createPlayer(request3);
        
        gameService.joinGame(game.getId(), player1.getId());
        gameService.joinGame(game.getId(), player2.getId());
        
        assertThrows(IllegalStateException.class, () -> {
            gameService.joinGame(game.getId(), player3.getId());
        });
    }

    @Test
    @DisplayName("Should play card successfully")
    void shouldPlayCardSuccessfully() {
        GameDto game = gameService.createGame();
        
        CreatePlayerRequest request1 = new CreatePlayerRequest();
        request1.setName("Player 1");
        PlayerDto player1 = gameService.createPlayer(request1);
        
        CreatePlayerRequest request2 = new CreatePlayerRequest();
        request2.setName("Player 2");
        PlayerDto player2 = gameService.createPlayer(request2);
        
        gameService.joinGame(game.getId(), player1.getId());
        gameService.joinGame(game.getId(), player2.getId());
        
        PlayCardRequest playRequest = new PlayCardRequest();
        playRequest.setPlayerId(player1.getId());
        
        GameDto updatedGame = gameService.playCard(game.getId(), playRequest);
        
        assertEquals(19, updatedGame.getPlayers().get(0).getDeckSize()); // Player1 lost 1 card
        assertEquals(20, updatedGame.getPlayers().get(1).getDeckSize()); // Player2 still has 20 cards
        assertEquals(1, updatedGame.getTableCards().size()); // 1 card on table
    }

    @Test
    @DisplayName("Should throw exception when playing card in non-existent game")
    void shouldThrowExceptionWhenPlayingCardInNonExistentGame() {
        PlayCardRequest request = new PlayCardRequest();
        request.setPlayerId("player-id");
        
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.playCard("non-existent-game", request);
        });
    }

    @Test
    @DisplayName("Should throw exception when playing card out of turn")
    void shouldThrowExceptionWhenPlayingCardOutOfTurn() {
        GameDto game = gameService.createGame();
        
        CreatePlayerRequest request1 = new CreatePlayerRequest();
        request1.setName("Player 1");
        PlayerDto player1 = gameService.createPlayer(request1);
        
        CreatePlayerRequest request2 = new CreatePlayerRequest();
        request2.setName("Player 2");
        PlayerDto player2 = gameService.createPlayer(request2);
        
        gameService.joinGame(game.getId(), player1.getId());
        gameService.joinGame(game.getId(), player2.getId());
        
        PlayCardRequest playRequest = new PlayCardRequest();
        playRequest.setPlayerId(player2.getId()); // Player2 tries to play out of turn
        
        assertThrows(IllegalStateException.class, () -> {
            gameService.playCard(game.getId(), playRequest);
        });
    }

    @Test
    @DisplayName("Should delete game successfully")
    void shouldDeleteGameSuccessfully() {
        GameDto game = gameService.createGame();
        
        gameService.deleteGame(game.getId());
        
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.getGame(game.getId());
        });
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent game")
    void shouldThrowExceptionWhenDeletingNonExistentGame() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.deleteGame("non-existent-id");
        });
    }

    @Test
    @DisplayName("Should delete player successfully")
    void shouldDeletePlayerSuccessfully() {
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        PlayerDto player = gameService.createPlayer(request);
        
        gameService.deletePlayer(player.getId());
        
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.getPlayer(player.getId());
        });
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent player")
    void shouldThrowExceptionWhenDeletingNonExistentPlayer() {
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.deletePlayer("non-existent-id");
        });
    }

    @Test
    @DisplayName("Should check if game exists")
    void shouldCheckIfGameExists() {
        GameDto game = gameService.createGame();
        
        assertTrue(gameService.gameExists(game.getId()));
        assertFalse(gameService.gameExists("non-existent-id"));
    }

    @Test
    @DisplayName("Should check if player exists")
    void shouldCheckIfPlayerExists() {
        CreatePlayerRequest request = new CreatePlayerRequest();
        request.setName("Test Player");
        PlayerDto player = gameService.createPlayer(request);
        
        assertTrue(gameService.playerExists(player.getId()));
        assertFalse(gameService.playerExists("non-existent-id"));
    }
}
