# MongoDB Configuration Guide - Tic Tac Toe Spring Boot

## MongoDB Setup

### 1. Install MongoDB

#### Windows
```bash
# Download and install from: https://www.mongodb.com/try/download/community

# Or use Chocolatey
choco install mongodb-community
```

#### macOS
```bash
# Using Homebrew
brew tap mongodb/brew
brew install mongodb-community
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get install mongodb-server
```

### 2. Start MongoDB Service

#### Windows
```bash
# MongoDB runs as a service
# Verify it's running in Services app
# Or manually start:
mongod --dbpath "C:\data\db"
```

#### macOS/Linux
```bash
# Start MongoDB
brew services start mongodb-community
# Or
mongod
```

### 3. Verify MongoDB Connection

```bash
# Connect to MongoDB
mongosh
# or
mongo

# Check version
db.version()

# Exit
exit
```

### 4. Create Database and Collection

```bash
# Connect to MongoDB
mongosh

# Create and use database
use tic_tac_toe_db

# Create initial collection
db.createCollection("game_records")

# Add indexes for better query performance
db.game_records.createIndex({ "winner": 1 })
db.game_records.createIndex({ "playerXName": 1 })
db.game_records.createIndex({ "playerOName": 1 })
db.game_records.createIndex({ "gameDate": 1 })

# Verify
show collections
db.game_records.getIndexes()

# Exit
exit
```

## Application Configuration

### application.properties

```properties
# MongoDB Connection String
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# Auto create indexes
spring.data.mongodb.auto-index-creation=true
```

### Connection String Format

```
mongodb://[username:password@]host[:port]/[database][?options]
```

Examples:
```
# Local (no auth)
mongodb://localhost:27017/tic_tac_toe_db

# Remote server
mongodb://mongodb.example.com:27017/tic_tac_toe_db

# With authentication
mongodb://user:password@localhost:27017/tic_tac_toe_db

# Replica set
mongodb://host1:27017,host2:27017,host3:27017/tic_tac_toe_db
```

## Docker Setup (Optional)

### Using Docker Compose

Create `docker-compose.yml`:
```yaml
version: '3.8'
services:
  mongodb:
    image: mongo:latest
    container_name: tic-tac-toe-mongodb
    ports:
      - "27017:27017"
    environment:
      MONGO_INITDB_DATABASE: tic_tac_toe_db
    volumes:
      - mongodb_data:/data/db
    restart: unless-stopped

volumes:
  mongodb_data:
```

Start MongoDB:
```bash
docker-compose up -d
```

## Build & Run

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/tic-tac-toe-0.0.1-SNAPSHOT.jar
```

## Accessing MongoDB Data

### Using MongoDB Shell

```bash
# Connect
mongosh

# Use database
use tic_tac_toe_db

# View all records
db.game_records.find()

# View records for specific player
db.game_records.find({ "playerXName": "Alice" })

# View records by winner
db.game_records.find({ "winner": "Alice" })

# Count records
db.game_records.countDocuments()

# Delete records (careful!)
db.game_records.deleteMany({})

# Drop collection
db.game_records.drop()
```

### Using MongoDB Compass

1. Download: https://www.mongodb.com/products/compass
2. Connect to: `mongodb://localhost:27017`
3. Browse database and collections
4. View, edit, and manage records

## Troubleshooting

### MongoDB Connection Refused
```
Error: connect ECONNREFUSED 127.0.0.1:27017

Solution:
1. Verify MongoDB is running
2. Check connection string
3. Verify host and port are correct
```

### Database Not Found
```
Error: database not found

Solution:
1. Collections are created automatically on first write
2. Or manually create using mongosh
```

### Permission Denied
```
Error: authentication failed

Solution:
1. Check MongoDB authentication enabled
2. Update connection string with credentials
```

## Connection String in application.properties

```properties
# For remote MongoDB Atlas
spring.data.mongodb.uri=mongodb+srv://user:password@cluster.mongodb.net/tic_tac_toe_db?retryWrites=true&w=majority

# For local MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# With authentication
spring.data.mongodb.uri=mongodb://user:password@localhost:27017/tic_tac_toe_db?authSource=admin
```

## Dependencies

- **spring-boot-starter-data-mongodb** - Spring Data MongoDB
- **lombok** - Code generation

## Benefits of MongoDB

✅ NoSQL Document Database
✅ Flexible schema (no migrations needed)
✅ Automatic ID generation
✅ Built-in indexing
✅ Easy horizontal scaling
✅ Great for game records
✅ JSON-like document structure

---

**Version**: 1.0  
**Date**: August 9, 2026
