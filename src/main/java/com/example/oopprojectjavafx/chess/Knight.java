package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        int[][] offsets = {{-2,-1},{-2,1},{-1,-2},{-1,2},{1,-2},{1,2},{2,-1},{2,1}};
        for (int[] o : offsets) {
            int r = row + o[0];
            int c = col + o[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece target = board[r][c];
                if (target == null || !target.getColor().equals(color))
                    moves.add(new int[]{r, c});
            }
        }
        return moves;
    }
}