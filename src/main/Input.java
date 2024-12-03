package main;

/**
 * Importa a classe genérica Piece (Peça) do pacote pieces (peças)
 */
import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input extends MouseAdapter {

    /**
     * Cria uma instância da classe Board (tabuleiro)
     */
    Board board;

    public Input(Board board) {
        this.board = board;

    }

    /**
     * Lida com o clique no tabuleiro para selecionar uma peça
     */
    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        Piece pieceXY = board.getPiece(col, row);
        if (pieceXY != null) {
            board.selectedPiece = pieceXY;
        }
    }

    /**
     * Permite o movimento de "arrastar" uma peça
     */
    @Override
    public void mouseDragged(MouseEvent e) {

        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.tileSize / 2;
            board.selectedPiece.yPos = e.getY() - board.tileSize / 2;

            board.repaint();
        }

    }

    /**
     * ao soltar a peça na nova posição, valida o movimento
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        int col = e.getX() / board.tileSize;
        int row = e.getY() / board.tileSize;

        if (board.selectedPiece != null) {
            Move move = new Move(board, board.selectedPiece, col, row);

            if (board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col * board.tileSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tileSize;

            }
        }

        board.selectedPiece = null;
        board.repaint();

    }


}
