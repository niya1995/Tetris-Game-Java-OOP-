package mvc.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import sounds.Sound;
import mvc.model.CommandCenter;
import mvc.model.GameOpsList;

public class GameKeyListener implements KeyListener {

    private long lTime; // time stamp
    private long lTimeStep;
    private final static int PRESS_DELAY = 40; // avoid double pressing

    private final GameOpsList<String> gameOpsList;

    // Constructor expects Game class
    public GameKeyListener(GameOpsList<String> gameOpsList) {

        this.gameOpsList = gameOpsList;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Optional: Handle keyTyped if needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        lTime = System.currentTimeMillis();
        int nKeyPressed = e.getKeyCode();

        // Start game
        if (nKeyPressed == KeyEvent.VK_SPACE && 
            CommandCenter.getInstance().isLoaded() &&
            !CommandCenter.getInstance().isPlaying()) {
            gameOpsList.enqueue("startGame");
        }

        // Pause game
        if (nKeyPressed == KeyEvent.VK_P && lTime > lTimeStep + PRESS_DELAY) {
            CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
            lTimeStep = System.currentTimeMillis();
        }

        // Quit game
        if (nKeyPressed == KeyEvent.VK_Q && lTime > lTimeStep + PRESS_DELAY) {
            System.exit(0);
        }

        // Move tetromino down
        if (nKeyPressed == KeyEvent.VK_DOWN && 
            lTime > lTimeStep + PRESS_DELAY - 35 && 
            CommandCenter.getInstance().isPlaying()) {
            gameOpsList.enqueue("movingDown");
            lTimeStep = System.currentTimeMillis();
        }

        // Move tetromino right
        if (nKeyPressed == KeyEvent.VK_RIGHT && lTime > lTimeStep + PRESS_DELAY) {
            gameOpsList.enqueue("moveRight");
        }

        // Move tetromino left
        if (nKeyPressed == KeyEvent.VK_LEFT && lTime > lTimeStep + PRESS_DELAY) {
            gameOpsList.enqueue("moveLeft");
        }

        // Rotate tetromino
        if (nKeyPressed == KeyEvent.VK_UP) {
            gameOpsList.enqueue("rotate");
        }

        // Mute/Unmute background music
        if (nKeyPressed == KeyEvent.VK_M) {
            Sound.toggleMuteWrapper();  // Call the public wrapper method
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Optional: Handle keyReleased if needed
    }
}
