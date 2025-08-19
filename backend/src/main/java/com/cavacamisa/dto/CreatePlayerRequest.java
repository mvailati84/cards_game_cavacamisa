package com.cavacamisa.dto;

public class CreatePlayerRequest {
    private String name;

    public CreatePlayerRequest() {}

    public CreatePlayerRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
