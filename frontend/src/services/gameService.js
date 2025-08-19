const API_BASE_URL = import.meta.env.VITE_API_URL;

export const GameService = {
  async createPlayer(name) {
    try {
      const response = await fetch(`${API_BASE_URL}/game/player`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ name })
      });
      if (!response.ok) throw new Error('Failed to create player');
      return await response.json();
    } catch (error) {
      throw new Error(`Error creating player: ${error.message}`);
    }
  },

  async joinGame(gameId, playerId) {
    try {
      const response = await fetch(`${API_BASE_URL}/game/${gameId}/join`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ playerId })
      });
      if (!response.ok) throw new Error('Failed to join game');
      return await response.json();
    } catch (error) {
      throw new Error(`Error joining game: ${error.message}`);
    }
  },

  async initializeGame() {
    try {
      const response = await fetch(`${API_BASE_URL}/game`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      if (!response.ok) throw new Error('Failed to initialize game');
      return await response.json();
    } catch (error) {
      throw new Error(`Error initializing game: ${error.message}`);
    }
  },

  async initializeGameWithPlayers() {
    try {
      // Step 1: Create the game
      const game = await this.initializeGame();
      
      // Step 2: Create two players
      const player1 = await this.createPlayer('Player 1');
      const player2 = await this.createPlayer('Player 2');
      
      // Step 3: Join both players to the game
      await this.joinGame(game.id, player1.id);
      const finalGameState = await this.joinGame(game.id, player2.id);
      
      return finalGameState;
    } catch (error) {
      throw new Error(`Error initializing game with players: ${error.message}`);
    }
  },

  async playCard(gameId, playerId) {
    try {
      const playerIdStr = playerId.toString();

      const response = await fetch(`${API_BASE_URL}/game/${gameId}/play`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ playerId: playerIdStr })
      });
      if (!response.ok) throw new Error('Failed to play card');
      return await response.json();
    } catch (error) {
      throw new Error(`Error playing card: ${error.message}`);
    }
  },

  async getGameState(gameId) {
    try {
      const response = await fetch(`${API_BASE_URL}/game/${gameId}`);
      if (!response.ok) throw new Error('Failed to fetch game state');
      return await response.json();
    } catch (error) {
      throw new Error(`Error fetching game state: ${error.message}`);
    }
  }
};

export const POLLING_INTERVAL = 2000; // 2 seconds
