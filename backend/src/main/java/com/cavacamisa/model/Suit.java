package com.cavacamisa.model;

public enum Suit {
    BASTONI("Bastoni"),
    SPADE("Spade"),
    ORI("Ori"),
    COPPE("Coppe");

    private final String displayName;

    Suit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
