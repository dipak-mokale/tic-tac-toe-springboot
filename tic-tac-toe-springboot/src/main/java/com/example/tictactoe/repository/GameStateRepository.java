package com.example.tictactoe.repository;

import com.example.tictactoe.model.GameState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameStateRepository extends MongoRepository<GameState, String> {
    Optional<GameState> findBySessionId(String sessionId);
    List<GameState> findByPlayerXName(String playerXName);
    List<GameState> findByPlayerOName(String playerOName);
    List<GameState> findByGameOverFalse();
    List<GameState> findByGameOverTrue();
}
