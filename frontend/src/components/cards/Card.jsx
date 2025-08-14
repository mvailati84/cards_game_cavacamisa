import { useState } from 'react';
import './Card.css';

const SUITS = {
  COPPE: '🏆',  // Cups
  SPADE: '⚔️',  // Swords
  BASTONI: '🏑', // Clubs/Batons
  ORI: '🪙'     // Coins
};

const Card = ({ 
  suit, 
  value, 
  isFaceUp = false, 
  isPlayable = false,
  onClick,
  className = '',
  style = {}
}) => {
  const [isHovered, setIsHovered] = useState(false);

  const handleClick = () => {
    if (isPlayable && onClick) {
      onClick();
    }
  };

  return (
    <div
      className={`card ${isFaceUp ? 'face-up' : 'face-down'} 
                 ${isPlayable ? 'playable' : ''} 
                 ${isHovered ? 'hovered' : ''} 
                 ${className}`}
      style={{
        ...style,
        transform: isHovered ? 'translateY(-10px)' : style.transform || 'none'
      }}
      onMouseEnter={() => isPlayable && setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={handleClick}
    >
      <div className="card-inner">
        <div className="card-front">
          <div className="card-corner top-left">
            <div className="card-value">{value}</div>
            <div className="card-suit">{SUITS[suit]}</div>
          </div>
          <div className="card-center">
            <div className="card-suit large">{SUITS[suit]}</div>
          </div>
          <div className="card-corner bottom-right">
            <div className="card-value">{value}</div>
            <div className="card-suit">{SUITS[suit]}</div>
          </div>
        </div>
        <div className="card-back">
          <div className="card-pattern"></div>
        </div>
      </div>
    </div>
  );
};

export default Card;
