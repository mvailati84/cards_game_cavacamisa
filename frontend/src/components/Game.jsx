import { useState } from 'react';
import './Game.css';
import Deck from './cards/Deck';
import PlayArea from './cards/PlayArea';

const Game = () => {
  const [player1Name] = useState('Player 1');
  const [player2Name] = useState('Player 2');
  const [currentTurn] = useState(1); // 1 for Player 1, 2 for Player 2
  const [player1Cards] = useState(20); // Example card count
  const [player2Cards] = useState(20); // Example card count
  const [playedCards] = useState([]); // Example played cards

  return (
    <div className="game-container">
      <div className="player-area player-top">
        <div className="player-name">{player2Name}</div>
        <Deck
          position="top"
          cardCount={player2Cards}
          isPlayerTurn={currentTurn === 2}
        />
      </div>
      
      <div className="game-table">
        <PlayArea playedCards={playedCards} />
      </div>

      <div className="player-area player-bottom">
        <div className="player-name">{player1Name}</div>
        <Deck
          position="bottom"
          cardCount={player1Cards}
          isPlayerTurn={currentTurn === 1}
        />
      </div>
    </div>
  );
};

export default Game;
