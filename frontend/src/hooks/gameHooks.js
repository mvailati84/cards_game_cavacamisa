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
      
      // Log game state updates with card information
      if (state) {
        console.log('🎮 Game state updated:', {
          gameState: state.state,
          currentPlayer: state.currentPlayerIndex,
          playedCards: state.playedCards,
          players: state.players?.map(p => ({
            id: p.id,
            name: p.name,
            deckSize: p.deck?.length || 0,
            capturedCardsCount: p.capturedCards?.length || 0
          }))
        });
        
        // Log specific details about played cards
        if (state.playedCards && state.playedCards.length > 0) {
          console.log('🃏 Cards currently in play:', state.playedCards.map(card => 
            `${card.rank || card.value} of ${card.suit}`
          ));
        }
      }
      
      setGameState(state);
      setError(null);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, [gameId]);

  // Initial fetch only (no polling)
  useEffect(() => {
    fetchGameState();
  }, [fetchGameState]);

  const playCard = useCallback(async (playerId) => {
    if (!gameId || !playerId) {
      setError('Invalid game or player ID');
      return false;
    }
    
    console.log('🎴 Playing card for player:', playerId);
    
    setIsLoading(true);
    try {
      const result = await GameService.playCard(gameId, playerId);
      console.log('🎴 Card played successfully:', result.tableCards[result.tableCards.length - 1]);
      
      await fetchGameState(); // Refresh game state after playing
      return true;
    } catch (err) {
      console.error('❌ Error playing card:', err.message);
      setError(err.message);
      return false;
    } finally {
      setIsLoading(false);
    }
  }, [gameId, fetchGameState]);

  return {
    gameState,
    error,
    isLoading,
    playCard,
    refreshGameState: fetchGameState
  };
};

export const useCardAnimation = (animationDuration = 500) => {
  const [animatingCards, setAnimatingCards] = useState(new Set());

  const startCardAnimation = useCallback((cardId) => {
    setAnimatingCards(prev => new Set([...prev, cardId]));
    return new Promise(resolve => {
      setTimeout(() => {
        setAnimatingCards(prev => {
          const next = new Set(prev);
          next.delete(cardId);
          return next;
        });
        resolve();
      }, animationDuration);
    });
  }, [animationDuration]);

  const clearAllAnimations = useCallback(() => {
    setAnimatingCards(new Set());
  }, []);

  const isCardAnimating = useCallback((cardId) => {
    return animatingCards.has(cardId);
  }, [animatingCards]);

  return {
    animatingCards,
    startCardAnimation,
    clearAllAnimations,
    isCardAnimating
  };
};
