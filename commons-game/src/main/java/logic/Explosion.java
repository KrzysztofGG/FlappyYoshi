package logic;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Explosion {
    int x;
    int y;
    List<BufferedImage> frames;
    int frame = 0;
    double frameInterval = 7;
    double frameTimer = 0;
    boolean markedForDeletion = false;
    public Explosion(int x, int y){
        this.x = x;
        this.y = y;
        loadAssets();
    }
    private void loadAssets() {
        frames = new ArrayList<>();
        for(int i=1; i<9; i++){
            try {
                frames.add(ImageIO.read(new File(String.format("src/main/resources/images/explosion/%d.png", i))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void draw(Graphics2D g2){
        g2.drawImage(frames.get(frame), x, y, null);
    }
    public void update(double deltaTime){
        this.frameTimer += deltaTime;
        if(frameTimer > frameInterval){
            if(frame < 7) {
                frame++;
                frameTimer = 0;
            }
            else{
                markedForDeletion = true;
            }

        }
    }
}
