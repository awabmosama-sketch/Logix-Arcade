package com.example.oopprojectjavafx.chess;

public class Game {
    private final Board board = new Board();
    private String currentTurn = "white";

    public Board getBoard() { return board; }
    public String getCurrentTurn() { return currentTurn; }
    public void switchTurn() { currentTurn = currentTurn.equals("white") ? "black" : "white"; }
    public boolean isInCheck(String color) { return board.isInCheck(color); }
    public boolean isCheckmate(String color) { return board.isCheckmate(color); }
    public boolean isStalemate(String color) { return board.isStalemate(color); }
}