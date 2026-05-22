package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        int[][] dirs = {{-1,-1},{-1,1},{1,-1},{1,1}};
        for (int[] d : dirs) {
            int r = row + d[0], c = col + d[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c] == null) moves.add(new int[]{r, c});
                else {
                    if (!board[r][c].getColor().equals(color)) moves.add(new int[]{r, c});
                    break;
                }
                r += d[0]; c += d[1];
            }
        }
        return moves;
    }
}