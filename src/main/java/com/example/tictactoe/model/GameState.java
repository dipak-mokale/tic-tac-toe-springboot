package com.example.tictactoe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "game_states")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameState {
    @Id
    private String id;

    private String sessionId;

    private String playerXName;

    private String playerOName;

    private String[] board;

    private String currentPlayer;

    private String winner;

    private Boolean draw;

    private Integer moveCount;

    private LocalDateTime startTime;

    private LocalDateTime lastMoveTime;

    private Boolean gameOver;

    public GameState(String sessionId, String playerXName, String playerOName) {
        this.sessionId = sessionId;
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.board = new String[9];
        this.currentPlayer = "X";
        this.winner = null;
        this.draw = false;
        this.moveCount = 0;
        this.startTime = LocalDateTime.now();
        this.lastMoveTime = LocalDateTime.now();
        this.gameOver = false;
    }
}
