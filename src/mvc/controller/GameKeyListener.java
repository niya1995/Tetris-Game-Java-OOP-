package mvc.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import sounds.Sound;

import mvc.model.CommandCenter;
import mvc.model.Tetromino;
import mvc.view.GamePanel;

public class GameKeyListener implements KeyListener {

    private long lTime; // time stamp
    private long lTimeStep;
    final static int PRESS_DELAY = 40; // avoid double pressing

    private Game game; // Reference to the Game class
    private GamePanel gmpPanel;

    // Constructor now expects both Game and GamePanel to be passed
    public GameKeyListener(Game game, GamePanel gmpPanel) {
        if (game == null || gmpPanel == null) {
            throw new IllegalArgumentException("Game or GamePanel cannot be null");
        }
        this.game = game;
        this.gmpPanel = gmpPanel;
    }

    // Enum representing the key actions
    public enum KeyAction {
        PAUSE(KeyEvent.VK_P),
        QUIT(KeyEvent.VK_Q),
        LEFT(KeyEvent.VK_LEFT),
        RIGHT(KeyEvent.VK_RIGHT),
        START(KeyEvent.VK_SPACE),
        MUTE(KeyEvent.VK_M),
        DOWN(KeyEvent.VK_DOWN),
        UP(KeyEvent.VK_UP);

        private final int keyCode;

        KeyAction(int keyCode) {
            this.keyCode = keyCode;
        }

        public int getKeyCode() {
            return keyCode;
        }
    }

    // Mapping of key codes to actions
    private final Map<Integer, KeyAction> keyMappings = new HashMap<>();

    // Initialize key mappings
    public void initializeKeyMappings() {
        for (KeyAction action : KeyAction.values()) {
            keyMappings.put(action.getKeyCode(), action);
        }
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
        if (nKeyPressed == KeyAction.START.getKeyCode() &&
            CommandCenter.getInstance().isLoaded() &&
            !CommandCenter.getInstance().isPlaying()) {
            game.triggerStartGame();
        }

        // Pause game
        if (nKeyPressed == KeyAction.PAUSE.getKeyCode() && lTime > lTimeStep + PRESS_DELAY) {
            CommandCenter.getInstance().setPaused(!CommandCenter.getInstance().isPaused());
            lTimeStep = System.currentTimeMillis();
        }

        // Quit game
        if (nKeyPressed == KeyAction.QUIT.getKeyCode() && lTime > lTimeStep + PRESS_DELAY) {
            System.exit(0);
        }

        // Move tetromino down
        if (nKeyPressed == KeyAction.DOWN.getKeyCode() &&
            lTime > lTimeStep + PRESS_DELAY - 35 &&
            CommandCenter.getInstance().isPlaying()) {
            game.gettryMovingDown();
            lTimeStep = System.currentTimeMillis();
        }

        // Move tetromino right
        if (nKeyPressed == KeyAction.RIGHT.getKeyCode() && lTime > lTimeStep + PRESS_DELAY) {
            game.tryMoveTetromino(() -> gmpPanel.tetrCurrent.cloneTetromino(), Tetromino::moveRight);
        }

        // Move tetromino left
        if (nKeyPressed == KeyAction.LEFT.getKeyCode() && lTime > lTimeStep + PRESS_DELAY) {
            game.tryMoveTetromino(() -> gmpPanel.tetrCurrent.cloneTetromino(), Tetromino::moveLeft);
        }

        // Rotate tetromino
        if (nKeyPressed == KeyAction.UP.getKeyCode()) {
            game.tryMoveTetromino(() -> gmpPanel.tetrCurrent.cloneTetromino(), Tetromino::rotate);
        }

        // Mute/Unmute background music
        if (nKeyPressed == KeyAction.MUTE.getKeyCode()) {
            Sound.toggleMuteWrapper();  // Call the public wrapper method
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Optional: Handle keyReleased if needed
    }
}
