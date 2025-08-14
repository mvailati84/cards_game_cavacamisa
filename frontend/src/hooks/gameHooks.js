import { useState, useEffect, useCallback } from 'react';
import { GameService, POLLING_INTERVAL } from '../services/gameService';

export const useGameState = (gameId) => {
  const [gameState, setGameState] = useState(null);
  const [error, setError] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchGameState = useCallback(async () => {
    if (!gameId) return;
    try {
      const state = await GameService.getGameState(gameId);
      setGameState(state);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, [gameId]);

  // Initial fetch and polling setup
  useEffect(() => {
    fetchGameState();
    
    const pollInterval = setInterval(fetchGameState, POLLING_INTERVAL);
    
    return () => clearInterval(pollInterval);
  }, [fetchGameState]);

  const playCard = async (playerId) => {
    setIsLoading(true);
    try {
      await GameService.playCard(gameId, playerId);
      await fetchGameState(); // Refresh game state after playing
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return {
    gameState,
    error,
    isLoading,
    playCard,
    refreshGameState: fetchGameState
  };
};

export const useCardAnimation = () => {
  const [animatingCards, setAnimatingCards] = useState(new Set());

  const startCardAnimation = (cardId) => {
    setAnimatingCards(prev => new Set([...prev, cardId]));
    return new Promise(resolve => {
      setTimeout(() => {
        setAnimatingCards(prev => {
          const next = new Set(prev);
          next.delete(cardId);
          return next;
        });
        resolve();
      }, 500); // Animation duration
    });
  };

  return {
    animatingCards,
    startCardAnimation
  };
};
