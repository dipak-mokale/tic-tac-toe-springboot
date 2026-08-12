# Tic Tac Toe Spring Boot - Setup & Configuration Guide

## Overview
This application has been enhanced to include:
- Player name input before starting the game
- MySQL database integration
- Automatic storage of winning records with player details
- Game records viewer

## Prerequisites
1. **Java 17+** (Verify: `java -version`)
2. **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
3. **MySQL Server** ([Download](https://www.mysql.com/downloads/))

## Step-by-Step Setup

### 1. Install Maven (if not already installed)
```bash
# Download Maven and add to PATH
# Verify installation
mvn -version
```

### 2. Create MySQL Database
```sql
CREATE DATABASE tic_tac_toe_db;
USE tic_tac_toe_db;

CREATE TABLE game_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_x_name VARCHAR(255) NOT NULL,
    player_o_name VARCHAR(255) NOT NULL,
    winner VARCHAR(255) NOT NULL,
    moves INT NOT NULL,
    game_date TIMESTAMP NOT NULL
);
```

### 3. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tic_tac_toe_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

**Note:** Update credentials if your MySQL uses different username/password

### 4. Build & Run Application

#### Build
```bash
cd tic-tac-toe-springboot
mvn clean package
```

#### Run
```bash
# Option 1: Direct JAR execution
java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar

# Option 2: Maven Spring Boot plugin
mvn spring-boot:run
```

Application will start on: **http://localhost:8080**

## Features

### Player Name Input
- Enter custom names for both players (X and O)
- Click "Start Game" to begin
- Player names are displayed during the game and stored in records

### Game Records
- All completed games are automatically saved to the database
- Click "View Records" to see:
  - Player names
  - Winner (or "DRAW" if tied)
  - Number of moves
  - Game date and time

### API Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/game/initialize` | Initialize game with player names |
| POST | `/api/game/move` | Make a move (position: 0-8) |
| POST | `/api/game/reset` | Reset game board |
| GET | `/api/game` | Get current game state |
| GET | `/api/game/records` | Get all game records |
| GET | `/api/game/records/winner/{name}` | Get records for specific winner |
| GET | `/api/game/records/player/{name}` | Get records for specific player |

### Example API Calls

```bash
# Initialize game with player names
curl -X POST http://localhost:8080/api/game/initialize \
  -H "Content-Type: application/json" \
  -d '{"playerXName":"Alice","playerOName":"Bob"}'

# Make a move
curl -X POST http://localhost:8080/api/game/move \
  -H "Content-Type: application/json" \
  -d '{"position":4}'

# View all records
curl http://localhost:8080/api/game/records

# View records for a specific player
curl http://localhost:8080/api/game/records/player/Alice
```

## Database Schema

### game_records Table
```
id (BIGINT)          - Auto-increment primary key
player_x_name (VARCHAR) - Name of player X
player_o_name (VARCHAR) - Name of player O
winner (VARCHAR)     - Winner's name or "DRAW"
moves (INT)          - Total number of moves
game_date (TIMESTAMP) - Date and time of the game
```

## Troubleshooting

### Maven Not Found
- Download and install Maven: https://maven.apache.org/download.cgi
- Add Maven bin folder to PATH environment variable

### MySQL Connection Failed
- Verify MySQL is running
- Check credentials in `application.properties`
- Ensure database `tic_tac_toe_db` exists

### Port 8080 Already in Use
```bash
# Run on different port
java -Dserver.port=8081 -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
```

### Database Not Initialized
- The application will automatically create the `game_records` table on first run (ddl-auto=update)
- Or manually create using the SQL above

## Dependencies Added
- **spring-boot-starter-data-jpa** - JPA/Hibernate for database operations
- **mysql-connector-java:8.0.33** - MySQL JDBC driver
- **lombok** - Reduce boilerplate code

## Modified Files
- `pom.xml` - Added database dependencies
- `src/main/resources/application.properties` - Database configuration
- `src/main/java/com/example/tictactoe/model/Game.java` - Added player names and move count
- `src/main/java/com/example/tictactoe/controller/GameController.java` - Added endpoints for records
- `src/main/resources/static/index.html` - Added player input UI and records modal
- `src/main/resources/static/game.js` - Updated to handle player names and records

## New Files Created
- `src/main/java/com/example/tictactoe/model/GameRecord.java` - JPA entity for storing game records
- `src/main/java/com/example/tictactoe/repository/GameRecordRepository.java` - Spring Data JPA repository

---
For more information, visit the Spring Boot documentation: https://spring.io/projects/spring-boot
