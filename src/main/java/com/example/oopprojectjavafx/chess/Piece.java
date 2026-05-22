package com.example.oopprojectjavafx.chess;

import java.util.List;

public abstract class Piece {
    protected String color;
    protected int row;
    protected int col;
    protected boolean hasMoved = false;

    public Piece(String color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
    }

    public String getColor() { return color; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean hasMoved() { return hasMoved; }
    public void setPosition(int row, int col) { this.row = row; this.col = col; }
    public void setHasMoved(boolean moved) { this.hasMoved = moved; }

    public abstract List<int[]> getValidMoves(Piece[][] board);
}