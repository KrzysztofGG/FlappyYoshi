package logic;

import gui.GamePanel;
import lombok.Getter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class KeyHandler implements KeyListener {
    @Getter
    Map<Integer, Boolean> keysPressed;
    GamePanel gamePanel;

    public KeyHandler(GamePanel gamePanel){
        this.gamePanel = gamePanel;
        this.keysPressed = new HashMap<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //not required in game logic
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if(!gamePanel.isGameStarted()){
            if(code == KeyEvent.VK_SPACE) {
                gamePanel.startGame();
            }
        }
        else if(gamePanel.isGameOver()){
            if(code == KeyEvent.VK_SPACE)
                gamePanel.gameRestarts();
        }
        else {
            if((code == KeyEvent.VK_D ||
                    code == KeyEvent.VK_A ||
                    code == KeyEvent.VK_SPACE) &&
            !keysPressed.containsKey(code)){
                keysPressed.put(code, true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_D ||
                code == KeyEvent.VK_A ||
                code == KeyEvent.VK_SPACE){
            keysPressed.remove(code);
        }
    }
}

