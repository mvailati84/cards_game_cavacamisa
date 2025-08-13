package com.cavacamisa.model;

public enum GameState {
    WAITING_FOR_PLAYERS("Waiting for players"),
    DEALING("Dealing cards"),
    PLAYING("Game in progress"),
    FINISHED("Game finished");

    private final String displayName;

    GameState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
