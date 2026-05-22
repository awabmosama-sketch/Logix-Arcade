package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public Rook(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        // up
        for (int r = row - 1; r >= 0; r--) {
            if (board[r][col] == null) moves.add(new int[]{r, col});
            else {
                if (!board[r][col].getColor().equals(color)) moves.add(new int[]{r, col});
                break;
            }
        }
        // down
        for (int r = row + 1; r < 8; r++) {
            if (board[r][col] == null) moves.add(new int[]{r, col});
            else {
                if (!board[r][col].getColor().equals(color)) moves.add(new int[]{r, col});
                break;
            }
        }
        // left
        for (int c = col - 1; c >= 0; c--) {
            if (board[row][c] == null) moves.add(new int[]{row, c});
            else {
                if (!board[row][c].getColor().equals(color)) moves.add(new int[]{row, c});
                break;
            }
        }
        // right
        for (int c = col + 1; c < 8; c++) {
            if (board[row][c] == null) moves.add(new int[]{row, c});
            else {
                if (!board[row][c].getColor().equals(color)) moves.add(new int[]{row, c});
                break;
            }
        }
        return moves;
    }
}