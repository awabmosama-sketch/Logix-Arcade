package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        for (int[] d : dirs) {
            int r = row + d[0];
            int c = col + d[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece target = board[r][c];
                if (target == null || !target.getColor().equals(color))
                    moves.add(new int[]{r, c});
            }
        }
        return moves;
    }
}