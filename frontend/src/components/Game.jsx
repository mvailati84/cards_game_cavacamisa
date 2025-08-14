import { useState, useEffect, useCallback } from 'react';
import './Game.css';
import Deck from './cards/Deck';
import PlayArea from './cards/PlayArea';
import { GameService } from '../services/gameService';
import { useGameState, useCardAnimation } from '../hooks/gameHooks';

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

  const handleDeckClick = useCallback(async (playerId) => {
    if (!gameState || isLoading || gameState.currentPlayer !== playerId) return;
    
    try {
      await playCard(playerId);
      await startCardAnimation(`${playerId}-card`);
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
  const players = gameState.players || [];
  const player1 = players[0] ? { 
    name: players[0].name, 
    cardCount: players[0].deckSize 
  } : { name: 'Player 1', cardCount: 0 };
  const player2 = players[1] ? { 
    name: players[1].name, 
    cardCount: players[1].deckSize 
  } : { name: 'Player 2', cardCount: 0 };
  const currentPlayer = gameState.currentPlayerIndex + 1; // Backend uses 0-based index, frontend uses 1-based
  const playedCards = gameState.tableCards || [];
  const isGameOver = gameState.gameFinished || false;

  return (
    <div className={`game-container ${isLoading ? 'loading' : ''}`}>
      {error && <div className="error-message">{error}</div>}
      
      <div className="player-area player-top">
        <div className="player-name">{player2.name}</div>
        <Deck
          position="top"
          cardCount={player2.cardCount}
          isPlayerTurn={currentPlayer === 2}
          onClick={() => handleDeckClick(2)}
          disabled={currentPlayer !== 2 || isLoading || isGameOver}
          isAnimating={animatingCards.has('2-card')}
        />
      </div>
      
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

      <div className="player-area player-bottom">
        <div className="player-name">{player1.name}</div>
        <Deck
          position="bottom"
          cardCount={player1.cardCount}
          isPlayerTurn={currentPlayer === 1}
          onClick={() => handleDeckClick(1)}
          disabled={currentPlayer !== 1 || isLoading || isGameOver}
          isAnimating={animatingCards.has('1-card')}
        />
      </div>

      {isLoading && <div className="loading-overlay">Processing move...</div>}
    </div>
  );
};

export default Game;
