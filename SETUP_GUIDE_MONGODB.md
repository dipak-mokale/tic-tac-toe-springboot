# Tic Tac Toe Spring Boot - MongoDB Setup & Configuration Guide

## Overview
This application has been configured to use:
- MongoDB as the NoSQL database
- Spring Data MongoDB for ORM
- Connection string: `mongodb://localhost:27017/tic_tac_toe_db`

## Prerequisites
1. **Java 17+** (Verify: `java -version`)
2. **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
3. **MongoDB Server** ([Download](https://www.mongodb.com/try/download/community))

## Step-by-Step Setup

### 1. Install MongoDB
```bash
# Windows: Download installer or use Chocolatey
choco install mongodb-community

# macOS: Use Homebrew
brew tap mongodb/brew
brew install mongodb-community

# Linux: Use package manager
sudo apt-get install mongodb-server
```

### 2. Start MongoDB Service
```bash
# Windows
mongod --dbpath "C:\data\db"

# macOS/Linux
brew services start mongodb-community
# or
mongod
```

### 3. Create Database and Collection
```bash
# Connect to MongoDB
mongosh

# Create and use database
use tic_tac_toe_db

# Create collection and indexes
db.createCollection("game_records")
db.game_records.createIndex({ "winner": 1 })
db.game_records.createIndex({ "playerXName": 1 })
db.game_records.createIndex({ "playerOName": 1 })
db.game_records.createIndex({ "gameDate": 1 })

# Exit
exit
```

### 4. Verify MongoDB Connection
```bash
mongosh
# In MongoDB shell:
use tic_tac_toe_db
db.stats()
# Should show database stats
exit
```

### 5. Configure Application
The connection is pre-configured in `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db
```

**To use a different connection:**
Edit `src/main/resources/application.properties`:
```properties
# Local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# Remote MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://user:password@cluster.mongodb.net/tic_tac_toe_db

# With authentication
spring.data.mongodb.uri=mongodb://user:password@localhost:27017/tic_tac_toe_db?authSource=admin
```

### 6. Build Application
```bash
cd tic-tac-toe-springboot
mvn clean package
```

### 7. Run Application
```bash
java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
```

Application will start on: **http://localhost:8080**

## Features

### Player Name Input
- Enter custom names for both players (X and O)
- Click "Start Game" to begin
- Player names are displayed during the game and stored in records

### Game Records
- All completed games are automatically saved to MongoDB
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

## MongoDB Shell Commands

```bash
# Connect to MongoDB
mongosh

# Show databases
show databases

# Use database
use tic_tac_toe_db

# Show collections
show collections

# Find all game records
db.game_records.find()

# Pretty print
db.game_records.find().pretty()

# Find by player
db.game_records.find({ "playerXName": "Alice" })

# Find by winner
db.game_records.find({ "winner": "Alice" })

# Count records
db.game_records.countDocuments()

# Delete all records
db.game_records.deleteMany({})

# Drop collection
db.game_records.drop()

# Exit
exit
```

## MongoDB Compass (GUI)

1. Download: https://www.mongodb.com/products/compass
2. Connect to: `mongodb://localhost:27017`
3. Browse and manage data visually

## Database Collections

### game_records Collection

Each document contains:
```json
{
  "_id": "ObjectId",
  "playerXName": "Alice",
  "playerOName": "Bob",
  "winner": "Alice",
  "moves": 5,
  "gameDate": "2026-08-09T18:30:00"
}
```

**Fields:**
- `_id`: MongoDB Object ID (auto-generated)
- `playerXName`: Player X's name
- `playerOName`: Player O's name
- `winner`: Winner's name or "DRAW"
- `moves`: Total number of moves
- `gameDate`: ISO timestamp of game

## Troubleshooting

### MongoDB Connection Failed
- **Error**: `MongoServerSelectionException: No servers chosen from cluster`
- **Solution**:
  1. Verify MongoDB is running: `mongosh`
  2. Check connection string in `application.properties`
  3. Ensure host and port are correct (default: localhost:27017)
  4. Check network connectivity

### Database Not Found
- **Error**: Database doesn't exist
- **Solution**:
  - Collections are created automatically on first write
  - Or manually create: `use tic_tac_toe_db; db.createCollection("game_records")`

### Permission Issues
- **Error**: `authentication failed`
- **Solution**:
  1. Update connection string with credentials
  2. Use format: `mongodb://user:password@localhost:27017/tic_tac_toe_db`
  3. Add `?authSource=admin` if using admin user

### Port Already in Use
- **Error**: `Address already in use`
- **Solution**:
  1. MongoDB uses port 27017 by default
  2. Start MongoDB on different port: `mongod --port 27018`
  3. Update `application.properties`: `mongodb://localhost:27018/tic_tac_toe_db`

### Application Won't Start
- **Error**: `Failed to connect to MongoDB`
- **Solution**:
  1. Check Maven build succeeded: `mvn clean package`
  2. Ensure all dependencies downloaded
  3. Verify MongoDB connection string
  4. Check logs for detailed error

## Docker Setup (Optional)

### Quick MongoDB with Docker

```bash
# Run MongoDB container
docker run -d -p 27017:27017 --name tic-tac-toe-mongodb mongo

# Connect to container
docker exec -it tic-tac-toe-mongodb mongosh

# Stop container
docker stop tic-tac-toe-mongodb
```

### Docker Compose

Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:latest
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_DATABASE: tic_tac_toe_db
    volumes:
      - mongodb_data:/data/db

volumes:
  mongodb_data:
```

Start:
```bash
docker-compose up -d
```

## Dependencies Added

- **spring-boot-starter-data-mongodb** - MongoDB ORM
- **lombok** - Code generation

## Modified Files

- `pom.xml` - Updated dependencies (JPA → MongoDB)
- `src/main/resources/application.properties` - MongoDB configuration
- `src/main/java/.../model/GameRecord.java` - Changed to MongoDB Document
- `src/main/java/.../repository/GameRecordRepository.java` - Changed to MongoRepository

## Performance Tips

1. **Indexes**: Already created on commonly queried fields
2. **Connection Pooling**: Handled automatically by Spring
3. **Batch Operations**: Use bulk writes for many inserts
4. **Caching**: Consider Redis for frequently accessed records

## Next Steps

1. Verify MongoDB is installed and running
2. Create the database and collection
3. Build the application: `mvn clean package`
4. Run the application: `java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar`
5. Access at: http://localhost:8080

## Additional Resources

- MongoDB Documentation: https://docs.mongodb.com/
- Spring Data MongoDB: https://spring.io/projects/spring-data-mongodb
- MongoDB Shell: https://www.mongodb.com/docs/mongodb-shell/
- MongoDB Compass: https://www.mongodb.com/products/compass

---

**Version**: 2.0 (MongoDB Edition)  
**Last Updated**: August 9, 2026
