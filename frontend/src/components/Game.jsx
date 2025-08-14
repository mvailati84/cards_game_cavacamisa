import { useState } from 'react';
import './Game.css';

const Game = () => {
  const [player1Name] = useState('Player 1');
  const [player2Name] = useState('Player 2');

  return (
    <div className="game-container">
      <div className="player-area player-top">
        <div className="player-name">{player2Name}</div>
        <div className="player-deck"></div>
      </div>
      
      <div className="game-table">
        {/* Center table area for played cards */}
        <div className="table-center"></div>
      </div>

      <div className="player-area player-bottom">
        <div className="player-name">{player1Name}</div>
        <div className="player-deck"></div>
      </div>
    </div>
  );
};

export default Game;
