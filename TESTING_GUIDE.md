# Quick Testing Guide - Tic Tac Toe Application

## Prerequisites
- Maven installed and configured
- MySQL server running
- Database initialized (run `database-init.sql`)
- Port 8080 available

## Build Steps

```bash
cd tic-tac-toe-springboot
mvn clean package
java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
```

Expected output:
```
Tomcat started on port(s): 8080
Started TicTacToeApplication in X.XXX seconds
```

## Testing via Web UI (http://localhost:8080)

### Test 1: Start a New Game
1. Open http://localhost:8080 in browser
2. Enter names in the input fields (e.g., "Alice", "Bob")
3. Click "Start Game"
4. Verify: Game board appears, first move shows "Alice (X)'s turn"

### Test 2: Play a Complete Game (X Wins)
```
Moves: 0 (2) -> 1 (4) -> 3 (5) -> 6
```
1. Click position 0 (X wins top-left to bottom-right diagonal)
2. Click position 2 (O)
3. Click position 4 (X)
4. Click position 5 (O)
5. Click position 8 (X wins)
6. Verify: Shows "🎉 Alice (X) wins!"

### Test 3: Play a Draw Game
```
Moves: 0 (1) -> 2 (3) -> 4 (5) -> 6 (7) -> 8
```
1. Alternate moves to fill the board
2. Verify: Shows "🤝 It's a draw!"

### Test 4: View Game Records
1. After completing a game, click "View Records"
2. Verify: Modal shows all completed games with:
   - Player names
   - Winner name
   - Move count
   - Timestamp

### Test 5: Play Another Game
1. Click "New Game"
2. Enter different player names
3. Play and complete the game
4. Verify: Previous record + new record appear in records view

## Testing via API (curl)

### Initialize Game
```bash
curl -X POST http://localhost:8080/api/game/initialize \
  -H "Content-Type: application/json" \
  -d '{"playerXName":"Alice","playerOName":"Bob"}'
```

Expected Response:
```json
{
  "board": [null, null, null, null, null, null, null, null, null],
  "currentPlayer": "X",
  "winner": null,
  "draw": false,
  "playerXName": "Alice",
  "playerOName": "Bob",
  "moveCount": 0
}
```

### Make a Move
```bash
curl -X POST http://localhost:8080/api/game/move \
  -H "Content-Type: application/json" \
  -d '{"position":4}'
```

### Get All Records
```bash
curl http://localhost:8080/api/game/records
```

Expected Response:
```json
[
  {
    "id": 1,
    "playerXName": "Alice",
    "playerOName": "Bob",
    "winner": "Alice",
    "moves": 5,
    "gameDate": "2026-08-09T18:30:00"
  }
]
```

### Get Records by Winner
```bash
curl http://localhost:8080/api/game/records/winner/Alice
```

### Get Records by Player
```bash
curl http://localhost:8080/api/game/records/player/Alice
```

## Database Verification

```bash
# Connect to MySQL
mysql -u root -p

# In MySQL prompt
USE tic_tac_toe_db;
SELECT * FROM game_records;
SELECT winner, COUNT(*) as total FROM game_records GROUP BY winner;
SELECT * FROM game_records WHERE player_x_name = 'Alice' OR player_o_name = 'Alice';
```

## Troubleshooting

### Build Fails
- Check: Java 17+ installed (`java -version`)
- Check: Maven installed and in PATH (`mvn -version`)
- Solution: Run `mvn clean -U package` to update dependencies

### Database Connection Failed
- Check: MySQL is running
- Check: Database exists: `mysql> SHOW DATABASES;`
- Check: Credentials in `application.properties`
- Check: `tic_tac_toe_db` created: `mysql> USE tic_tac_toe_db;`

### Port 8080 Already in Use
```bash
# Run on different port
java -Dserver.port=8081 -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
# Then access: http://localhost:8081
```

### No Records Appearing
- Check: MySQL service is running
- Check: `game_records` table exists
- Run: `mvn clean package` and restart
- Check logs for database errors

### Browser Shows "Cannot GET /"
- Verify: Application started successfully
- Wait: Spring Boot initialization can take 10-15 seconds
- Check: URL is `http://localhost:8080` (not `https`)

## Performance Testing

### Test with Multiple Games
1. Play 5-10 games
2. View records to ensure all are stored
3. Check MySQL:
   ```sql
   SELECT COUNT(*) FROM game_records;
   ```

### Test Query Performance
```bash
# Time the query
curl -w "\nTime: %{time_total}s\n" http://localhost:8080/api/game/records
```

## Log Analysis

Check logs for:
- Database connection messages
- Entity creation logs
- API request logs
- Error messages

Location: Console output when running `java -jar ...`

---

**Testing Status**: Ready  
**Date**: August 9, 2026
