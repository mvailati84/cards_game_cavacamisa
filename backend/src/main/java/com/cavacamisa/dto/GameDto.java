package com.cavacamisa.dto;

import com.cavacamisa.model.Game;
import java.util.List;
import java.util.stream.Collectors;

public class GameDto {
    private String id;
    private String state;
    private List<PlayerDto> players;
    private List<CardDto> tableCards;
    private int currentPlayerIndex;
    private int cardsToPlay;
    private PlayerDto lastWinningPlayer;
    private PlayerDto winner;
    private PlayerDto loser;
    private boolean gameFinished;

    public GameDto() {}

    public GameDto(Game game) {
        this.id = game.getId();
        this.state = game.getState().getDisplayName();
        this.players = game.getPlayers().stream()
                .map(PlayerDto::new)
                .collect(Collectors.toList());
        this.tableCards = game.getTableCards().stream()
                .map(CardDto::new)
                .collect(Collectors.toList());
        this.currentPlayerIndex = game.getCurrentPlayerIndex();
        this.cardsToPlay = game.getCardsToPlay();
        this.lastWinningPlayer = game.getLastWinningPlayer() != null ? 
                new PlayerDto(game.getLastWinningPlayer()) : null;
        this.winner = game.getWinner() != null ? 
                new PlayerDto(game.getWinner()) : null;
        this.loser = game.getLoser() != null ? 
                new PlayerDto(game.getLoser()) : null;
        this.gameFinished = game.isGameFinished();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }

    public List<CardDto> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<CardDto> tableCards) {
        this.tableCards = tableCards;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public int getCardsToPlay() {
        return cardsToPlay;
    }

    public void setCardsToPlay(int cardsToPlay) {
        this.cardsToPlay = cardsToPlay;
    }

    public PlayerDto getLastWinningPlayer() {
        return lastWinningPlayer;
    }

    public void setLastWinningPlayer(PlayerDto lastWinningPlayer) {
        this.lastWinningPlayer = lastWinningPlayer;
    }

    public PlayerDto getWinner() {
        return winner;
    }

    public void setWinner(PlayerDto winner) {
        this.winner = winner;
    }

    public PlayerDto getLoser() {
        return loser;
    }

    public void setLoser(PlayerDto loser) {
        this.loser = loser;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
}
