package com.example.tictactoe.controller;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.GameRecord;
import com.example.tictactoe.model.GameState;
import com.example.tictactoe.repository.GameRecordRepository;
import com.example.tictactoe.repository.GameStateRepository;
import com.example.tictactoe.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    private final GameService gameService;
    private final GameRecordRepository gameRecordRepository;
    private final GameStateRepository gameStateRepository;

    public GameController(GameService gameService, GameRecordRepository gameRecordRepository, GameStateRepository gameStateRepository) {
        this.gameService = gameService;
        this.gameRecordRepository = gameRecordRepository;
        this.gameStateRepository = gameStateRepository;
    }

    @GetMapping
    public Game getGame() {
        return gameService.getGame();
    }

    @PostMapping("/initialize")
    public ResponseEntity<?> initializeGame(@RequestBody Map<String, String> request) {
        String playerXName = request.get("playerXName");
        String playerOName = request.get("playerOName");
        Game game = gameService.initializeGame(playerXName, playerOName);
        
        return ResponseEntity.ok(Map.of(
            "game", game,
            "message", "Game initialized successfully"
        ));
    }

    @PostMapping("/move")
    public ResponseEntity<?> move(@RequestBody Map<String, Integer> request) {
        try {
            Integer position = request.get("position");

            if (position == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Position is required."));
            }

            return ResponseEntity.ok(gameService.move(position));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public Game reset() {
        return gameService.reset();
    }

    // Game Records Endpoints
    @GetMapping("/records")
    public ResponseEntity<List<GameRecord>> getAllRecords() {
        List<GameRecord> records = gameRecordRepository.findAll();
        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/winner/{winner}")
    public ResponseEntity<List<GameRecord>> getRecordsByWinner(@PathVariable String winner) {
        List<GameRecord> records = gameRecordRepository.findByWinner(winner);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/records/player/{playerName}")
    public ResponseEntity<List<GameRecord>> getRecordsByPlayer(@PathVariable String playerName) {
        List<GameRecord> records = gameRecordRepository.findByPlayerXNameOrPlayerOName(playerName, playerName);
        return ResponseEntity.ok(records);
    }

    // Game State Endpoints
    @GetMapping("/state/{sessionId}")
    public ResponseEntity<?> getGameState(@PathVariable String sessionId) {
        GameState gameState = gameService.getGameState(sessionId);
        if (gameState == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(gameState);
    }

    @GetMapping("/states/active")
    public ResponseEntity<List<GameState>> getActiveGameStates() {
        List<GameState> activeStates = gameStateRepository.findByGameOverFalse();
        return ResponseEntity.ok(activeStates);
    }

    @GetMapping("/states/completed")
    public ResponseEntity<List<GameState>> getCompletedGameStates() {
        List<GameState> completedStates = gameStateRepository.findByGameOverTrue();
        return ResponseEntity.ok(completedStates);
    }

    @GetMapping("/states/player/{playerName}")
    public ResponseEntity<List<GameState>> getGameStatesByPlayer(@PathVariable String playerName) {
        List<GameState> playerXGames = gameStateRepository.findByPlayerXName(playerName);
        List<GameState> playerOGames = gameStateRepository.findByPlayerOName(playerName);
        playerXGames.addAll(playerOGames);
        return ResponseEntity.ok(playerXGames);
    }

    @DeleteMapping("/state/{sessionId}")
    public ResponseEntity<?> deleteGameState(@PathVariable String sessionId) {
        try {
            gameStateRepository.deleteById(sessionId);
            return ResponseEntity.ok(Map.of("message", "Game state deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
