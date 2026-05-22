package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        int dir = color.equals("white") ? -1 : 1;
        int nr = row + dir;
        if (nr >= 0 && nr < 8) {
            if (board[nr][col] == null) {
                moves.add(new int[]{nr, col});
                int start = color.equals("white") ? 6 : 1;
                int two = row + 2 * dir;
                if (row == start && two >= 0 && two < 8 && board[two][col] == null)
                    moves.add(new int[]{two, col});
            }
            if (col - 1 >= 0 && board[nr][col - 1] != null && !board[nr][col - 1].getColor().equals(color))
                moves.add(new int[]{nr, col - 1});
            if (col + 1 < 8 && board[nr][col + 1] != null && !board[nr][col + 1].getColor().equals(color))
                moves.add(new int[]{nr, col + 1});
        }
        return moves;
    }
}