package com.example.oopprojectjavafx.RockPaperScisscors;

public class ComputerPlayer extends Player{
    @Override
    public String makeChoice(String ignoredInput) {
        int index = (int) (Math.random() * 3);
        String[] moves = {"rock", "paper", "scissors"};
        return moves[index];
    }
}
