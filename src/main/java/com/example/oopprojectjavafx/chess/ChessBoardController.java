package com.example.oopprojectjavafx.chess;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoardController {
    private final Board board = new Board();
    private String currentTurn = "white";
    private boolean gameOver = false;

    private final Map<Piece, StackPane> pieceToNode = new HashMap<>();
    private final Map<StackPane, Piece> nodeToPiece = new HashMap<>();
    private final Map<StackPane, EventHandler<MouseEvent>> originalHandlers = new HashMap<>();

    private Piece selectedPiece = null;
    private StackPane selectedWrapper = null;
    private List<int[]> currentLegalMoves = null;
    private String lastCheckedColor = null;

    @FXML
    GridPane chessBoard;

    private Pane getSquareAt(int row, int col) {
        for (Node node : chessBoard.getChildren()) {
            if (!(node instanceof Pane) || node instanceof StackPane) continue;
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if ((r == null ? 0 : r) == row && (c == null ? 0 : c) == col)
                return (Pane) node;
        }
        return null;
    }

    private void resetHighlights() {
        for (Node node : chessBoard.getChildren()) {
            if (node instanceof Pane && !(node instanceof StackPane)) {
                Pane square = (Pane) node;
                int r = GridPane.getRowIndex(square) == null ? 0 : GridPane.getRowIndex(square);
                int c = GridPane.getColumnIndex(square) == null ? 0 : GridPane.getColumnIndex(square);
                square.setStyle(((r + c) % 2 == 0) ? "-fx-background-color: beige" : "-fx-background-color: gray");
                square.setOnMouseClicked(null);
                square.setCursor(Cursor.DEFAULT);
            }
            if (node instanceof StackPane) {
                StackPane wrapper = (StackPane) node;
                wrapper.setStyle("");
                EventHandler<MouseEvent> original = originalHandlers.get(wrapper);
                if (original != null) wrapper.setOnMouseClicked(original);
            }
        }
        if (lastCheckedColor != null) highlightKingInCheck(lastCheckedColor);
    }

    private void highlightKingInCheck(String color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p instanceof King && p.getColor().equals(color)) {
                    Pane sq = getSquareAt(r, c);
                    if (sq != null) sq.setStyle("-fx-background-color: red");
                    return;
                }
            }
        }
    }

    private StackPane createPieceVisual(Piece piece) {
        Rectangle rect = new Rectangle();
        rect.widthProperty().bind(chessBoard.widthProperty().divide(8));
        rect.heightProperty().bind(chessBoard.heightProperty().divide(8));
        rect.setFill(piece.getColor().equals("white") ? Color.WHITE : Color.DARKGRAY);
        rect.setStroke(Color.BLACK);

        Text text = new Text(getPieceSymbol(piece));
        text.setFill(piece.getColor().equals("white") ? Color.BLACK : Color.WHITE);
        text.setFont(Font.font(chessBoard.widthProperty().divide(8).multiply(0.6).doubleValue()));

        StackPane wrapper = new StackPane(rect, text);
        wrapper.prefWidthProperty().bind(chessBoard.widthProperty().divide(8));
        wrapper.prefHeightProperty().bind(chessBoard.heightProperty().divide(8));
        wrapper.setCursor(Cursor.HAND);

        String resourcePath = "/com/example/oopprojectjavafx/pieces-basic-png/" + piece.getColor() + "-" + piece.getClass().getSimpleName().toLowerCase() + ".png";
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            ImageView iv = new ImageView(new Image(url.toExternalForm()));
            iv.setPreserveRatio(true);
            iv.fitWidthProperty().bind(chessBoard.widthProperty().divide(8));
            iv.fitHeightProperty().bind(chessBoard.heightProperty().divide(8));
            wrapper.getChildren().setAll(iv);
        }
        return wrapper;
    }

    private void updatePieceVisual(StackPane wrapper, Piece piece) {
        Rectangle rect = new Rectangle();
        rect.widthProperty().bind(chessBoard.widthProperty().divide(8));
        rect.heightProperty().bind(chessBoard.heightProperty().divide(8));
        rect.setFill(piece.getColor().equals("white") ? Color.WHITE : Color.DARKGRAY);
        rect.setStroke(Color.BLACK);

        Text text = new Text(getPieceSymbol(piece));
        text.setFill(piece.getColor().equals("white") ? Color.BLACK : Color.WHITE);
        text.setFont(Font.font(chessBoard.widthProperty().divide(8).multiply(0.6).doubleValue()));

        String resourcePath = "/com/example/oopprojectjavafx/pieces-basic-png/" + piece.getColor() + "-" + piece.getClass().getSimpleName().toLowerCase() + ".png";
        URL url = getClass().getResource(resourcePath);
        if (url != null) {
            ImageView iv = new ImageView(new Image(url.toExternalForm()));
            iv.setPreserveRatio(true);
            iv.fitWidthProperty().bind(chessBoard.widthProperty().divide(8));
            iv.fitHeightProperty().bind(chessBoard.heightProperty().divide(8));
            wrapper.getChildren().setAll(iv);
        } else {
            wrapper.getChildren().setAll(rect, text);
        }
    }

    private String getPieceSymbol(Piece p) {
        if (p instanceof Pawn) return "♙";
        if (p instanceof Rook) return "♖";
        if (p instanceof Knight) return "♘";
        if (p instanceof Bishop) return "♗";
        if (p instanceof Queen) return "♕";
        if (p instanceof King) return "♔";
        return "?";
    }

    private StackPane makePiece(Piece logicPiece) {
        StackPane wrapper = createPieceVisual(logicPiece);
        board.placePiece(logicPiece);
        pieceToNode.put(logicPiece, wrapper);
        nodeToPiece.put(wrapper, logicPiece);

        EventHandler<MouseEvent> selectHandler = evt -> {
            if (gameOver) return;
            resetHighlights();

            int row = GridPane.getRowIndex(wrapper) == null ? 0 : GridPane.getRowIndex(wrapper);
            int col = GridPane.getColumnIndex(wrapper) == null ? 0 : GridPane.getColumnIndex(wrapper);
            Piece p = board.getPiece(row, col);
            if (p == null || !p.getColor().equals(currentTurn)) return;

            selectedPiece = p;
            selectedWrapper = wrapper;
            currentLegalMoves = board.generateLegalMovesForPiece(p);

            System.out.println("=== Selected " + p.getClass().getSimpleName() + " at (" + row + "," + col + ") ===");
            System.out.println("Legal moves count: " + currentLegalMoves.size());
            for (int[] mv : currentLegalMoves) {
                System.out.println("  -> (" + mv[0] + "," + mv[1] + ")");
            }

            for (int[] mv : currentLegalMoves) {
                int tr = mv[0], tc = mv[1];
                Piece targetPiece = board.getPiece(tr, tc);
                boolean isCastling = (selectedPiece instanceof King && Math.abs(selectedPiece.getCol() - tc) == 2);
                if (isCastling) {
                    System.out.println("Castling move detected to (" + tr + "," + tc + ")");
                }

                if (targetPiece != null && !targetPiece.getColor().equals(currentTurn)) {
                    StackPane targetWrapper = pieceToNode.get(targetPiece);
                    if (targetWrapper != null) {
                        targetWrapper.setStyle("-fx-background-color: rgba(255,255,0,0.5); -fx-border-color: yellow; -fx-border-width: 2;");
                        targetWrapper.setCursor(Cursor.HAND);
                        originalHandlers.putIfAbsent(targetWrapper, (EventHandler<MouseEvent>) targetWrapper.getOnMouseClicked());
                        targetWrapper.setOnMouseClicked(captureEvent -> {
                            resetHighlights();
                            movePieceTo(tr, tc);
                        });
                    }
                } else {
                    Pane square = getSquareAt(tr, tc);
                    if (square != null) {
                        if (isCastling) {
                            square.setStyle("-fx-background-color: green");
                            System.out.println("Set green square at (" + tr + "," + tc + ")");
                        } else {
                            square.setStyle("-fx-background-color: yellow");
                        }
                        square.setCursor(Cursor.HAND);
                        square.setOnMouseClicked(e -> {
                            resetHighlights();
                            movePieceTo(tr, tc);
                        });
                    }
                }
            }
        };
        wrapper.setOnMouseClicked(selectHandler);
        originalHandlers.put(wrapper, selectHandler);
        return wrapper;
    }

    private void movePieceTo(int newRow, int newCol) {
        if (gameOver || selectedPiece == null || selectedWrapper == null) return;

        boolean allowed = false;
        if (currentLegalMoves != null) {
            for (int[] mv : currentLegalMoves)
                if (mv[0] == newRow && mv[1] == newCol) { allowed = true; break; }
        }
        if (!allowed) {
            selectedPiece = null;
            selectedWrapper = null;
            currentLegalMoves = null;
            resetHighlights();
            return;
        }

        // En passant detection
        boolean isEnPassant = false;
        Board.Move last = board.getLastMove();
        if (selectedPiece instanceof Pawn && last != null &&
                Math.abs(selectedPiece.getCol() - newCol) == 1 && board.getPiece(newRow, newCol) == null &&
                last.piece instanceof Pawn && last.toRow == selectedPiece.getRow() && last.toCol == newCol) {
            isEnPassant = true;
        }

        // *** IMPORTANT: Check castling BEFORE moving the piece ***
        boolean isCastling = (selectedPiece instanceof King && Math.abs(selectedPiece.getCol() - newCol) == 2);
        if (isCastling) {
            System.out.println("Executing castling...");
        }

        // Execute logic move
        Piece captured = null;
        if (isEnPassant && last != null) {
            captured = board.removeAt(last.toRow, last.toCol);
            board.movePiece(selectedPiece, newRow, newCol);
        } else {
            captured = board.movePiece(selectedPiece, newRow, newCol);
        }

        // Castling: move rook (both logic and UI) - now after moving king, but we need to move rook
        if (isCastling) {
            int row = selectedPiece.getRow();
            if (newCol == 6) { // kingside
                Piece rook = board.getPiece(row, 7);
                if (rook instanceof Rook) {
                    board.movePiece(rook, row, 5);
                    moveRookUI(rook, 7, row, 5, row);
                }
            } else if (newCol == 2) { // queenside
                Piece rook = board.getPiece(row, 0);
                if (rook instanceof Rook) {
                    board.movePiece(rook, row, 3);
                    moveRookUI(rook, 0, row, 3, row);
                }
            }
        }

        // Remove captured piece UI
        if (captured != null) {
            StackPane capNode = pieceToNode.remove(captured);
            if (capNode != null) {
                nodeToPiece.remove(capNode);
                originalHandlers.remove(capNode);
                chessBoard.getChildren().remove(capNode);
            } else {
                Node found = null;
                for (Node node : chessBoard.getChildren()) {
                    if (node instanceof StackPane && node != selectedWrapper) {
                        int r = GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node);
                        int c = GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node);
                        if (r == newRow && c == newCol) {
                            found = node; break;
                        }
                    }
                }
                if (found != null) chessBoard.getChildren().remove(found);
            }
        }

        // Move selected piece UI
        chessBoard.getChildren().remove(selectedWrapper);
        chessBoard.add(selectedWrapper, newCol, newRow);

        // Update mappings (promotion)
        Piece now = board.getPiece(newRow, newCol);
        if (now != selectedPiece) {
            pieceToNode.remove(selectedPiece);
            nodeToPiece.remove(selectedWrapper);
            pieceToNode.put(now, selectedWrapper);
            nodeToPiece.put(selectedWrapper, now);
            updatePieceVisual(selectedWrapper, now);
        } else {
            nodeToPiece.put(selectedWrapper, now);
            pieceToNode.put(now, selectedWrapper);
        }

        selectedPiece = null;
        selectedWrapper = null;
        currentLegalMoves = null;
        resetHighlights();

        // Switch turn
        currentTurn = currentTurn.equals("white") ? "black" : "white";
        System.out.println("Turn switched to " + currentTurn);

        // Check end conditions
        if (board.isCheckmate(currentTurn)) {
            gameOver = true;
            System.out.println("Checkmate! " + (currentTurn.equals("white") ? "Black" : "White") + " wins.");
        } else if (board.isStalemate(currentTurn)) {
            gameOver = true;
            System.out.println("Stalemate! Draw.");
        } else if (board.isInCheck(currentTurn)) {
            lastCheckedColor = currentTurn;
            highlightKingInCheck(currentTurn);
            System.out.println("Check on " + currentTurn);
        } else {
            lastCheckedColor = null;
        }
    }

    private void moveRookUI(Piece rook, int fromCol, int fromRow, int toCol, int toRow) {
        StackPane rookNode = pieceToNode.get(rook);
        if (rookNode == null) {
            System.err.println("ERROR: Rook node not found");
            return;
        }
        chessBoard.getChildren().remove(rookNode);
        chessBoard.add(rookNode, toCol, toRow);
        System.out.println("Rook moved from (" + fromRow + "," + fromCol + ") to (" + toRow + "," + toCol + ")");
    }

    @FXML
    public void initialize() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Pane square = new Pane();
                square.prefWidthProperty().bind(chessBoard.widthProperty().divide(8));
                square.prefHeightProperty().bind(chessBoard.heightProperty().divide(8));
                square.setStyle(((r + c) % 2 == 0) ? "-fx-background-color: beige" : "-fx-background-color: gray");
                chessBoard.add(square, c, r);
            }
        }

        for (int c = 0; c < 8; c++) {
            chessBoard.add(makePiece(new Pawn("white", 6, c)), c, 6);
            chessBoard.add(makePiece(new Pawn("black", 1, c)), c, 1);
        }
        chessBoard.add(makePiece(new Rook("white", 7, 0)), 0, 7);
        chessBoard.add(makePiece(new Rook("white", 7, 7)), 7, 7);
        chessBoard.add(makePiece(new Rook("black", 0, 0)), 0, 0);
        chessBoard.add(makePiece(new Rook("black", 0, 7)), 7, 0);
        chessBoard.add(makePiece(new Knight("white", 7, 1)), 1, 7);
        chessBoard.add(makePiece(new Knight("white", 7, 6)), 6, 7);
        chessBoard.add(makePiece(new Knight("black", 0, 1)), 1, 0);
        chessBoard.add(makePiece(new Knight("black", 0, 6)), 6, 0);
        chessBoard.add(makePiece(new Bishop("white", 7, 2)), 2, 7);
        chessBoard.add(makePiece(new Bishop("white", 7, 5)), 5, 7);
        chessBoard.add(makePiece(new Bishop("black", 0, 2)), 2, 0);
        chessBoard.add(makePiece(new Bishop("black", 0, 5)), 5, 0);
        chessBoard.add(makePiece(new Queen("white", 7, 3)), 3, 7);
        chessBoard.add(makePiece(new Queen("black", 0, 3)), 3, 0);
        chessBoard.add(makePiece(new King("white", 7, 4)), 4, 7);
        chessBoard.add(makePiece(new King("black", 0, 4)), 4, 0);
    }
}