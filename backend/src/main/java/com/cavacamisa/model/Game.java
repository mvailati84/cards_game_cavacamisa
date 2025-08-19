package com.cavacamisa.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Game {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);
    private final String id;
    private final List<Player> players;
    private final List<Card> tableCards; // Cards on the table
    private GameState state;
    private int currentPlayerIndex;
    private int cardsToPlay; // Number of cards the current player must play
    private Player lastWinningPlayer; // Last player who played a winning card
    private final List<Card> deck; // Full deck of 40 cards

    public Game(String id) {
        this.id = id;
        this.players = new ArrayList<>();
        this.tableCards = new ArrayList<>();
        this.state = GameState.WAITING_FOR_PLAYERS;
        this.currentPlayerIndex = 0;
        this.cardsToPlay = 0;
        this.lastWinningPlayer = null;
        this.deck = createDeck();
    }

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (int rank = 1; rank <= 10; rank++) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    public GameState getState() {
        return state;
    }

    public List<Card> getTableCards() {
        return new ArrayList<>(tableCards);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) return null;
        return players.get(currentPlayerIndex);
    }

    public int getCardsToPlay() {
        return cardsToPlay;
    }

    public Player getLastWinningPlayer() {
        return lastWinningPlayer;
    }

    public boolean addPlayer(Player player) {
        if (players.size() >= 2) {
            return false; // Only 2 players allowed
        }
        players.add(player);
        if (players.size() == 2) {
            state = GameState.DEALING;
            dealCards();
            state = GameState.PLAYING;
        }
        return true;
    }

    private void dealCards() {
        // Shuffle the deck
        Collections.shuffle(deck);
        
        // Deal cards equally to players (20 each)
        int cardsPerPlayer = deck.size() / players.size();
        for (int i = 0; i < players.size(); i++) {
            List<Card> playerCards = new ArrayList<>();
            for (int j = 0; j < cardsPerPlayer; j++) {
                playerCards.add(deck.get(i * cardsPerPlayer + j));
            }
            players.get(i).addCardsToDeck(playerCards);
        }
        
        // Clear the deck after dealing
        deck.clear();
    }

    public boolean playCard(String playerId) {
        logger.info("Attempting to play card for player: {}", playerId);
        
        if (state != GameState.PLAYING) {
            logger.warn("Cannot play card - game is not in PLAYING state. Current state: {}", state);
            return false;
        }

        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            logger.warn("Invalid player turn. Expected: {}, Actual: {}", currentPlayer.getId(), playerId);
            return false;
        }

        if (!currentPlayer.hasCards()) {
            logger.info("Player {} has no cards left. Game finished.", playerId);
            state = GameState.FINISHED;
            return true;
        }

        // Get the top card from the player's deck (index 0)
        Card playedCard = currentPlayer.drawCard();
        logger.info("Player {} played card: {}", playerId, playedCard);
        // Add the card to the table
        tableCards.add(playedCard);

        // Check if it's a winning card
        if (playedCard.isWinningCard()) {
            logger.info("Winning card played! Player {} must play {} cards", 
                getCurrentPlayer().getId(), playedCard.getCardsToPlay());
            lastWinningPlayer = currentPlayer;
            cardsToPlay = playedCard.getCardsToPlay();
            nextPlayer();
        } else if (cardsToPlay > 0) {
            // Player is obligated to play cards
            cardsToPlay--;
            logger.info("Obligatory card played. Remaining cards to play: {}", cardsToPlay);
            if (cardsToPlay == 0) {
                // Player completed their obligation, capture the table cards
                if (lastWinningPlayer != null) {
                    int capturedCards = tableCards.size();
                    lastWinningPlayer.captureCards(new ArrayList<>(tableCards));
                    logger.info("Player {} captured {} cards", lastWinningPlayer.getId(), capturedCards);
                    tableCards.clear();
                }
                lastWinningPlayer = null;
                nextPlayer();
            }
        } else {
            // Normal play, just move to next player
            logger.info("Normal card played. Moving to next player");
            nextPlayer();
        }

        // Check if game is finished
        checkGameEnd();
        logger.info("Current game state - Table cards: {}, Current player: {}, Cards to play: {}", 
            tableCards.size(), getCurrentPlayer().getId(), cardsToPlay);

        return true;
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void checkGameEnd() {
        // Check if any player has won (all 40 cards)
        for (Player player : players) {
            if (player.isWinner()) {
                state = GameState.FINISHED;
                return;
            }
        }
        
        // Note: The case where a player has no cards is now handled directly in playCard()
        // to immediately end the game when it's a player's turn and they have no cards
    }

    public Player getWinner() {
        if (state != GameState.FINISHED) {
            return null;
        }
        
        // Find the player with all 40 cards
        return players.stream()
                .filter(Player::isWinner)
                .findFirst()
                .orElse(null);
    }

    public Player getLoser() {
        if (state != GameState.FINISHED) {
            return null;
        }
        
        // Find the player with no cards to draw
        return players.stream()
                .filter(Player::hasLost)
                .findFirst()
                .orElse(null);
    }

    public boolean isGameFinished() {
        return state == GameState.FINISHED;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", players=" + players.size() +
                ", tableCards=" + tableCards.size() +
                ", currentPlayer=" + (getCurrentPlayer() != null ? getCurrentPlayer().getName() : "none") +
                '}';
    }
}
