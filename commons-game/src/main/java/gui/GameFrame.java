package gui;

import javax.swing.*;

public class GameFrame extends JFrame{

    GamePanel game;
    JButton resetButton;

    public GameFrame() {

        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Flappy Not So Bird");

        resetButton = new JButton("Reset Game");
        resetButton.addActionListener(e->game.gameRestarts());
        resetButton.setFocusable(false);

        game = new GamePanel();
        game.add(resetButton);

        this.add(game);
        this.setVisible(true);
        this.pack();

        game.startGameThread();
    }
}
