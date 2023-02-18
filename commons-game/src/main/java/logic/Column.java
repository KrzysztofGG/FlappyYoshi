package logic;

import gui.GamePanel;
import lombok.Getter;

import java.awt.*;

public class Column {
    int x;
    int y;
    int width;
    int height;

    boolean isCrossed ;

    boolean isBottom;
    double columnSpeed;
    GamePanel game;

    @Getter
    boolean markedForDeletion = false;

    int mistakeMargin = 3;

    public Column(GamePanel gamePanel, int height, boolean isBottom){
        this.game = gamePanel;
        this.isBottom = isBottom;
        setDefaults(isBottom, height);
    }
    public void setDefaults(boolean isBottom, int height){
        this.width = 64;

        this.height = height;
        this.x = game.getScreenWidth();
        if(isBottom)
            this.y = game.getGroundLevel() - this.height;
        else
            this.y = 0;
        this.columnSpeed = game.isGameStarted() ? game.getGameSpeed() : 0;
        this.isCrossed = false;

    }


    public void update(){
        x -= columnSpeed;
        if(x < -width)
            markedForDeletion = true;
        checkCollision();
        updateScore();
    }
    public void checkCollision(){
        Player p = game.getPlayer();
        if(this.x < p.x + p.sideSize - mistakeMargin * 1.5 && this.x > p.x - this.width + mistakeMargin &&
            this.y + this.height >= p.y + mistakeMargin && this.y < p.y + p.sideSize - mistakeMargin * 1.5) {
            p.isAlive = false;
        }
    }
    public void updateScore(){
        if(this.x + this.width < game.getPlayer().x && isBottom && !isCrossed){
            game.setScore(game.getScore() + 1);
            isCrossed = true;
        }
    }
    public void draw(Graphics2D g2){
        g2.setColor(new Color(0, 170, 0));
        g2.fillRect(x, y, width, height);
        g2.setColor(new Color(0, 128, 0));
        if(isBottom)
            g2.fillRect(x, y, width, game.getScreenHeight()/20);
        else
            g2.fillRect(x, y + height - game.getScreenHeight()/20, width, game.getScreenHeight()/20);
    }

    public void gameEnds() {
        this.columnSpeed = 0;
    }
}
