package com.example.tictactoe.controller;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@CrossOrigin
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Game getGame() {
        return gameService.getGame();
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
}
