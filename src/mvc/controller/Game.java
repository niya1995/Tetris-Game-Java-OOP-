package mvc.controller;

import mvc.model.*;
import mvc.view.GamePanel;
import sounds.Sound;
import java.awt.*;
import java.util.Random;


public class Game implements Runnable{

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

    public Tetromino tetrCurrent;
    public TetrominoCloner tetrominoCloner;


    private static Game instance = null;

    private Game() {

        gmpPanel = GamePanel.getInstance(DIM); // Pass the dimension to GamePanel's getInstance method
        GameKeyListener keyListener = new GameKeyListener(this, gmpPanel); // Pass Game and GamePanel to KeyListener
        gmpPanel.addKeyListener(keyListener);
        Sound.initializeSounds();
        tetrominoCloner = new TetrominoCloner();

    }

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
        // Lower this thread's priority; let the main thread do what it needs to do first
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    
        // Get the current time
        long startTime = System.currentTimeMillis();
    
        // Handle loading logic
        loadGameIfNecessary(startTime);
    
        // Handle auto-down thread logic
        handleAutoDownThread(startTime);
    
        // Handle animation thread logic
        handleAnimationThread(startTime);
    }
    
    private void loadGameIfNecessary(long startTime) {
        if (!CommandCenter.getInstance().isLoaded() && Thread.currentThread() == thrLoaded) {
            CommandCenter.getInstance().setLoaded(true);
        }
    }
    
    private void handleAutoDownThread(long startTime) {
        while (Thread.currentThread() == thrAutoDown) {
            if (shouldProcessAutoDown()) {
                tryMovingDown();
            }
            gmpPanel.repaint();
            sleepForNextFrame(startTime, nAutoDelay);
        }
    }
    
    private void handleAnimationThread(long startTime) {
        while (Thread.currentThread() == thrAnim) {
            if (shouldProcessAnimation()) {
                updateGrid();
            }
            gmpPanel.repaint();
            sleepForNextFrame(startTime, ANIM_DELAY);
        }
    }
    
    private boolean shouldProcessAutoDown() {
        return !CommandCenter.getInstance().isPaused() && CommandCenter.getInstance().isPlaying();
    }
    
    private boolean shouldProcessAnimation() {
        return !CommandCenter.getInstance().isPaused() && CommandCenter.getInstance().isPlaying();
    }
    
    private void sleepForNextFrame(long startTime, long delay) {
        try {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = Math.max(0, delay - elapsedTime % delay); // Ensure sleep time is correct
            Thread.sleep(remainingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle interruption properly
        }
    }//end run

    private void updateGrid() {
        gmpPanel.grid.setBlocks(tetrCurrent);

    }


    private void tryMovingDown() {
        // Uses a test tetromino to see if it can move down on the board
        Tetromino tetrTest = tetrominoCloner.cloneTetromino(tetrCurrent);
        tetrTest.moveDown();
        
        // Check if the tetromino can move down without colliding
        if (gmpPanel.grid.requestDown(tetrTest)) {
            tetrCurrent.moveDown(); // Move the actual tetromino down
        }
        // Handle the "bomb" tetromino separately
        else if (CommandCenter.getInstance().isPlaying() && tetrCurrent instanceof Bomb) {
            handleBombTetromino();
        }
        // Handle regular tetrominoes
        else if (CommandCenter.getInstance().isPlaying()) {
            handleRegularTetromino();
        }
    }
    
    // Method to handle the bomb tetromino logic
    private void handleBombTetromino() {
        Sound.playBombSound(); // Play bomb sound
        gmpPanel.grid.clearGrid(); // Clear the grid
        CommandCenter.getInstance().addScore(500); // Add score for bomb
        updateHighScore(); // Update high score if necessary
        tetrCurrent = gmpPanel.tetrOnDeck; // Set current tetromino to on-deck piece
        gmpPanel.tetrOnDeck = createNewTetromino(); // Generate a new on-deck tetromino
    }
    
    // Method to handle regular tetromino logic
    private void handleRegularTetromino() {
        gmpPanel.grid.addToOccupied(tetrCurrent); // Add tetromino to occupied blocks
        gmpPanel.grid.checkTopRow(); // Check if top row is filled
        gmpPanel.grid.checkCompletedRow(); // Check if any row is completed
        tetrCurrent = gmpPanel.tetrOnDeck; // Set current tetromino to on-deck piece
        gmpPanel.tetrOnDeck = createNewTetromino(); // Generate a new on-deck tetromino
    }
    
    
    private void updateHighScore() {
        // Update high score if necessary
        if (CommandCenter.getInstance().getHighScore() < CommandCenter.getInstance().getScore()) {
            CommandCenter.getInstance().setHighScore(CommandCenter.getInstance().getScore());
        }
    }
    

    public void gettryMovingDown() {
        tryMovingDown();  // Calls the private tryMovingDown method internally
    }


    // Called when the user presses 'space' to start the game
    private void startGame() {
        initializeTetrominos();
        resetGameState();
        startAutoDownThread();
        stopLoopingSounds();
    }

    // Initializes the current and on-deck tetrominos
    private void initializeTetrominos() {
        tetrCurrent = createNewTetromino();
        gmpPanel.tetrOnDeck = createNewTetromino();
    }

    // Resets the game state through the CommandCenter
    private void resetGameState() {
        CommandCenter.getInstance().clearAll();
        CommandCenter.getInstance().initGame();
        CommandCenter.getInstance().setPlaying(true);
        CommandCenter.getInstance().setPaused(false);
        CommandCenter.getInstance().setGameOver(false);
    }

    // Starts the auto-down thread if it is not already running
    private void startAutoDownThread() {
        if (thrAutoDown == null || !thrAutoDown.isAlive()) {
            thrAutoDown = new Thread(this);
            thrAutoDown.start();
        }
    }

    // Stops any looping sounds if the sound is not muted
    private void stopLoopingSounds() {
        if (!Sound.isMuted()) {
            Sound.stopLoopingSounds();
        }
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
    


    private void moveTetromino(Tetromino tetrTest, String actionType) {
    // Perform the requested action
    performAction(tetrTest, actionType);

    // Now check if the move is valid and apply it to the actual Tetromino
    if (isMoveValid(tetrTest)) {
        applyAction(actionType);
    }
}

// Method to perform the action on the test Tetromino
    private void performAction(Tetromino tetrTest, String actionType) {
        switch (actionType) {
            case "moveRight":
                tetrTest.moveRight();
                break;
            case "moveLeft":
                tetrTest.moveLeft();
                break;
            case "rotate":
                tetrTest.rotate();
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + actionType);
        }
    }

    // Method to check if the requested move is valid
    private boolean isMoveValid(Tetromino tetrTest) {
        return gmpPanel.grid.requestLateral(tetrTest);
    }

    // Method to apply the action to the current Tetromino
    private void applyAction(String actionType) {
        switch (actionType) {
            case "moveRight":
                tetrCurrent.moveRight();
                break;
            case "moveLeft":
                tetrCurrent.moveLeft();
                break;
            case "rotate":
                tetrCurrent.rotate();
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + actionType);
        }
    }


    public void tryMoveTetromino(String actionType) {
        Tetromino tetrTest = tetrominoCloner.cloneTetromino(tetrCurrent);;  // Clone the actual Tetromino
        moveTetromino(tetrTest, actionType);  // Pass the cloned Tetromino and action type
    }
    


}


