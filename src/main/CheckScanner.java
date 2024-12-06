package main;

import pieces.Piece;


/**
 * Classe pública "CheckScanner" que tem como função principal fazer a checagem dos movimentos realizados no tabuleiro, verificando se o rei está em Cheque
 */
public class CheckScanner {

    /**
     * Representa o estado do tabuleiro utilizado pelo CheckScanner para avaliar
     * ameaças ao rei e outras mecânicas do jogo.
     * <p>
     * A variável 'board' armazena a configuração atual de todas as peças no
     * tabuleiro, permitindo a detecção de ameaças e a avaliação do estado do jogo.
     * Os métodos na classe CheckScanner dependem da variável 'board' para realizar
     * verificações e cálculos necessários para determinar se o rei está em cheque, se
     * peças específicas ameaçam o rei e para avaliar se o jogo acabou
     * com base nos movimentos disponíveis no tabuleiro.
     */
    Board board;

    /**
     * Constrói um CheckScanner com o tabuleiro fornecido.
     * <p>
     * Este construtor inicializa uma instância de CheckScanner que é usada
     * para realizar verificações de ameaças no jogo.
     */
    public CheckScanner(Board board) {
        this.board = board;
    }

    /**
     * Determina se o rei está atualmente em cheque no tabuleiro.
     * <p>
     * Este método valida se o rei está sob ameaça de alguma peça adversária
     * com base no movimento mais recente. Ele verifica vários tipos de peças, incluindo
     * torres, bispos, cavaleiros, peões e reis adversários para determinar se alguma delas pode atacar
     * o rei a partir de suas respectivas posições.
     *
     * @param move O último movimento realizado, que contém detalhes sobre
     *             a peça envolvida, suas novas posições de coluna e linha.
     * @return true se o rei estiver em cheque por qualquer peça adversária; false caso contrário.
     */
    public boolean isKingChecked(Move move) {

        /**
         * Localiza o rei no tabuleiro através de um método findKing (encontrarRei), e assegura que o rei será diferente de nulo
         */
        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        int kingCol = king.col;
        int kingRow = king.row;

        if (board.selectedPiece != null && board.selectedPiece.name.equals("King")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || // cima
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || // direita
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || // baixo
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || // esquerda

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // diagonal superior esquerda
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // diagonal superior direita
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // diagonal inferior direita
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // diagonal inferior esquerda

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);
    }

    /**
     * Determina se um dado rei está ameaçado por uma torre ou uma rainha ao longo de um caminho reto.
     * <p>
     * Este método verifica as linhas retas (horizontais e verticais) de onde uma torre ou uma rainha pode ameaçar o rei.
     *
     * @param col     a coluna da posição a ser verificada quanto a ameaças
     * @param row     a linha da posição a ser verificada quanto a ameaças
     * @param King    a peça do rei que está potencialmente ameaçada
     * @param kingCol a posição da coluna do rei
     * @param kingRow a posição da linha do rei
     * @param colVal  o valor do incremento de coluna para traçar o caminho reto
     * @param rowVal  o valor do incremento de linha para traçar o caminho reto
     * @return true se o rei estiver ameaçado por uma torre ou uma rainha ao longo do caminho reto especificado; false caso contrário
     */
    private boolean hitByRook(int col, int row, Piece King, int kingCol, int kingRow, int colVal, int rowVal) {
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
     * Determina se um dado rei está sob ameaça de um bispo ou uma rainha nas coordenadas especificadas.
     * <p>
     * Este método verifica os caminhos diagonais de onde um bispo ou uma rainha pode ameaçar o rei.
     *
     * @param col     a coluna da posição a ser verificada quanto a ameaças
     * @param row     a linha da posição a ser verificada quanto a ameaças
     * @param King    a peça do rei que está potencialmente ameaçada
     * @param kingCol a posição da coluna do rei
     * @param kingRow a posição da linha do rei
     * @param colVal  o valor do incremento de coluna para traçar o caminho diagonal
     * @param rowVal  o valor do incremento de linha para traçar o caminho diagonal
     * @return true se o rei estiver ameaçado por um bispo ou uma rainha ao longo do caminho diagonal especificado; false caso contrário
     */
    private boolean hitByBishop(int col, int row, Piece King, int kingCol, int kingRow, int colVal, int rowVal) {
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
     * Determina se um dado rei está sob ameaça de um cavaleiro nas coordenadas especificadas.
     * <p>
     * Este método verifica todas as posições possíveis de onde um cavaleiro pode ameaçar o rei.
     *
     * @param col a coluna atual
     */
    private boolean hitByKnight(int col, int row, Piece king, int kingCol, int kingRow) {
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
     * Verifica se um cavaleiro especificado está em uma determinada posição e não está na mesma equipe de outra peça.
     *
     * @param p   a peça a ser verificada, que deve ser um cavaleiro potencial
     * @param k   a peça contra a qual a verificação da equipe é realizada
     * @param col a posição de coluna do cavaleiro
     * @param row a posição de linha do cavaleiro
     * @return true se a peça for um cavaleiro e não estiver na mesma equipe do rei
     */
    private boolean checkKnight(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Knight") && p.col == col && p.row == row;
    }

    /**
     * Verifica se um Rei pode atacar a posição de outra peça.
     * <p>
     * Este método verifica os ataques potenciais a uma peça dada por um Rei localizado nas coordenadas especificadas
     * verificando todas as posições possíveis para onde o Rei pode se mover a partir de sua localização atual.
     *
     * @param king    A peça Rei que está sendo verificada para possíveis ataques.
     * @param kingCol A posição da coluna do Rei.
     * @param kingRow A posição da linha do Rei.
     * @return true se pelo menos uma das posições ao redor do Rei contiver uma peça pertencente à equipe oposta, indicando captura potencial; falso caso contrário.
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
     * Verifica se a peça especificada é um Rei que não está na mesma equipe que outra peça.
     *
     * @param p a peça a ser verificada, que se espera que seja potencialmente um Rei
     * @param k a peça contra a qual a verificação de equipe está sendo realizada
     * @return true se a peça 'p' for um Rei e não estiver na mesma equipe que a peça 'k'; falso caso contrário
     */
    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("King");
    }

    /**
     * Determina se um Rei dado está ameaçado por um peão.
     *
     * @param col     a posição da coluna para verificar peões ameaçando o Rei
     * @param row     a posição da linha para verificar peões ameaçando o Rei
     * @param king    a peça Rei que está potencialmente ameaçada
     * @param kingCol a posição da coluna do Rei
     * @param kingRow a posição da linha do Rei
     * @return true se o Rei estiver ameaçado por um peão de qualquer posição diagonal adjacente; falso caso contrário
     */
    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorVal = king.isWhite ? -1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVal), king, kingCol + 1, kingRow + colorVal) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVal), king, kingCol - 1, kingRow + colorVal);
    }

    /**
     * Verifica se um peão especificado está em uma posição específica, não estando na mesma equipe que outra peça.
     *
     * @param p   a peça a ser verificada, esperada para ser um peão potencial
     * @param k   a peça contra a qual a equipe é verificada
     * @param col a posição da coluna para verificar o peão
     * @param row a posição da linha para verificar o peão
     * @return true se a peça for um peão na posição especificada e não estiver na mesma equipe que a peça 'k'; falso caso contrário
     */
    private boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn") && p.col == col && p.row == row;
    }

    /**
     * Determina se o jogo acabou verificando se ainda há movimentos válidos restantes
     * para as peças da mesma equipe que o Rei especificado.
     *
     * @param king a peça Rei a ser verificada para possíveis movimentos válidos por sua equipe
     * @return true se não houver movimentos válidos disponíveis para a equipe do Rei, indicando que o jogo acabou; falso caso contrário
     */
    public boolean isGameOver(Piece king) {
        for (Piece piece : board.pieceList) {
            if (board.sameTeam(piece, king)) {
                board.selectedPiece = piece == king ? king : null;
                for (int row = 0; row < board.rows; row++) {
                    for (int col = 0; col < board.cols; col++) {
                        Move move = new Move(board, piece, col, row);
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
