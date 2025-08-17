package com.cavacamisa.controller;

import com.cavacamisa.service.GameService;
import com.cavacamisa.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "*")
@Tag(name = "Game", description = "Game management API")
public class GameController {

    @Autowired
    private GameService gameService;

    // Game management endpoints
    @Operation(summary = "Create a new game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game created successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<GameDto> createGame() {
        GameDto game = gameService.createGame();
        return ResponseEntity.ok(game);
    }

    @Operation(summary = "Get game by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game found"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameDto> getGame(@PathVariable String gameId) {
        try {
            GameDto game = gameService.getGame(gameId);
            return ResponseEntity.ok(game);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all games")
    @GetMapping
    public ResponseEntity<List<GameDto>> getAllGames() {
        List<GameDto> games = gameService.getAllGames();
        return ResponseEntity.ok(games);
    }

    @Operation(summary = "Delete game by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Game deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
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
    @Operation(summary = "Create a new player")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid player data")
    })
    @PostMapping("/player")
    public ResponseEntity<PlayerDto> createPlayer(@RequestBody CreatePlayerRequest request) {
        PlayerDto player = gameService.createPlayer(request);
        return ResponseEntity.ok(player);
    }

    @Operation(summary = "Get player by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player found"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
    @GetMapping("/player/{playerId}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable String playerId) {
        try {
            PlayerDto player = gameService.getPlayer(playerId);
            return ResponseEntity.ok(player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete player by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Player not found")
    })
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
    @Operation(summary = "Join a game")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully joined game"),
        @ApiResponse(responseCode = "400", description = "Invalid request or game full"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
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

    @Operation(summary = "Play a card")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Card played successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid move or not player's turn"),
        @ApiResponse(responseCode = "404", description = "Game not found")
    })
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
    @Operation(summary = "Check if game exists")
    @GetMapping("/{gameId}/exists")
    public ResponseEntity<Map<String, Boolean>> gameExists(@PathVariable String gameId) {
        boolean exists = gameService.gameExists(gameId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

    @Operation(summary = "Check if player exists")
    @GetMapping("/player/{playerId}/exists")
    public ResponseEntity<Map<String, Boolean>> playerExists(@PathVariable String playerId) {
        boolean exists = gameService.playerExists(playerId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
