package gui;

import logic.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GamePanel extends JPanel implements Runnable{
    //game logic data
    @Getter @Setter
    boolean gameOver = false;
    @Getter
    boolean gameStarted = false;


    @Getter
    int gameSpeed = 2;

    @Getter
    static final int tileSize = 48;
    @Getter
    static final int screenWidth = 576;
    @Getter
    static final int screenHeight = 768;
    @Getter
    int groundLevel = screenHeight - tileSize;
    static final int FPS = 60;

    @Getter @Setter
    int score = 0;
    transient Thread gameThread;
    transient KeyHandler keyH;
    transient Player player;
    @Getter
    transient Music music;



    //column data
    int gapSize = 5 * tileSize + tileSize/3;
    transient ArrayList<Column> columns;
    double timerCurrent = 0;
    double timerTotal = 150;

    transient Background bg;

    String FONT_NAME = "Monospaced";

    public GamePanel(){
        prepareGame();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void prepareGame(){
        keyH = new KeyHandler(this);
        player = new Player(this, keyH);
        columns = new ArrayList<>();
        bg = new Background(this);
        music = new Music();
        music.playMusic("src/main/resources/sounds/background_music.wav", -10.0f, true);

    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void startGame() {
        gameStarted = true;
    }

    public void addColumn(){
        int topHeight = ThreadLocalRandom.current().nextInt(screenHeight/4, 8 * screenHeight/15 + 1);
        columns.add(new Column(this, topHeight,  false));
        columns.add(new Column(this, screenHeight - topHeight - gapSize, true));
    }
    public void endGame(){
        keyH.getKeysPressed().clear();
        player.gameEnds();
        columns.forEach(Column::gameEnds);
        bg.gameEnds();
    }
    public void gameRestarts(){
        gameStarted = false;
        gameOver = false;
        repaint();
        score = 0;
        player = new Player(this, keyH);
        columns.clear();
        bg = new Background(this);
        gameStarted = true;
    }
    public void showGameOver(Graphics2D g2){
        endGame();
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 70));
        g2.drawString("GAME OVER", (screenWidth - g2.getFontMetrics().stringWidth("GAME OVER"))/2,
                (screenHeight)/2 - screenHeight/15);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 40));
        g2.drawString("Press Space to restart", (screenWidth - g2.getFontMetrics().stringWidth("Press Space to restart"))/2,
                (screenHeight)/2 + screenHeight/15);
    }

    public void showStartInfo(Graphics2D g2){
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 45));
        String text = "Press Space to Start";
        int x = (screenWidth - g2.getFontMetrics().stringWidth(text))/2;
        int y = (screenHeight - g2.getFontMetrics().getHeight())/2;
        g2.drawString(text, x, y);
    }

    public void drawScore(Graphics2D g2){
        g2.setColor(Color.BLUE);
        g2.setFont(new Font(FONT_NAME, Font.BOLD, 70));
        String text = Integer.toString(score);
        g2.drawString(text, (screenWidth - g2.getFontMetrics().stringWidth(text))/2, screenHeight/4);
    }

    public void update(double deltaTime){

        bg.update();
        player.update(deltaTime);
        if(timerCurrent >= timerTotal){
            addColumn();
            timerCurrent -= timerTotal;
        }
        else{
            timerCurrent += deltaTime;
        }
        for(Column c : columns){
            c.update();
        }
        columns.removeIf(Column::isMarkedForDeletion);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        bg.draw(g2);

        player.draw(g2);
        for(Column c : columns){
            c.draw(g2);
        }
        drawScore(g2);

        if(!gameStarted)
            showStartInfo(g2);
        if(gameOver)
            showGameOver(g2);

        g2.dispose();

    }

    @Override
    public void run() {

        double drawInterval = (double)1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while(gameThread != null){
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            lastTime = currentTime;
            if(delta>1){
                update(delta);
                repaint();
                delta--;
            }

        }

    }

    public Player getPlayer(){
        return player;
    }


}
