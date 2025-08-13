package com.cavacamisa.dto;

public class PlayCardRequest {
    private String playerId;

    public PlayCardRequest() {}

    public PlayCardRequest(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
