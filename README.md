# Tic Tac Toe Spring Boot Application

Enhanced Spring Boot application with **MongoDB** database integration for tracking player names and winning records.

## ? New Features

### 1. **Player Name Input**
   - Enter custom names for Player X and Player O before starting the game
   - Player names are displayed during gameplay
   - Default names provided if not specified

### 2. **MongoDB Database Integration**
   - Configured to use MongoDB as NoSQL database
   - Automatic collection creation on first write
   - Flexible schema for game records
   - Connection string: mongodb://localhost:27017/tic_tac_toe_db

### 3. **Game Records Tracking**
   - Automatically saves all completed games (wins and draws)
   - Stores player names, winner, move count, and timestamp
   - View historical game records through the UI
   - Query records by player name or winner

### 4. **Enhanced UI**
   - Player name input form
   - Modal dialog to view game records
   - Improved status messages showing player names
   - Better styling for buttons and forms

## ?? What's Included

### Modified Files
- **pom.xml** - Updated to use MongoDB Spring Data
- **Game.java** - Added player names and move tracking
- **GameService.java** - Added database save functionality and game initialization
- **GameController.java** - Added new REST endpoints for records
- **GameRecord.java** - Converted to MongoDB Document
- **GameRecordRepository.java** - Changed to MongoRepository
- **index.html** - Enhanced UI with player input and records modal
- **game.js** - Updated frontend logic to handle player names
- **style.css** - New styling for player setup and records modal
- **application.properties** - MongoDB configuration

## ?? Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+
- MongoDB Server

### Setup Steps

1. **Install & Start MongoDB**
   `bash
   # Install MongoDB from: https://www.mongodb.com/try/download/community
   
   # Start MongoDB
   mongod
   `

2. **Create Database Collection**
   `bash
   mongosh
   use tic_tac_toe_db
   db.createCollection("game_records")
   db.game_records.createIndex({ "winner": 1 })
   db.game_records.createIndex({ "playerXName": 1 })
   db.game_records.createIndex({ "playerOName": 1 })
   exit
   `

3. **Build Application**
   `bash
   mvn clean package
   `

4. **Run Application**
   `bash
   java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
   `

5. **Access Application**
   - Open browser and navigate to http://localhost:8080
   - Enter player names and click "Start Game"

## ?? MongoDB Collection

### game_records Collection
`json
{
  "_id": "ObjectId",
  "playerXName": "Alice",
  "playerOName": "Bob",
  "winner": "Alice",
  "moves": 5,
  "gameDate": "2026-08-09T18:30:00"
}
`

## ?? API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| /api/game | GET | Get current game state |
| /api/game/initialize | POST | Initialize game with player names |
| /api/game/move | POST | Make a move (position: 0-8) |
| /api/game/reset | POST | Reset current game |
| /api/game/records | GET | Get all game records |
| /api/game/records/winner/{name} | GET | Get records for specific winner |
| /api/game/records/player/{name} | GET | Get records for specific player |

## ?? Example Requests

`bash
# Initialize game
curl -X POST http://localhost:8080/api/game/initialize \
  -H "Content-Type: application/json" \
  -d '{"playerXName":"Alice","playerOName":"Bob"}'

# Make a move
curl -X POST http://localhost:8080/api/game/move \
  -H "Content-Type: application/json" \
  -d '{"position":4}'

# Get all records
curl http://localhost:8080/api/game/records

# Get player records
curl http://localhost:8080/api/game/records/player/Alice
`

## ?? Configuration

### application.properties
`properties
# MongoDB Connection String
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# Auto create indexes
spring.data.mongodb.auto-index-creation=true
`

### Custom Connection Strings

`properties
# Local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# Remote MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://user:password@cluster.mongodb.net/tic_tac_toe_db

# With authentication
spring.data.mongodb.uri=mongodb://user:password@localhost:27017/tic_tac_toe_db?authSource=admin
`

## ?? Dependencies

- **Spring Boot 3.5.0**
- **Spring Data MongoDB** - MongoDB ORM
- **Lombok** - Code generation
- **Spring Boot Actuator** - Monitoring

## ?? Troubleshooting

**MongoDB Connection Failed?**
- Ensure MongoDB is running: mongosh
- Verify database exists: use tic_tac_toe_db
- Check connection string in pplication.properties

**Maven not found?**
- Download Maven: https://maven.apache.org/download.cgi
- Add Maven bin/ to PATH

**Port 27017 in use?**
- Change MongoDB port: mongod --port 27018
- Update URI: mongodb://localhost:27018/tic_tac_toe_db

**Application won't start?**
- Verify MongoDB is running
- Check Maven build succeeded: mvn clean package
- Review console logs for errors

## ?? Documentation

- **SETUP_GUIDE_MONGODB.md** - Comprehensive MongoDB setup
- **MONGODB_SETUP.md** - Detailed MongoDB configuration
- **MONGODB_MIGRATION.md** - Migration details from MySQL
- **TESTING_GUIDE.md** - Testing procedures
- **IMPLEMENTATION_SUMMARY.md** - Technical details
- **PROJECT_STRUCTURE.md** - File organization

---

**Version**: 2.0 (MongoDB Edition)  
**Last Updated**: August 2026
