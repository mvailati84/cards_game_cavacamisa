// Player utility functions for consistent indexing and data handling

/**
 * Creates a standardized player object from backend data
 * @param {Object} playerData - Player data from backend
 * @param {string} defaultName - Default name if player data is missing
 * @returns {Object} Standardized player object
 */
export const createPlayerObject = (playerData, defaultName) => {
  return playerData ? {
    name: playerData.name,
    id: playerData.id,
    cardCount: playerData.deckSize,
    capturedCards: playerData.capturedCards || 0
  } : { 
    name: defaultName, 
    cardCount: 0, 
    capturedCards: 0 
  };
};

/**
 * Gets player by index from players array
 * @param {Array} players - Array of players
 * @param {number} index - Player index (0-based)
 * @returns {Object|null} Player object or null if not found
 */
export const getPlayerByIndex = (players, index) => {
  return players && players[index] ? players[index] : null;
};

/**
 * Checks if it's a specific player's turn
 * @param {number} currentPlayerIndex - Current player index from game state
 * @param {number} playerIndex - Player index to check
 * @returns {boolean} True if it's the player's turn
 */
export const isPlayerTurn = (currentPlayerIndex, playerIndex) => {
  return currentPlayerIndex === playerIndex;
};

/**
 * Gets animation card ID for a player
 * @param {number} playerIndex - Player index (0-based)
 * @returns {string} Animation card ID
 */
export const getPlayerAnimationId = (playerIndex) => {
  return `${playerIndex}-card`;
};

/**
 * Determines if a player can make a move
 * @param {Object} gameState - Current game state
 * @param {number} playerIndex - Player index to check
 * @param {boolean} isLoading - Whether the game is currently loading
 * @returns {boolean} True if player can make a move
 */
export const canPlayerMove = (gameState, playerIndex, isLoading) => {
  if (!gameState || isLoading || gameState.gameFinished) {
    return false;
  }
  
  return isPlayerTurn(gameState.currentPlayerIndex, playerIndex);
};

/**
 * Gets display information for both players
 * @param {Object} gameState - Current game state
 * @returns {Object} Object containing player1 and player2 display info
 */
export const getPlayersDisplayInfo = (gameState) => {
  const players = gameState?.players || [];
  
  return {
    player1: createPlayerObject(players[0], 'Player 1'),
    player2: createPlayerObject(players[1], 'Player 2')
  };
};