import Card from './Card';
import './PlayArea.css';

const PlayArea = ({ playedCards = [] }) => {
  return (
    <div className="play-area">
      <div className="played-cards-stack">
        {playedCards.map((card, index) => (
          <div
            key={index}
            className="played-card"
            style={{
              transform: `rotate(${Math.random() * 10 - 5}deg) translate(${index * 2}px, ${index * 2}px)`,
              zIndex: index
            }}
          >
            <Card
              suit={card.suit}
              value={card.value}
              isFaceUp={true}
            />
          </div>
        ))}
      </div>
      {playedCards.length === 0 && (
        <div className="play-area-placeholder">
          Play Area
        </div>
      )}
    </div>
  );
};

export default PlayArea;
