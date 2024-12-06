package main;

import pieces.Piece;


/**
 * Classe pública "CheckScanner" que tem como função principal fazer a checagem dos movimentos realizados no tabuleiro, verificando se o rei esta em Cheque
 */
public class CheckScanner {

    /**
     * Represents the chessboard state utilized by the CheckScanner for evaluating
     * threats to the king and other game mechanics.
     *
     * The 'board' variable stores the current configuration of all the pieces on
     * the chessboard, enabling threat detection and assessment of the game state.
     * Methods in the CheckScanner class rely on the 'board' variable to perform
     * checks and computations necessary to determine if the king is in check, if
     * specific pieces threaten the king, and to assess whether the game is over
     * based on the available moves on the board.
     */
    Board board;

    /**
     * Constructs a CheckScanner with the provided board.
     *
     * This constructor initializes a CheckScanner instance that is used
     * to*/
    public CheckScanner(Board board) {
        this.board = board;
    }

    /**
     * Determines if the king is currently in check on the board.
     *
     * This method validates whether the king is under threat from any opposing pieces
     * based on the most recent move. It checks against various piece types including
     * rooks, bishops, knights, pawns, and opposing kings to determine if any can attack
     * the king from their respective positions.
     *
     * @param move The last move that was made, which contains details about
     *             the piece involved, its new column, and row positions.
     *
     * @return true if the king is in check by any opposing piece; false otherwise.
     */
    public boolean isKingChecked(Move move) {

        /**
         * Localiza o rei no tabuleiro através de um método findKing (encontrarRei), e assegura ue o rei vai ser diferente de nulo
         */
        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        int kingCol = king.col;
        int kingRow = king.row;

        if (board.selectedPiece != null && board.selectedPiece.name.equals("King")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRook   (move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || // cima
                hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || // direita
                hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || // baixo
                hitByRook  (move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || // esquerda

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // diagonal superior esquerda
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // diagonal superior direita
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow,1, 1) || // diagonal inferior direita
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // diagonal inferior esquerda

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);


    }

    /**
     * Determines if a given king is threatened by a rook or a queen along a straight path.
     *
     * This method checks the straight lines (horizontal and vertical) from which a rook or a queen can threaten the king.
     *
     * @param col the column of the position to check for threats
     * @param row the row of the position to check for threats
     * @param King the king piece that is potentially threatened
     * @param kingCol the column position of the king
     * @param kingRow the row position of the king
     * @param colVal the column increment value to trace the straight path
     * @param rowVal the row increment value to trace the straight path
     * @return true if the king is threatened by a rook or a queen from the specified straight path; false otherwise
     */
    private boolean hitByRook(int col, int row, Piece King,int kingCol,int kingRow,int colVal, int rowVal) {
        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break;
            }

            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));

            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, King) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Determines if a given king is under threat by a bishop or a queen at specified coordinates.
     *
     * This method checks the diagonal paths from which a bishop or a queen can threaten the king.
     *
     * @param col the column of the position to check for threats
     * @param row the row of the position to check for threats
     * @param King the king piece that is potentially threatened
     * @param kingCol the column position of the king
     * @param kingRow the row position of the king
     * @param colVal the column increment value to trace the diagonal path
     * @param rowVal the row increment value to trace the diagonal path
     * @return true if the king is threatened by a bishop or a queen from the specified diagonal path; false otherwise
     */
    private boolean hitByBishop(int col, int row, Piece King,int kingCol,int kingRow,int colVal, int rowVal) {
        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {
                break;
            }

            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, King) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * Determines if a given king is under threat by a knight at specified coordinates.
     *
     * This method checks all possible positions from which a knight can threaten the king.
     *
     * @param col the current column*/
    private boolean hitByKnight(int col, int row, Piece king,int kingCol,int kingRow) {
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, kingCol - 1, kingRow - 2) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, kingCol + 1, kingRow - 2) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, kingCol + 2, kingRow - 1) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, kingCol + 2, kingRow + 1) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, kingCol + 1, kingRow + 2) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, kingCol - 1, kingRow + 2) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, kingCol - 2, kingRow + 1) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, kingCol - 2, kingRow - 1);
    }

    /**
     * Checks if a specified knight is at a certain position and is not on the same team as another piece.
     *
     * @param p the piece to check, expected to be a potential knight
     * @param k the piece against which the team check is being performed
     * @param col the column position to check for the knight
     * @param row the row position to check for the knight
     * @return true if the piece is a knight at the specified position and not on the same team as piece k; false otherwise
     */
    private boolean checkKnight(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Knight") && p.col == col && p.row == row;
    }

    /**
     * Checks if a King can attack the position of another piece.
     *
     * This method checks for potential attacks on a given piece by a King located at the specified coordinates
     * by checking all possible positions that a King can move to from its current location.
     *
     * @param king The King piece that is being checked for potential attacks.
     * @param kingCol The column position of the King.
     * @param kingRow The row position of the King.
     * @return true if at least one of the positions around the King contains a piece belonging to the opposing team, indicating potential capture; false otherwise.
     */
    private boolean hitByKing(Piece king, int kingCol, int kingRow) {
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    /**
     * Checks if the specified piece is a King piece that is not on the same team as another piece.
     *
     * @param p the piece to be checked, which is expected to potentially be a King
     * @param k the piece against which the team check is being performed
     * @return true if the piece 'p' is a King and not on the same team as piece 'k'; false otherwise
     */
    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("King");
    }

    /**
     * Determines if a given king is threatened by a pawn.
     *
     * @param col the column position to check for pawns threatening the king
     * @param row the row position to check for pawns threatening the king
     * @param king the king piece that is potentially threatened
     * @param kingCol the column position of the king
     * @param kingRow the row position of the king
     * @return true if the king is threatened by a pawn from any diagonal adjacent position; false otherwise
     */
    private  boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorVal = king.isWhite ? -1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, kingCol + 1, kingRow + colorVal) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, kingCol - 1, kingRow + colorVal);
    }

    /**
     * Checks if a specified pawn is at a certain position, not on the same team as another piece.
     *
     * @param p the piece to check, expected to be a potential pawn
     * @param k the piece against which the team is checked
     * @param col the column position to check for the pawn
     * @param row the row position to check for the pawn
     * @return true if the piece is a pawn at the specified position and not on the same team as piece k; false otherwise
     */
    private boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn") && p.col == col && p.row == row;
    }

    /**
     * Determines whether the game is over by checking if there are any valid moves left
     * for the pieces on the same team as the specified king.
     *
     * @param king the king piece to be checked for potential valid moves by its team
     * @return true if no valid moves are available for the king's team, indicating the
     *         game is over; false otherwise
     */
    public boolean isGameOver(Piece king) {
        for (Piece piece : board.pieceList) {
            if (board.sameTeam(piece, king)) {
                board.selectedPiece = piece == king ? king : null;
                for (int row = 0; row < board.rows; row++) {
                    for (int col = 0; col < board.cols; col++) {
                        Move move = new Move(board, piece, col ,row);
                        if (board.isValidMove(move)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
