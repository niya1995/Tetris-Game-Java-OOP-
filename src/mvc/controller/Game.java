package mvc.controller;

import mvc.model.*;
import mvc.view.GamePanel;
import sounds.Sound;
import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

// ===============================================
// == This Game class is the CONTROLLER
// ===============================================

public class Game implements Runnable{

    // ===============================================
    // FIELDS
    // ===============================================

    public static final Dimension DIM = new Dimension(500, 800); //the dimension of the game.
    public static final int THRESHOLD = 2400; // threshold to increase speed as score goes up
    public static int nAutoDelay = 300; // how fast the tetrominoes come down
    public static final int TETROMINO_NUMBER = 100; // for tetromino probability of which comes next
    private GamePanel gmpPanel;
    public static Random R = new Random();
    public final static int ANIM_DELAY = 45; // milliseconds between screen updates (animation)
    //	threads for game play
    private Thread thrAnim;
    private Thread thrAutoDown;
    private Thread thrLoaded;
    final static int PRESS_DELAY = 40; // avoid double pressing
    private boolean bMuted = true;


    private static Game instance = null;


    // ===============================================
    // ==CONSTRUCTOR
    // ===============================================

    private Game() {

        gmpPanel = GamePanel.getInstance(DIM); // Pass the dimension to GamePanel's getInstance method
        GameKeyListener keyListener = new GameKeyListener(this, gmpPanel); // Pass Game and GamePanel to KeyListener
        gmpPanel.addKeyListener(keyListener);
        Sound.initializeSounds();

    }

    // ===============================================
    // ==METHODS
    // ===============================================
    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            try {
                Game game = Game.getInstance(); // construct itself
                game.fireUpThreads(); // Initialize and start the game threads

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void fireUpThreads() { // called initially
        if (thrAnim == null) {
            thrAnim = new Thread(this); // pass the the thread a runnable object (this)
            thrAnim.start();
        }
        if (thrAutoDown == null ) {
            thrAutoDown = new Thread(this);
            thrAutoDown.start();
        }

        if (!CommandCenter.getInstance().isLoaded() && thrLoaded == null) {
            thrLoaded = new Thread(this);
            thrLoaded.start();
        }
    }

    // implements runnable - must have run method
    public void run() {

        // lower this thread's priority; let the "main" aka 'Event Dispatch'
        // thread do what it needs to do first
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        // and get the current time
        long lStartTime = System.currentTimeMillis();
        if (!CommandCenter.getInstance().isLoaded() && Thread.currentThread() == thrLoaded) {
            CommandCenter.getInstance().setLoaded(true);
        }

        // thread animates the scene
        while (Thread.currentThread() == thrAutoDown) {
            if (!CommandCenter.getInstance().isPaused() && CommandCenter.getInstance().isPlaying()) {
                tryMovingDown();
            }
            gmpPanel.repaint();
            try {
                lStartTime += nAutoDelay;
                Thread.sleep(Math.max(0, lStartTime - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
        }
        while (Thread.currentThread() == thrAnim) {
            if (!CommandCenter.getInstance().isPaused() && CommandCenter.getInstance().isPlaying()) {
                updateGrid();
            }
            gmpPanel.repaint();


            try {
                // The total amount of time is guaranteed to be at least ANIM_DELAY long.  If processing (update)
                // between frames takes longer than ANIM_DELAY, then the difference between lStartTime -
                // System.currentTimeMillis() will be negative, then zero will be the sleep time
                lStartTime += ANIM_DELAY;
                Thread.sleep(Math.max(0, lStartTime - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                // just skip this frame -- continue;
                break;
            }
        } // end while
    } // end run

    private void updateGrid() {
        gmpPanel.grid.setBlocks(gmpPanel.tetrCurrent);

    }


    private void tryMovingDown() {
		//uses a test tetromino to see if can move down in board
        Tetromino tetrTest = gmpPanel.tetrCurrent.cloneTetromino();
        tetrTest.moveDown();
        if (gmpPanel.grid.requestDown(tetrTest)) {
            gmpPanel.tetrCurrent.moveDown();
            tetrTest = null;
        }
		//once bomb hits the bottom, plays bomb noise, clears the board and adds to score
        else if (CommandCenter.getInstance().isPlaying() && gmpPanel.tetrCurrent instanceof Bomb) {
            Sound.playBombSound();
            gmpPanel.grid.clearGrid();
            CommandCenter.getInstance().addScore(1000);
            // sets high score
            if (CommandCenter.getInstance().getHighScore() < CommandCenter.getInstance().getScore()) {
                CommandCenter.getInstance().setHighScore(CommandCenter.getInstance().getScore());
            }
            gmpPanel.tetrCurrent = gmpPanel.tetrOnDeck;
            gmpPanel.tetrOnDeck = createNewTetromino();
            tetrTest = null;
        }
//		once a tetromino hits the bottom, check if game is over (top row)
//  check if any full rows completed, generate new tetromino for on deck, switch on deck to current
        else if (CommandCenter.getInstance().isPlaying()) {
            gmpPanel.grid.addToOccupied(gmpPanel.tetrCurrent);
            gmpPanel.grid.checkTopRow();
            gmpPanel.grid.checkCompletedRow();
            gmpPanel.tetrCurrent = gmpPanel.tetrOnDeck;
            gmpPanel.tetrOnDeck = createNewTetromino();
            tetrTest = null;
        } else {
            tetrTest = null;
        }

    }

    public void gettryMovingDown() {
        tryMovingDown();  // Calls the private tryMovingDown method internally
    }


    // Called when user presses 'space'
    private void startGame() {
        gmpPanel.tetrCurrent = createNewTetromino();
        gmpPanel.tetrOnDeck = createNewTetromino();

        CommandCenter.getInstance().clearAll();
        CommandCenter.getInstance().initGame();
        CommandCenter.getInstance().setPlaying(true);
        CommandCenter.getInstance().setPaused(false);
        CommandCenter.getInstance().setGameOver(false);

        if (thrAutoDown == null || !thrAutoDown.isAlive()) {
            thrAutoDown = new Thread(this);
            thrAutoDown.start();
        }

        if (!bMuted)
            Sound.stopLoopingSounds();
    }

    public void triggerStartGame() {
        startGame();  // Calls the private startGame method internally
    }


    // creates the next tetromino from the different options available
    private Tetromino createNewTetromino() {
        int nKey = R.nextInt(TETROMINO_NUMBER);
        if (nKey <= 12) return new LongPiece();
        else if (nKey <= 23) return new SquarePiece();
        else if (nKey <= 35) return new SPiece();
        else if (nKey <= 46) return new TPiece();
        else if (nKey <= 58) return new ZPiece();
        else if (nKey <= 71) return new LPiece();
        else if (nKey <= 84) return new JPiece();
        else if (nKey <= 98) return new PlusPiece();
        else return new Bomb();
    }
    


private void moveTetromino(Supplier<Tetromino> tetrominoSupplier, Consumer<Tetromino> action) {
    Tetromino tetrTest = tetrominoSupplier.get();
    action.accept(tetrTest); // Test the action on the clone
    if (gmpPanel.grid.requestLateral(tetrTest)) {
        action.accept(gmpPanel.tetrCurrent); // Apply the action to the actual Tetromino
    }
}

public void tryMoveTetromino(Supplier<Tetromino> tetrominoSupplier, Consumer<Tetromino> action) {
    moveTetromino(tetrominoSupplier, action);  // Call the private method
}


private void toggleMute() {
    bMuted = !bMuted; // Toggle the mute state
    
    if (bMuted) {
        // If muted, stop all looping sounds
        Sound.toggleMute(bMuted); // This will stop all sounds
    } else {
        // If unmuted, resume playing background music
        Sound.playBackgroundMusic(bMuted); // This will start background music if not muted
    }
}

public void gettoggleMute(){
    toggleMute();
}


}


