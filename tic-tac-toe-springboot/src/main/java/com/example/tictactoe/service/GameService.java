package com.example.tictactoe.service;

import com.example.tictactoe.model.Game;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Game game = new Game();

    public synchronized Game getGame() {
        return game;
    }

    public synchronized Game reset() {
        game.reset();
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

        if (hasWinner(game.getCurrentPlayer())) {
            game.setWinner(game.getCurrentPlayer());
        } else if (isBoardFull()) {
            game.setDraw(true);
        } else {
            game.setCurrentPlayer(game.getCurrentPlayer().equals("X") ? "O" : "X");
        }

        return game;
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
