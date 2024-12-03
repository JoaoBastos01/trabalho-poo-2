package main;

import pieces.Piece;

import java.time.Period;

/**
 * Criação da classe pública Move (Movimento), tendo como parâmetros os inteiros oldCol e oldRow,
 * representando a Coluna e Fileira antigas em que a peça especifica estava posicionada antes de fazer o movimento,
 * newCol e newRow, sendo estas a coluna e a fileira nova ocupada pela respectiva peça realizando o movimento,
 * e duas instâncias da classe Piece, sendos ela a peça relacionada ao movimento, e a provável peça a ser capturada pelo movimento
 *
 */
public class Move {

    int oldCol;
    int oldRow;

    int newCol;
    int newRow;

    Piece piece;
    Piece capture;

    /**
     * Construtor da classe Move (Movimento), definindo a configuração inicial básica de todas instâncias de Movimento
     * @param board
     * @param piece
     * @param newCol
     * @param newRow
     */
    public Move(Board board, Piece piece, int newCol, int newRow) {

        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;

        /**
         * Define a peça capturada, caso exista
         */
        this.capture =  board.getPiece(newCol, newRow);

    }

}
