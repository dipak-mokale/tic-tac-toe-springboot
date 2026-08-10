package com.example.tictactoe.service;

import com.example.tictactoe.model.Game;
import com.example.tictactoe.model.GameRecord;
import com.example.tictactoe.model.GameState;
import com.example.tictactoe.repository.GameRecordRepository;
import com.example.tictactoe.repository.GameStateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class GameService {

    private final Game game = new Game();
    private final GameRecordRepository gameRecordRepository;
    private final GameStateRepository gameStateRepository;
    private String currentSessionId;

    public GameService(GameRecordRepository gameRecordRepository, GameStateRepository gameStateRepository) {
        this.gameRecordRepository = gameRecordRepository;
        this.gameStateRepository = gameStateRepository;
    }

    public synchronized Game getGame() {
        return game;
    }

    public synchronized Game reset() {
        game.reset();
        currentSessionId = null;
        return game;
    }

    public synchronized Game initializeGame(String playerXName, String playerOName) {
        game.reset();
        game.setPlayerXName(playerXName != null ? playerXName : "Player X");
        game.setPlayerOName(playerOName != null ? playerOName : "Player O");
        
        // Create new session ID for this game
        currentSessionId = UUID.randomUUID().toString();
        
        // Save game state to database
        GameState gameState = new GameState(currentSessionId, game.getPlayerXName(), game.getPlayerOName());
        gameStateRepository.save(gameState);
        
        return game;
    }

    public synchronized Game move(int position) {
        if (position < 0 || position > 8) {
            throw new IllegalArgumentException("Position must be between 0 and 8.");
        }

        if (game.isGameOver()) {
            throw new IllegalStateException("Game is already over.");
        }

        if (game.getBoard()[position] != null) {
            throw new IllegalArgumentException("That cell is already occupied.");
        }

        game.getBoard()[position] = game.getCurrentPlayer();
        game.incrementMoveCount();

        if (hasWinner(game.getCurrentPlayer())) {
            game.setWinner(game.getCurrentPlayer());
            saveGameState();
            saveGameRecord();
        } else if (isBoardFull()) {
            game.setDraw(true);
            saveGameState();
            saveGameRecord();
        } else {
            game.setCurrentPlayer(game.getCurrentPlayer().equals("X") ? "O" : "X");
            saveGameState();
        }

        return game;
    }

    private void saveGameState() {
        if (currentSessionId == null) {
            return;
        }
        
        GameState gameState = gameStateRepository.findBySessionId(currentSessionId)
            .orElse(new GameState(currentSessionId, game.getPlayerXName(), game.getPlayerOName()));
        
        gameState.setBoard(game.getBoard().clone());
        gameState.setCurrentPlayer(game.getCurrentPlayer());
        gameState.setWinner(game.getWinner());
        gameState.setDraw(game.isDraw());
        gameState.setMoveCount(game.getMoveCount());
        gameState.setLastMoveTime(LocalDateTime.now());
        gameState.setGameOver(game.isGameOver());
        
        gameStateRepository.save(gameState);
    }

    private void saveGameRecord() {
        String winner;
        if (game.isDraw()) {
            winner = "DRAW";
        } else {
            winner = game.getWinner().equals("X") ? game.getPlayerXName() : game.getPlayerOName();
        }

        GameRecord record = new GameRecord(
            game.getPlayerXName(),
            game.getPlayerOName(),
            winner,
            game.getMoveCount()
        );
        gameRecordRepository.save(record);
    }

    public GameState getGameState(String sessionId) {
        return gameStateRepository.findBySessionId(sessionId).orElse(null);
    }

    private boolean hasWinner(String player) {
        String[] b = game.getBoard();

        int[][] lines = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
        };

        for (int[] line : lines) {
            if (player.equals(b[line[0]])
                    && player.equals(b[line[1]])
                    && player.equals(b[line[2]])) {
                return true;
            }
        }

        return false;
    }

    private boolean isBoardFull() {
        for (String cell : game.getBoard()) {
            if (cell == null) {
                return false;
            }
        }
        return true;
    }
}
