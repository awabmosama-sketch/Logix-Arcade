package com.example.oopprojectjavafx.chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Piece[][] grid = new Piece[8][8];
    private Move lastMove = null;

    public static class Move {
        public final Piece piece;
        public final int fromRow, fromCol, toRow, toCol;
        public final Piece captured;
        public Move(Piece piece, int fr, int fc, int tr, int tc, Piece captured) {
            this.piece = piece;
            this.fromRow = fr; this.fromCol = fc;
            this.toRow = tr; this.toCol = tc;
            this.captured = captured;
        }
    }

    public Piece[][] getGrid() { return grid; }
    public Move getLastMove() { return lastMove; }

    private boolean inBounds(int r, int c) { return r >= 0 && r < 8 && c >= 0 && c < 8; }

    public Piece getPiece(int r, int c) {
        return inBounds(r, c) ? grid[r][c] : null;
    }

    public void placePiece(Piece p) {
        if (p != null && inBounds(p.getRow(), p.getCol()))
            grid[p.getRow()][p.getCol()] = p;
    }

    public Piece removeAt(int r, int c) {
        if (!inBounds(r, c)) return null;
        Piece p = grid[r][c];
        grid[r][c] = null;
        return p;
    }

    public Piece movePiece(Piece piece, int toR, int toC) {
        if (piece == null || !inBounds(toR, toC)) return null;
        int fromR = piece.getRow(), fromC = piece.getCol();

        if (inBounds(fromR, fromC) && grid[fromR][fromC] == piece)
            grid[fromR][fromC] = null;
        else
            removePieceReference(piece);

        Piece captured = grid[toR][toC];
        grid[toR][toC] = piece;
        piece.setPosition(toR, toC);
        piece.setHasMoved(true);

        if (piece instanceof Pawn && ((piece.getColor().equals("white") && toR == 0) || (piece.getColor().equals("black") && toR == 7))) {
            Piece queen = new Queen(piece.getColor(), toR, toC);
            queen.setHasMoved(true);
            grid[toR][toC] = queen;
            piece = queen;
        }

        lastMove = new Move(piece, fromR, fromC, toR, toC, captured);
        return captured;
    }

    private void removePieceReference(Piece piece) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (grid[r][c] == piece) {
                    grid[r][c] = null;
                    return;
                }
    }

    public List<int[]> generateLegalMovesForPiece(Piece piece) {
        List<int[]> legal = new ArrayList<>();
        if (piece == null) return legal;

        List<int[]> pseudo = new ArrayList<>();
        if (piece instanceof Pawn) {
            pseudo.addAll(piece.getValidMoves(grid));
            addEnPassantIfAvailable((Pawn) piece, pseudo);
        } else if (piece instanceof King) {
            pseudo.addAll(piece.getValidMoves(grid));
            addCastlingIfAvailable((King) piece, pseudo);
        } else {
            pseudo.addAll(piece.getValidMoves(grid));
        }

        for (int[] mv : pseudo) {
            int tr = mv[0], tc = mv[1];
            if (!inBounds(tr, tc)) continue;

            if (piece instanceof King && Math.abs(piece.getCol() - tc) == 2) {
                if (isCastlingLegal((King) piece, tc))
                    legal.add(new int[]{tr, tc});
            } else if (piece instanceof Pawn && isEnPassantMove(piece, tr, tc)) {
                if (isEnPassantLegal((Pawn) piece, tr, tc))
                    legal.add(new int[]{tr, tc});
            } else {
                if (isMoveLegal(piece, tr, tc))
                    legal.add(new int[]{tr, tc});
            }
        }
        return legal;
    }

    private boolean isMoveLegal(Piece piece, int toR, int toC) {
        int fromR = piece.getRow(), fromC = piece.getCol();
        Piece captured = grid[toR][toC];
        grid[fromR][fromC] = null;
        grid[toR][toC] = piece;
        piece.setPosition(toR, toC);
        boolean safe = !isInCheck(piece.getColor());
        piece.setPosition(fromR, fromC);
        grid[fromR][fromC] = piece;
        grid[toR][toC] = captured;
        return safe;
    }

    private void addEnPassantIfAvailable(Pawn pawn, List<int[]> pseudo) {
        if (lastMove == null) return;
        if (!(lastMove.piece instanceof Pawn)) return;
        if (Math.abs(lastMove.fromRow - lastMove.toRow) != 2) return;
        int prow = pawn.getRow(), pcol = pawn.getCol();
        if (lastMove.toRow == prow && Math.abs(lastMove.toCol - pcol) == 1) {
            int capRow = pawn.getColor().equals("white") ? prow - 1 : prow + 1;
            int capCol = lastMove.toCol;
            if (inBounds(capRow, capCol) && grid[capRow][capCol] == null)
                pseudo.add(new int[]{capRow, capCol});
        }
    }

    private boolean isEnPassantMove(Piece pawn, int toR, int toC) {
        if (!(pawn instanceof Pawn) || lastMove == null) return false;
        if (!(lastMove.piece instanceof Pawn)) return false;
        if (Math.abs(lastMove.fromRow - lastMove.toRow) != 2) return false;
        int prow = pawn.getRow(), pcol = pawn.getCol();
        return (lastMove.toRow == prow && Math.abs(lastMove.toCol - pcol) == 1 &&
                toR == (pawn.getColor().equals("white") ? prow - 1 : prow + 1) &&
                toC == lastMove.toCol);
    }

    private boolean isEnPassantLegal(Pawn pawn, int toR, int toC) {
        int fromR = pawn.getRow(), fromC = pawn.getCol();
        int capRow = lastMove.toRow, capCol = lastMove.toCol;
        Piece capturedPawn = grid[capRow][capCol];
        grid[capRow][capCol] = null;
        grid[fromR][fromC] = null;
        grid[toR][toC] = pawn;
        pawn.setPosition(toR, toC);
        boolean safe = !isInCheck(pawn.getColor());
        pawn.setPosition(fromR, fromC);
        grid[fromR][fromC] = pawn;
        grid[toR][toC] = null;
        grid[capRow][capCol] = capturedPawn;
        return safe;
    }

    private void addCastlingIfAvailable(King king, List<int[]> pseudo) {
        if (king.hasMoved()) return;
        String color = king.getColor();
        int row = king.getRow(), col = king.getCol();
        if (isInCheck(color)) return;

        // Kingside
        Piece rookK = getPiece(row, 7);
        if (rookK instanceof Rook && !rookK.hasMoved()) {
            boolean empty = true;
            for (int c = col + 1; c < 7; c++)
                if (grid[row][c] != null) { empty = false; break; }
            if (empty && !isSquareAttacked(row, col+1, oppositeColor(color)) &&
                    !isSquareAttacked(row, col+2, oppositeColor(color))) {
                pseudo.add(new int[]{row, col + 2});
            }
        }
        // Queenside
        Piece rookQ = getPiece(row, 0);
        if (rookQ instanceof Rook && !rookQ.hasMoved()) {
            boolean empty = true;
            for (int c = 1; c < col; c++)
                if (grid[row][c] != null) { empty = false; break; }
            if (empty && !isSquareAttacked(row, col-1, oppositeColor(color)) &&
                    !isSquareAttacked(row, col-2, oppositeColor(color))) {
                pseudo.add(new int[]{row, col - 2});
            }
        }
    }

    private boolean isCastlingLegal(King king, int toCol) {
        String color = king.getColor();
        int row = king.getRow();
        int fromCol = king.getCol();
        boolean kingside = toCol > fromCol;
        int rookFromCol = kingside ? 7 : 0;
        int rookToCol = kingside ? 5 : 3;
        Piece rook = getPiece(row, rookFromCol);
        if (!(rook instanceof Rook) || rook.hasMoved()) return false;

        Piece capturedRookTarget = grid[row][rookToCol];
        grid[row][fromCol] = null;
        grid[row][toCol] = king;
        king.setPosition(row, toCol);
        grid[row][rookFromCol] = null;
        grid[row][rookToCol] = rook;
        rook.setPosition(row, rookToCol);

        boolean safe = !isInCheck(color);

        king.setPosition(row, fromCol);
        grid[row][fromCol] = king;
        grid[row][toCol] = null;
        rook.setPosition(row, rookFromCol);
        grid[row][rookFromCol] = rook;
        grid[row][rookToCol] = capturedRookTarget;

        return safe;
    }

    public boolean isInCheck(String color) {
        int kr = -1, kc = -1;
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if (grid[r][c] instanceof King && grid[r][c].getColor().equals(color)) {
                    kr = r; kc = c; break;
                }
        if (kr == -1) return false;
        return isSquareAttacked(kr, kc, oppositeColor(color));
    }

    public boolean isSquareAttacked(int r, int c, String attackerColor) {
        int pawnDir = attackerColor.equals("white") ? -1 : 1;
        if (inBounds(r - pawnDir, c - 1) && grid[r - pawnDir][c - 1] instanceof Pawn &&
                grid[r - pawnDir][c - 1].getColor().equals(attackerColor))
            return true;
        if (inBounds(r - pawnDir, c + 1) && grid[r - pawnDir][c + 1] instanceof Pawn &&
                grid[r - pawnDir][c + 1].getColor().equals(attackerColor))
            return true;

        for (int rr = 0; rr < 8; rr++) {
            for (int cc = 0; cc < 8; cc++) {
                Piece p = grid[rr][cc];
                if (p == null || !p.getColor().equals(attackerColor)) continue;
                if (p instanceof King && Math.abs(rr - r) <= 1 && Math.abs(cc - c) <= 1)
                    return true;
                if (p instanceof Knight) {
                    int dr = Math.abs(rr - r);
                    int dc = Math.abs(cc - c);
                    if ((dr == 2 && dc == 1) || (dr == 1 && dc == 2))
                        return true;
                }
                if (p instanceof Bishop || p instanceof Rook || p instanceof Queen) {
                    List<int[]> moves = p.getValidMoves(grid);
                    for (int[] mv : moves)
                        if (mv[0] == r && mv[1] == c)
                            return true;
                }
            }
        }
        return false;
    }

    public boolean hasAnyLegalMove(String color) {
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Piece p = grid[r][c];
                if (p != null && p.getColor().equals(color) && !generateLegalMovesForPiece(p).isEmpty())
                    return true;
            }
        return false;
    }

    public boolean isCheckmate(String color) {
        return isInCheck(color) && !hasAnyLegalMove(color);
    }

    public boolean isStalemate(String color) {
        return !isInCheck(color) && !hasAnyLegalMove(color);
    }

    private String oppositeColor(String color) {
        return color.equals("white") ? "black" : "white";
    }
}