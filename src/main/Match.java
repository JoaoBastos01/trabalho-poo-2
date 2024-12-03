package main;

import javax.swing.*;
import java.awt.*;

public class Match {

    /**
     * Método para iniciar a partida, fazendo a criação de um JFrame e setando a cor do seu background como preto,
     * seguido da criação de um GridBagLayout e definindo suas dimensões, fazendo a declaração de uma nova instância
     * da classe Board (Tabuleiro) e adicionando ao JFrame, definindo também a visibilidade do frame como verdadeira
     */
    private boolean isWhiteTurn = true;
    public void start() {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setLocationRelativeTo(null);

        Board board = new Board();
        frame.add(board);

        frame.setVisible(true);

        System.out.println("Partida inicializada");


    }
}
