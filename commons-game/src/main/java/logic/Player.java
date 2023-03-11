package logic;

import gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Player {
    boolean isAlive = true;
    BufferedImage imgR;
    BufferedImage imgL;
    BufferedImage img;
    int x;
    int y;
    int sideSize;
    int xSpeed;
    double ySpeed;
    int maxXSpeed;
    double maxYSpeed;
    double weight;
    GamePanel game;
    KeyHandler kh;
    boolean deathSoundPlayed = false;

    List<Explosion> explosions;
    public Player(GamePanel gamePanel, KeyHandler keyHandler){
        this.game = gamePanel;
        this.kh = keyHandler;
        this.explosions = new ArrayList<>();
        setDefaults();
    }

    public void setDefaults(){
        this.x = game.getScreenWidth()/2 - game.getTileSize()/2;
        this.y  = game.getScreenHeight()/2 - game.getTileSize()/2;
        this.maxXSpeed = 4;
        this.maxYSpeed = 12;
        this.weight = 0.7;
        this.xSpeed = 0;
        this.ySpeed = 0;
        try{
            imgR = ImageIO.read(new File("src/main/resources/images/yoshiR.png"));
            imgL = ImageIO.read(new File("src/main/resources/images/yoshiL.png"));
            img = imgR;
            this.sideSize = img.getWidth();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void update(double deltaTime){

        if(game.isGameStarted()){
            updateHorizontalMovement();

            updateVerticalMovement();

            if(!isAlive){
                game.setGameOver(true);
                if(!deathSoundPlayed){
                    game.getMusic().playMusic("src/main/resources/sounds/death_sound.wav", 5.0f, false);
                    deathSoundPlayed = true;
                }
            }
            if(ySpeed > 0)
                kh.keysPressed.remove(KeyEvent.VK_SPACE);
        }
        explosions.forEach(e -> e.update(deltaTime));
        explosions.removeIf(e -> e.markedForDeletion);

    }
    private void updateHorizontalMovement(){
        if(kh.keysPressed.containsKey(KeyEvent.VK_A)){
            xSpeed = -maxXSpeed;
            img = imgL;
        }
        else if(kh.keysPressed.containsKey(KeyEvent.VK_D)){
            xSpeed = maxXSpeed;
            img = imgR;
        }
        else
            xSpeed = 0;
        if(x < 0)
            x = 0;
        else if(x + sideSize > game.getScreenWidth())
            x = game.getScreenWidth() - sideSize;
        x += xSpeed;
    }
    private void updateVerticalMovement(){
        y += ySpeed;
        ySpeed += weight;
        if(kh.keysPressed.containsKey(KeyEvent.VK_SPACE)){
            game.getMusic().playMusic("src/main/resources/sounds/jump_sound.wav", -15.0f, false);
            explosions.add(new Explosion(this.x, this.y));
            ySpeed = -maxYSpeed;
            kh.keysPressed.remove(KeyEvent.VK_SPACE);
        }

        if(y > game.getGroundLevel() - sideSize){
            y = game.getGroundLevel() - sideSize;
        }
        if(y < 0)
            this.isAlive = false;
    }

    public void draw(Graphics2D g2){
        explosions.forEach(e -> e.draw(g2));
        g2.drawImage(img, x, y, sideSize, sideSize, null);
    }

    public void gameEnds() {
        this.xSpeed = 0;
        this.ySpeed = 0;
    }
}
