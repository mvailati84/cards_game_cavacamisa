package com.cavacamisa.service;

import com.cavacamisa.model.Game;
import com.cavacamisa.model.Player;
import com.cavacamisa.dto.GameDto;
import com.cavacamisa.dto.PlayerDto;
import com.cavacamisa.dto.CreatePlayerRequest;
import com.cavacamisa.dto.PlayCardRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    private final Map<String, Player> players = new ConcurrentHashMap<>();

    public GameDto createGame() {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId);
        games.put(gameId, game);
        return new GameDto(game);
    }

    public GameDto getGame(String gameId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        return new GameDto(game);
    }

    public List<GameDto> getAllGames() {
        return games.values().stream()
                .map(GameDto::new)
                .toList();
    }

    public PlayerDto createPlayer(CreatePlayerRequest request) {
        String playerId = UUID.randomUUID().toString();
        Player player = new Player(playerId, request.getName());
        players.put(playerId, player);
        return new PlayerDto(player);
    }

    public PlayerDto getPlayer(String playerId) {
        Player player = players.get(playerId);
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
        return new PlayerDto(player);
    }

    public GameDto joinGame(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        Player player = players.get(playerId);
        if (player == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }

        if (!game.addPlayer(player)) {
            throw new IllegalStateException("Game is full or already started");
        }

        return new GameDto(game);
    }

    public GameDto playCard(String gameId, PlayCardRequest request) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }

        boolean success = game.playCard(request.getPlayerId());
        if (!success) {
            throw new IllegalStateException("Invalid move: not your turn or no cards to play");
        }

        return new GameDto(game);
    }

    public void deleteGame(String gameId) {
        Game removed = games.remove(gameId);
        if (removed == null) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
    }

    public void deletePlayer(String playerId) {
        Player removed = players.remove(playerId);
        if (removed == null) {
            throw new IllegalArgumentException("Player not found: " + playerId);
        }
    }

    public boolean gameExists(String gameId) {
        return games.containsKey(gameId);
    }

    public boolean playerExists(String playerId) {
        return players.containsKey(playerId);
    }
}
