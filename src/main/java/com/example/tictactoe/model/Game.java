package com.example.tictactoe.model;

public class Game {
    private String[] board;
    private String currentPlayer;
    private String winner;
    private boolean draw;

    public Game() {
        reset();
    }

    public void reset() {
        board = new String[9];
        currentPlayer = "X";
        winner = null;
        draw = false;
    }

    public String[] getBoard() {
        return board;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isDraw() {
        return draw;
    }

    public boolean isGameOver() {
        return winner != null || draw;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
