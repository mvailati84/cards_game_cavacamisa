# Backend Unit Tests Summary

This document provides an overview of the comprehensive unit test suite created for the Cavacamisa card game backend.

## Test Coverage

The test suite covers all major components of the backend:

### 1. Model Classes

#### CardTest.java
- **Purpose**: Tests the Card model class
- **Coverage**: 
  - Card creation with valid/invalid parameters
  - Winning card identification (Asso, Due, Tre)
  - Cards to play calculation
  - Display name generation for all card types
  - Equality and hashCode methods
  - String representation

#### PlayerTest.java
- **Purpose**: Tests the Player model class
- **Coverage**:
  - Player creation and basic properties
  - Deck management (add, draw, capture cards)
  - Win/loss condition detection
  - Defensive copying of collections
  - Multiple draw operations
  - String representation

#### GameTest.java
- **Purpose**: Tests the Game model class (most complex)
- **Coverage**:
  - Game initialization and state management
  - Player addition and game start logic
  - Card playing mechanics
  - Winning card handling (Asso, Due, Tre)
  - Obligation to play cards
  - **Game ending when player has no cards** (new feature)
  - Winner/loser identification
  - Player turn rotation
  - Defensive copying of collections
  - String representation

### 2. DTO Classes

#### CardDtoTest.java
- **Purpose**: Tests the CardDto data transfer object
- **Coverage**:
  - Conversion from Card model to DTO
  - Property mapping for all card types
  - Default constructor behavior
  - Setter/getter methods
  - Display name handling

#### PlayerDtoTest.java
- **Purpose**: Tests the PlayerDto data transfer object
- **Coverage**:
  - Conversion from Player model to DTO
  - Deck information mapping
  - Default constructor behavior
  - Setter/getter methods
  - Defensive copying

#### GameDtoTest.java
- **Purpose**: Tests the GameDto data transfer object
- **Coverage**:
  - Conversion from Game model to DTO
  - Player and table card mapping
  - Winner/loser identification
  - Game state representation
  - Default constructor behavior
  - Setter/getter methods

### 3. Service Classes

#### GameServiceTest.java
- **Purpose**: Tests the GameService business logic
- **Coverage**:
  - Game creation and management
  - Player creation and management
  - Game joining logic
  - Card playing through service layer
  - Error handling for invalid operations
  - Game and player deletion
  - Existence checking methods

## Key Test Scenarios

### Game Flow Testing
1. **Game Creation**: Verify games are created with correct initial state
2. **Player Joining**: Test single and dual player joining
3. **Game Start**: Verify game transitions to playing state when 2 players join
4. **Card Playing**: Test normal card playing and turn rotation
5. **Winning Cards**: Test Asso, Due, and Tre card mechanics
6. **Obligations**: Test forced card playing mechanics
7. **Game End**: Test both win conditions (all cards collected and no cards left)

### Error Handling Testing
1. **Invalid Operations**: Test attempts to play out of turn
2. **Non-existent Resources**: Test accessing non-existent games/players
3. **Full Games**: Test attempts to join full games
4. **Invalid States**: Test operations on games in wrong states

### Edge Cases Testing
1. **Empty Decks**: Test behavior when players run out of cards
2. **Collection Defensive Copying**: Ensure internal state is protected
3. **Null Handling**: Test proper handling of null values
4. **Boundary Conditions**: Test edge cases in card ranks and game states

## Test Structure

All tests follow these conventions:
- **Naming**: Descriptive test method names using `should...` pattern
- **Annotations**: `@DisplayName` for readable test descriptions
- **Setup**: `@BeforeEach` for test initialization
- **Assertions**: Comprehensive assertions using JUnit 5
- **Parameterized Tests**: Used for testing multiple similar scenarios

## Running the Tests

The tests can be run using:
```bash
# Using Maven (if available)
mvn test

# Using Docker (recommended)
docker-compose -f ../docker-compose.dev.yml up --build
```

## Test Quality

- **Comprehensive Coverage**: All major classes and methods are tested
- **Edge Case Handling**: Tests cover boundary conditions and error scenarios
- **Readable Tests**: Clear test names and descriptions
- **Maintainable**: Well-structured tests that are easy to understand and modify
- **Isolated**: Each test is independent and doesn't rely on other tests

## New Feature Testing

The test suite specifically includes tests for the new game ending logic:
- When a player has no cards left, the game immediately ends
- The other player is declared the winner
- The game state transitions to FINISHED
- Winner and loser are correctly identified

This ensures the new functionality works correctly and doesn't break existing game mechanics.
