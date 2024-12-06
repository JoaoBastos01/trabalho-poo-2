/**
 *
 */
package main;

import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Criação da classe pública principal representando o tabuleiro do jogo de xadrez
 */
public class Board extends JPanel {

    /**
     *  Inteiro público para definir o tamanho de cada "tile" (ladrilho) no tabuleiro
     */
    public int tileSize = 85;

    /**
     * Define as 8 colunas (cols) e 8 linhas (rows) no tabuleiro
     */
    int cols = 8;
    int rows = 8;

    /**
     * Lista criada para armazenar as peças presentes no respectivo tabuleiro
     */
    ArrayList<Piece> pieceList = new ArrayList<>();

    /**
     * Peça selecionada atualmente
     */
    public Piece selectedPiece;


    /**
     * Cria um manipulador de entrada específica para esta instância da classe tabuleiro (Board)
     */
    Input input = new Input(this);

    /**
     * Cria um Scanner para os cheques, usando como parâmetro o próprio tabuleiro que está inserido
     */
    CheckScanner checkscanner = new CheckScanner(this);

    private boolean isWhiteToMove = true;
    private boolean isGameOver = false;

    /**
     * Construtor da classe tabuleiro, definindo as configurações iniciais do tabuleiro
     */
    public Board() {
        this.setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        /**
         * Adiciona a configuração inicial das peças ao tabuleiro
         */
        addPieces();
    }

    /**
     * Getter para todas as peças adicionadas na lista de peças pieceList
     */
    public Piece getPiece(int col, int row) {

        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }

        return null;
    }

    /**
     * Método para fazer a movimentação genérica para as peças, possuindo uma condicional que verifica se esta peça é um peão, e se for o caso,
     * realiza o método movePawn (Mover peão), além de implementar a lógica de captura com o método capture, atríbuida à classe move (movimento)
     */
    public void makeMove(Move move) {

        if (move.piece.name.equals("Pawn")) {
            movePawn(move);

        } else {

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        move.piece.isFirstMove = false;

        capture(move.capture);

        isWhiteToMove = !isWhiteToMove;

        updateGameState();
        }

    }

    /**
     * Método para mover o peão, fazendo a atribuição de falso ao Booleano isFirstMove (Condicional para verificar se é o primeiro movimento)
     */
    public void movePawn(Move move) {
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        move.piece.isFirstMove = false;

        capture(move.capture);

        isWhiteToMove = !isWhiteToMove;

    }

    /**
     * Método que faz a remoção da peça capturada da lista de peças do respectivo tabuleiro
     */
    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    /**
     * Método que faz a verificação geral se o movimento é válido, utilizando os métodos respectivos para verificar se a peça a ser capturada é do mesmo time,
     * se o movimento é válido, e se o movimento colide com alguma outra peça
     */
    public boolean isValidMove(Move move) {

        if (isGameOver) {
            return false;
        }
        if(move.piece.isWhite != isWhiteToMove){
            return false;
        }
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (checkscanner.isKingChecked(move)) {
            return false;
        }



        return true;
    }

    /**
     * Verifica se a peça a ser capturada é do mesmo time da peça façendo o movimento
     */
    public boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Adiciona todas as peças no tabuleiro nas posições iniciais.
     */
    public void addPieces() {

        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));

        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));

        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));

        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));

    }

    private void updateGameState() {
        Piece king = findKing(isWhiteToMove);
        if (checkscanner.isGameOver(king)) {
            if (checkscanner.isKingChecked(new Move(this, king, king.col, king.row))) {
                System.out.println(isWhiteToMove ? "Black Wins!" : "White Wins!");
            } else {
                System.out.println("Impasse(Stalemate)!");
            }
        }
    }

    /**
     * Método com o propósito de renderizar o tabuleiro, as peças destacadas e as peças no JFrame criado na classe Match (Partida)
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;


        /**
         * Função dentro de laço com o propósito de pintar o tabuleiro no frame
         */
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                g2d.setColor((c + r) % 2 == 0 ? new Color(255, 255, 255) : new Color(29, 93, 120));
                g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
            }

        /**
         * Função condicional dependente da peça selecionada ser ou não nula, e com o propósito de destacar as posições válidas para a peça selecionada
         */
        if (selectedPiece != null)
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++) {

                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * tileSize, r * tileSize, tileSize, tileSize);
                    }
                }

        /**
         * Função dentro de laço com o propósito de pintar as peças no tabuleiro já impresso no frame
         */
        for (Piece piece : pieceList) {
                piece.paint(g2d);
            }
        }
    }

