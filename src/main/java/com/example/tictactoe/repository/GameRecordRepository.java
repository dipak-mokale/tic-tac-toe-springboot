package com.example.tictactoe.repository;

import com.example.tictactoe.model.GameRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {
    List<GameRecord> findByWinner(String winner);
    List<GameRecord> findByPlayerXNameOrPlayerOName(String playerXName, String playerOName);
}

