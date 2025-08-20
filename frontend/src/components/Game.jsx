import { useState, useEffect, useCallback } from 'react';
import './Game.css';
import Deck from './cards/Deck';
import PlayArea from './cards/PlayArea';
import { GameService } from '../services/gameService';
import { useGameState, useCardAnimation } from '../hooks/gameHooks';
import { 
  getPlayerByIndex, 
  getPlayerAnimationId, 
  canPlayerMove, 
  getPlayersDisplayInfo 
} from '../utils/playerUtils';

const Game = () => {
  const [gameId, setGameId] = useState(null);
  const [initError, setInitError] = useState(null);
  const { gameState, error, isLoading, playCard } = useGameState(gameId);
  const { animatingCards, startCardAnimation } = useCardAnimation();

  // Initialize game with players
  useEffect(() => {
    const initGame = async () => {
      try {
        const game = await GameService.initializeGameWithPlayers();
        setGameId(game.id);
      } catch (err) {
        setInitError(err.message);
      }
    };
    initGame();
  }, []);

  const handleDeckClick = useCallback(async (playerIndex) => {
    if (!canPlayerMove(gameState, playerIndex, isLoading)) return;
    
    const players = gameState?.players || [];
    const player = getPlayerByIndex(players, playerIndex);
    if (!player) return;
    
    try {
      const success = await playCard(player.id);
      if (success) {
        await startCardAnimation(getPlayerAnimationId(playerIndex));
      }
    } catch (error) {
      console.error('Error playing card:', error);
    }
  }, [gameState, isLoading, playCard, startCardAnimation]);

  if (initError) {
    return <div className="error-message">Failed to initialize game: {initError}</div>;
  }

  if (!gameState) {
    return <div className="loading">Loading game...</div>;
  }

  // Extract and map backend data structure to frontend expectations
  const { player1, player2 } = getPlayersDisplayInfo(gameState);
  const playedCards = gameState.tableCards || [];
  const isGameOver = gameState.gameFinished || false;

  return (
    <>
      <div className={`game-container ${isLoading ? 'loading' : ''}`}>
        {error && <div className="error-message">{error}</div>}
        
        {/* Player 2 Panel (Left Side) */}
        <div className={`player-panel player2-panel ${canPlayerMove(gameState, 1, false) ? 'active-turn' : ''}`}>
          <div className="player-avatar">
            {player2.name.charAt(0).toUpperCase()}
          </div>
          <div className="player-name">{player2.name}</div>
          <div className="player-stats">
            <div className="stat-item">
              <span className="stat-label">Cards</span>
              <span className="stat-value">{player2.cardCount}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Score</span>
              <span className="stat-value">0</span>
            </div>
          </div>
          <div 
            className={`turn-indicator ${canPlayerMove(gameState, 1, false) ? 'active' : 'inactive'}`}
            onClick={canPlayerMove(gameState, 1, false) && !isLoading && !isGameOver ? () => handleDeckClick(1) : undefined}
            style={{ cursor: canPlayerMove(gameState, 1, false) && !isLoading && !isGameOver ? 'pointer' : 'default' }}
          >
            {canPlayerMove(gameState, 1, false) ? 'Your Turn' : 'Waiting'}
          </div>
        </div>
        
        {/* Central Game Area */}
        <div className="game-area">
          <div className="game-table">
            <PlayArea 
              playedCards={playedCards}
              isAnimating={animatingCards.size > 0}
            />
            {isGameOver && (
              <div className="game-over-overlay">
                Game Over!
                <div className="winner">
                  {gameState.winner ? `${gameState.winner.name} Wins!` : "It's a tie!"}
                </div>
              </div>
            )}
          </div>
          
          <div className="decks-container">
            <Deck
              position="left"
              cardCount={player2.cardCount}
              isPlayerTurn={canPlayerMove(gameState, 1, false)}
              onClick={() => handleDeckClick(1)}
              disabled={!canPlayerMove(gameState, 1, isLoading) || isGameOver}
              isAnimating={animatingCards.has(getPlayerAnimationId(1))}
            />
            <Deck
              position="right"
              cardCount={player1.cardCount}
              isPlayerTurn={canPlayerMove(gameState, 0, false)}
              onClick={() => handleDeckClick(0)}
              disabled={!canPlayerMove(gameState, 0, isLoading) || isGameOver}
              isAnimating={animatingCards.has(getPlayerAnimationId(0))}
            />
          </div>
        </div>

        {/* Player 1 Panel (Right Side) */}
        <div className={`player-panel player1-panel ${canPlayerMove(gameState, 0, false) ? 'active-turn' : ''}`}>
          <div className="player-avatar">
            {player1.name.charAt(0).toUpperCase()}
          </div>
          <div className="player-name">{player1.name}</div>
          <div className="player-stats">
            <div className="stat-item">
              <span className="stat-label">Cards</span>
              <span className="stat-value">{player1.cardCount}</span>
            </div>
            <div className="stat-item">
              <span className="stat-label">Score</span>
              <span className="stat-value">0</span>
            </div>
          </div>
          <div 
            className={`turn-indicator ${canPlayerMove(gameState, 0, false) ? 'active' : 'inactive'}`}
            onClick={canPlayerMove(gameState, 0, false) && !isLoading && !isGameOver ? () => handleDeckClick(0) : undefined}
            style={{ cursor: canPlayerMove(gameState, 0, false) && !isLoading && !isGameOver ? 'pointer' : 'default' }}
          >
            {canPlayerMove(gameState, 0, false) ? 'Your Turn' : 'Waiting'}
          </div>
        </div>
      </div>

      {/* Loading Overlay - Outside game container for proper positioning */}
      {isLoading && (
        <div className="game-loading-overlay">
          <div className="loading-content">
            <div className="loading-spinner"></div>
            <div>Processing move...</div>
          </div>
        </div>
      )}
    </>
  );
};

export default Game;