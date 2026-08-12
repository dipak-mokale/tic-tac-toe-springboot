package com.example.tictactoe.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Document(collection = "game_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRecord {
    @Id
    private String id;

    private String playerXName;

    private String playerOName;

    private String winner;

    private Integer moves;

    private LocalDateTime gameDate;

    public GameRecord(String playerXName, String playerOName, String winner, Integer moves) {
        this.playerXName = playerXName;
        this.playerOName = playerOName;
        this.winner = winner;
        this.moves = moves;
        this.gameDate = LocalDateTime.now();
    }
}

