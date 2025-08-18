import { useState, useMemo } from 'react';
import './Card.css';

// Mapping suits to sprite rows (0-indexed)
const SUIT_TO_ROW = {
  Ori: 0,      // First row: Ori
  Coppe: 1,    // Second row: Coppe
  Bastoni: 2,  // Third row: Bastoni
  Spade: 3     // Fourth row: Spade
};

// Mapping card values to sprite columns (0-indexed)
const VALUE_TO_COL = {
  1: 0,   // Ace
  2: 1,   // 2
  3: 2,   // 3
  4: 3,   // 4
  5: 4,   // 5
  6: 5,   // 6
  7: 6,   // 7
  8: 7,   // 8 (Fante)
  9: 8,   // 9 (Cavallo)
  10: 9   // 10 (Re)
};

// Function to get sprite position for a card
const getSpritePosition = (suit, value) => {
  const row = SUIT_TO_ROW[suit];
  const col = VALUE_TO_COL[value];
  
  if (row === undefined || col === undefined) {
    console.warn(`Invalid card: suit=${suit}, value=${value}`);
    return null;
  }
  
  // Calculate background position as percentage
  // For a 10-column sprite sheet, each card is 10% width
  // For a 4-row sprite sheet, each card is 25% height
  const xPercent = col * (100 / 9); // 9 gaps between 10 columns
  const yPercent = row * (100 / 3); // 3 gaps between 4 rows

  console.log(`Card background position: xPercent=${xPercent}, yPercent=${yPercent}`);
  
  return {
    backgroundPosition: `${xPercent}% ${yPercent}%`
  };
};

const Card = ({ 
  suit, 
  value, 
  rank, // Backend sends rank, frontend previously used value
  isFaceUp = false, 
  isPlayable = false,
  isAnimating = false,
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

  // Use rank if available (from backend), otherwise fall back to value
  const cardValue = rank || value;

  // Memoize sprite position calculation to avoid recomputing on every render
  const spritePosition = useMemo(() => {
    if (!suit || !cardValue) {
      console.log('Missing card data:', { suit, cardValue, rank, value });
      return null;
    }
    console.log('Calculating sprite position for:', { suit, cardValue });
    return getSpritePosition(suit, cardValue);
  }, [suit, cardValue]);

  return (
    <div
      className={`card ${isFaceUp ? 'face-up' : 'face-down'} 
                 ${isPlayable ? 'playable' : ''} 
                 ${isHovered ? 'hovered' : ''} 
                 ${isAnimating ? 'animating' : ''}
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
        <div 
          className="card-front with-sprite"
          style={isFaceUp && spritePosition ? spritePosition : {}}
        >
        </div>
        <div className="card-back">
          <div className="card-pattern"></div>
        </div>
      </div>
    </div>
  );
};

export default Card;
