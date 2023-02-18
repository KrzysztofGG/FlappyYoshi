package logic;

import gui.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Background {

    GamePanel game;
    ArrayList<Layer> layers = new ArrayList<>();
    ArrayList<BufferedImage> images = new ArrayList<>();

    ArrayList<Image> scaledImages = new ArrayList<>();

    public Background(GamePanel gamePanel){
        this.game = gamePanel;
        loadImages();
        createLayers();
    }

    public void loadImages() {
        try{
            images.add(ImageIO.read(new File("src/main/resources/images/landscape.png")));
            images.add(ImageIO.read(new File("src/main/resources/images/ground3.png")));


        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
          there was supposed to be scaling were,
          but I guess it's redundant now
         */

        scaledImages.addAll(images);

    }

    public void createLayers(){
        layers.add(new Layer(1, scaledImages.get(0), game, 0.2));
        layers.add(new Layer(2, scaledImages.get(1), game, 1));
    }
    public void update(){
        for(Layer layer: layers){
            layer.update();
        }
    }
    public void draw(Graphics2D g2){
        for(Layer layer: layers){
            layer.draw(g2);
        }
    }

    public void gameEnds(){
        layers.forEach(l -> l.speed = 0);
    }

}

class Layer{
    Image layerImage;
    GamePanel game;
    int x;
    int y;
    int width;
    int height;
    double speed;
    double speedModifier;
    int id;

    public Layer(int id, Image image, GamePanel gamePanel, double speedModifier){
        this.id = id;
        this.layerImage = image;
        this.game = gamePanel;
        this.speedModifier = speedModifier;
        this.speed = game.getGameSpeed() * speedModifier;
        this.width = image.getWidth(null);
        this.height = image.getHeight(null);
        if(id == 1){
            this.x = 0;
            this.y = game.getGroundLevel() - height + 100;
        } else if (id == 2) {
            this.x = 0;
            this.y = game.getGroundLevel();
        }
    }

    public void update(){
        if(this.x <= -width)
            this.x = 0;
        this.x = (int) Math.floor(this.x - this.speed);
    }
    public void draw(Graphics2D g2){
        // there were supposed to be more layers, temporary solution :/
        if(id == 1){
            g2.drawImage(layerImage, x, y, width, height, null);
            g2.drawImage(layerImage, x + width, y, width, height,null);
        }
        else if(id == 2){
            g2.drawImage(layerImage, x, y, width, game.getScreenHeight() - game.getGroundLevel(), null);
            g2.drawImage(layerImage, x + width, y, width, game.getScreenHeight() - game.getGroundLevel(),null);
        }

    }

}
