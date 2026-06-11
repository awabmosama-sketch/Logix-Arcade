package com.example.oopprojectjavafx.RockPaperScisscors;

public abstract class Player {
    private int score = 0; // Encapsulated data

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        this.score++;
    }

    public abstract String makeChoice(String input);
}
