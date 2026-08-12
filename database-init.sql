-- Tic Tac Toe Database Initialization Script
-- Run this script in your MySQL client to set up the database

-- Create the database
CREATE DATABASE IF NOT EXISTS tic_tac_toe_db;
USE tic_tac_toe_db;

-- Create the game_records table
CREATE TABLE IF NOT EXISTS game_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_x_name VARCHAR(255) NOT NULL,
    player_o_name VARCHAR(255) NOT NULL,
    winner VARCHAR(255) NOT NULL,
    moves INT NOT NULL,
    game_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_winner (winner),
    INDEX idx_player_x (player_x_name),
    INDEX idx_player_o (player_o_name),
    INDEX idx_game_date (game_date)
);

-- Optional: Sample queries to verify table
-- SELECT * FROM game_records;
-- SELECT winner, COUNT(*) as total_wins FROM game_records WHERE winner != 'DRAW' GROUP BY winner;
-- SELECT * FROM game_records WHERE player_x_name = 'Alice' OR player_o_name = 'Alice';
