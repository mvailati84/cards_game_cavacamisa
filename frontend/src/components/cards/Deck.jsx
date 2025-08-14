import Card from './Card';
import './Deck.css';

const Deck = ({ 
  position = 'bottom',
  cardCount = 0,
  isPlayerTurn = false,
  onClick,
  disabled = false,
  isAnimating = false
}) => {
  const stackSize = Math.min(cardCount, 5); // Show max 5 stacked cards
  
  return (
    <div 
      className={`deck-area ${position} ${isPlayerTurn ? 'active-turn' : ''} ${disabled ? 'disabled' : ''}`}
      onClick={!disabled ? onClick : undefined}
    >
      <div className="deck-stack">
        {[...Array(stackSize)].map((_, index) => (
          <div
            key={index}
            className="deck-card"
            style={{
              transform: `translateY(${index * -1}px)`,
              zIndex: stackSize - index
            }}
          >
            <Card 
              isFaceUp={false}
              isPlayable={!disabled && isPlayerTurn}
              isAnimating={isAnimating && index === stackSize - 1}
            />
          </div>
        ))}
      </div>
      {cardCount > 0 && (
        <div className="card-count">
          {cardCount} {cardCount === 1 ? 'card' : 'cards'}
        </div>
      )}
    </div>
  );
};

export default Deck;
