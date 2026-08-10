package com.example.tictactoe.model;

public class Game {
    private String[] board;
    private String currentPlayer;
    private String winner;
    private boolean draw;
    private String playerXName;
    private String playerOName;
    private int moveCount;

    public Game() {
        reset();
    }

    public void reset() {
        board = new String[9];
        currentPlayer = "X";
        winner = null;
        draw = false;
        moveCount = 0;
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

    public String getPlayerXName() {
        return playerXName;
    }

    public void setPlayerXName(String playerXName) {
        this.playerXName = playerXName;
    }

    public String getPlayerOName() {
        return playerOName;
    }

    public void setPlayerOName(String playerOName) {
        this.playerOName = playerOName;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public void incrementMoveCount() {
        this.moveCount++;
    }
}
