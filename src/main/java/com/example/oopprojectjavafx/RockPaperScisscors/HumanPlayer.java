package com.example.oopprojectjavafx.RockPaperScisscors;

public class HumanPlayer extends Player{
    @Override
    public String makeChoice(String buttonId) {
        if (buttonId.equals("rockButton")) {
            return "rock";
        }
        if (buttonId.equals("paperButton")){
            return "paper";
        }
        return "scissors";
    }
}

