package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(String color, int row, int col) { super(color, row, col); }

    @Override
    public List<int[]> getValidMoves(Piece[][] board) {
        List<int[]> moves = new ArrayList<>();
        moves.addAll(new Rook(color, row, col).getValidMoves(board));
        moves.addAll(new Bishop(color, row, col).getValidMoves(board));
        return moves;
    }
}