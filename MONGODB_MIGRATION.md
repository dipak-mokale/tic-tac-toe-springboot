# MongoDB Migration Guide - Tic Tac Toe Spring Boot

## Overview

Successfully migrated the Tic Tac Toe application from **MySQL** to **MongoDB**.

## Changes Summary

### Dependency Changes

#### Removed
```xml
<!-- JPA for relational databases -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL JDBC Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

#### Added
```xml
<!-- MongoDB Spring Data -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

### Configuration Changes

#### MySQL (Before)
```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/tic_tac_toe_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true
```

#### MongoDB (After)
```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb://localhost:27017/tic_tac_toe_db

# Logging
spring.data.mongodb.auto-index-creation=true
```

### Entity Changes

#### GameRecord.java

**Before (JPA/Hibernate)**
```java
@Entity
@Table(name = "game_records")
@Data
public class GameRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String playerXName;
    
    @Column(nullable = false)
    private String playerOName;
    
    @Column(nullable = false)
    private String winner;
    
    @Column(nullable = false)
    private Integer moves;
    
    @Column(nullable = false)
    private LocalDateTime gameDate;
}
```

**After (MongoDB)**
```java
@Document(collection = "game_records")
@Data
public class GameRecord {
    @Id
    private String id;
    
    private String playerXName;
    private String playerOName;
    private String winner;
    private Integer moves;
    private LocalDateTime gameDate;
}
```

**Key Changes:**
- `@Entity` → `@Document`
- `@Table` → `@Document(collection = ...)`
- `@Column` annotations removed (not needed in MongoDB)
- `Long` ID → `String` ID (MongoDB uses ObjectId as String)
- No `@GeneratedValue` (MongoDB handles ID generation)
- No null constraints (MongoDB is schema-less)

### Repository Changes

#### GameRecordRepository.java

**Before (JPA)**
```java
@Repository
public interface GameRecordRepository extends JpaRepository<GameRecord, Long> {
    List<GameRecord> findByWinner(String winner);
    List<GameRecord> findByPlayerXNameOrPlayerOName(String playerXName, String playerOName);
}
```

**After (MongoDB)**
```java
@Repository
public interface GameRecordRepository extends MongoRepository<GameRecord, String> {
    List<GameRecord> findByWinner(String winner);
    List<GameRecord> findByPlayerXNameOrPlayerOName(String playerXName, String playerOName);
}
```

**Changes:**
- `JpaRepository` → `MongoRepository`
- ID type: `Long` → `String`
- Query methods remain the same (Spring Data handles the mapping)

### No Changes Required

The following files work seamlessly with MongoDB without modifications:

✅ **GameService.java** - No changes needed
- Repository injection still works
- Query methods have same signature
- Save logic unchanged

✅ **GameController.java** - No changes needed
- API endpoints remain identical
- Response format unchanged
- Error handling unchanged

✅ **Frontend (HTML, JS, CSS)** - No changes needed
- API calls unchanged
- Data format unchanged
- User interface identical

## Benefits of MongoDB Migration

### 1. **Flexible Schema**
- No schema migrations needed
- Add fields to documents on-the-fly
- Different records can have different structures

### 2. **Better Performance**
- No complex joins
- Faster reads for game records
- Horizontal scaling capabilities

### 3. **Easier Development**
- Natural JSON document mapping
- Simple query syntax
- No ORM overhead

### 4. **Scalability**
- Built-in sharding support
- Replica sets for high availability
- Horizontal scaling options

### 5. **Developer Experience**
- Less configuration
- Automatic collection creation
- Flexible indexing

## Data Structure

### MySQL (Before)
```sql
CREATE TABLE game_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_x_name VARCHAR(255) NOT NULL,
    player_o_name VARCHAR(255) NOT NULL,
    winner VARCHAR(255) NOT NULL,
    moves INT NOT NULL,
    game_date TIMESTAMP NOT NULL,
    INDEX idx_winner (winner),
    INDEX idx_player_x (player_x_name),
    INDEX idx_player_o (player_o_name),
    INDEX idx_game_date (game_date)
);
```

### MongoDB (After)
```json
db.game_records
├── Document 1
│   ├── _id: ObjectId("...")
│   ├── playerXName: "Alice"
│   ├── playerOName: "Bob"
│   ├── winner: "Alice"
│   ├── moves: 5
│   └── gameDate: ISODate("2026-08-09T18:30:00Z")
│
└── Document 2
    ├── _id: ObjectId("...")
    ├── playerXName: "Charlie"
    ├── playerOName: "Diana"
    ├── winner: "DRAW"
    ├── moves: 9
    └── gameDate: ISODate("2026-08-09T18:35:00Z")
```

## Migration Path (if upgrading existing app)

### Option 1: Fresh Install
```bash
1. Delete MongoDB database: db.dropDatabase()
2. Start application fresh
3. Records created on first game
```

### Option 2: Import Existing Data
```bash
# Export from MySQL as JSON
SELECT * FROM game_records;

# Import to MongoDB
mongoimport --db tic_tac_toe_db --collection game_records --jsonArray < records.json
```

## Operational Commands

### Create Indexes
```bash
mongosh

use tic_tac_toe_db

db.game_records.createIndex({ "winner": 1 })
db.game_records.createIndex({ "playerXName": 1 })
db.game_records.createIndex({ "playerOName": 1 })
db.game_records.createIndex({ "gameDate": 1 })
```

### View Records
```bash
# All records
db.game_records.find()

# By player
db.game_records.find({ "playerXName": "Alice" })

# By winner
db.game_records.find({ "winner": "Alice" })

# Count
db.game_records.countDocuments()
```

### Delete Records
```bash
# Delete all records
db.game_records.deleteMany({})

# Delete by player
db.game_records.deleteMany({ "playerXName": "Alice" })

# Delete by winner
db.game_records.deleteMany({ "winner": "Alice" })
```

## API Compatibility

All API endpoints remain unchanged:

```
GET    /api/game
GET    /api/game/records
GET    /api/game/records/winner/{name}
GET    /api/game/records/player/{name}
POST   /api/game/initialize
POST   /api/game/move
POST   /api/game/reset
```

Request/Response formats are identical.

## Performance Comparison

| Aspect | MySQL | MongoDB |
|--------|-------|---------|
| Query Speed | Good | Excellent (No Joins) |
| Schema Flexibility | Rigid | Flexible |
| Horizontal Scaling | Complex | Native |
| Setup Complexity | Medium | Simple |
| Index Support | Yes | Yes |
| Transaction Support | ACID | Multi-doc transactions |

## Troubleshooting

### Common Issues

**Problem**: Connection refused
```
Error: connect ECONNREFUSED 127.0.0.1:27017
```
**Solution**: Ensure MongoDB is running
```bash
mongosh  # Should connect successfully
```

**Problem**: Database not found
```
Error: database not found
```
**Solution**: Collections are auto-created on first write, or manually create:
```bash
mongosh
use tic_tac_toe_db
db.createCollection("game_records")
```

**Problem**: Port 27017 already in use
```
Error: Address already in use
```
**Solution**: Check if MongoDB is already running, or use different port:
```properties
spring.data.mongodb.uri=mongodb://localhost:27018/tic_tac_toe_db
```

## Rollback to MySQL (if needed)

If you need to rollback to MySQL:

1. **Restore pom.xml**:
```xml
<!-- Add back -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

2. **Restore application.properties**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tic_tac_toe_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

3. **Restore GameRecord.java**: Use JPA annotations
4. **Restore GameRecordRepository.java**: Extend JpaRepository
5. **Rebuild**: `mvn clean package`

## Documentation Files

- `SETUP_GUIDE_MONGODB.md` - MongoDB setup instructions
- `MONGODB_SETUP.md` - Detailed MongoDB configuration
- `README.md` - Updated project documentation
- `TESTING_GUIDE.md` - Testing procedures (unchanged)

---

**Migration Status**: ✅ COMPLETE  
**Version**: 2.0 (MongoDB Edition)  
**Date**: August 9, 2026
