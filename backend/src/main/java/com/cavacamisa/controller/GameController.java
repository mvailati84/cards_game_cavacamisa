package com.cavacamisa.controller;

import com.cavacamisa.service.GameService;
import com.cavacamisa.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    // Game management endpoints
    @PostMapping
    public ResponseEntity<GameDto> createGame() {
        GameDto game = gameService.createGame();
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameDto> getGame(@PathVariable String gameId) {
        try {
            GameDto game = gameService.getGame(gameId);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> getAllGames() {
        List<GameDto> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable String gameId) {
        try {
            gameService.deleteGame(gameId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Player management endpoints
    @PostMapping("/player")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody CreatePlayerRequest request) {
        PlayerDto player = gameService.createPlayer(request);
        return ResponseEntity.ok(player);
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String playerId) {
        try {
            PlayerDto player = gameService.getPlayer(playerId);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String playerId) {
        try {
            gameService.deletePlayer(playerId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Game action endpoints
    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameDto> joinGame(@PathVariable String gameId, @RequestBody Map<String, String> request) {
        try {
            String playerId = request.get("playerId");
            if (playerId == null) {
                return ResponseEntity.badRequest().build();
            }
            GameDto game = gameService.joinGame(gameId, playerId);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/{gameId}/play")
    public ResponseEntity<GameDto> playCard(@PathVariable String gameId, @RequestBody PlayCardRequest request) {
        try {
            GameDto game = gameService.playCard(gameId, request);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Utility endpoints
    @GetMapping("/{gameId}/exists")
    public ResponseEntity<Map<String, Boolean>> gameExists(@PathVariable String gameId) {
        boolean exists = gameService.gameExists(gameId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @GetMapping("/player/{playerId}/exists")
    public ResponseEntity<Map<String, Boolean>> playerExists(@PathVariable String playerId) {
        boolean exists = gameService.playerExists(playerId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
