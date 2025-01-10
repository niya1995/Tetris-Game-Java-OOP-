package mvc.controller;

import mvc.model.*;
import mvc.view.GamePanel;
import sounds.Sound;
import java.awt.*;
import java.util.Random;


public class Game implements Runnable{

    private static int nAutoDelay = 300; // how fast the tetrominoes come down
    private final GamePanel gmpPanel;
    private static final Random R = new Random();
    private final static int ANIM_DELAY = 45; // milliseconds between screen updates (animation)

    //	threads for game play
    private Thread thrAnim;
    private Thread thrAutoDown;
    private Thread thrLoaded;

    private Tetromino tetrCurrent;
    private TetrominoCloner tetrominoCloner;

    private final GameOpsList<String> gameOpsList; // String for operation types (moveRight, moveLeft, rotate, etc.)

    private static Game instance = null;

    private Game() {

        gmpPanel = GamePanel.getInstance(); // Pass the dimension to GamePanel's getInstance method
        gameOpsList = new GameOpsList<String>(this::processOperation);
        GameKeyListener keyListener = new GameKeyListener(gameOpsList); // Pass Game and GamePanel to KeyListener
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
            thrAnim = new Thread(this, "AnimationThread"); // pass the the thread a runnable object (this)
            thrAnim.start();
        }
        if (thrAutoDown == null ) {
            thrAutoDown = new Thread(this, "AutoDownThread");
            thrAutoDown.start();
        }

        if (!CommandCenter.getInstance().isLoaded() && thrLoaded == null) {
            thrLoaded = new Thread(this, "LoadingThread");
            thrLoaded.start();
        }
    }

    // implements runnable - must have run method
    public void run() {
        Thread currentThread = Thread.currentThread();
    
        // Get the current time
        long startTime = System.currentTimeMillis();
    
        if (currentThread.getName().equals("AnimationThread")) {
            currentThread.setPriority(Thread.NORM_PRIORITY); // Normal priority for smooth animations
            handleAnimationThread(startTime);
        } else if (currentThread.getName().equals("AutoDownThread")) {
            currentThread.setPriority(Thread.MIN_PRIORITY); // Lower priority for automatic Tetromino movement
            handleAutoDownThread(startTime);
        } else if (currentThread.getName().equals("LoadingThread")) {
            currentThread.setPriority(Thread.MAX_PRIORITY); // High priority during loading phase
            loadGameIfNecessary(startTime);
        }
    

    }
    
    private void loadGameIfNecessary(long startTime) {
        if (!CommandCenter.getInstance().isLoaded()) {
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

    public static void setnAutoDelay(int num){
        nAutoDelay = num;
    }
    public static int getAutoDelay(){
        return nAutoDelay;
    }
    
    private void sleepForNextFrame(long startTime, long delay) {
        try {
            long elapsedTime = System.currentTimeMillis() - startTime;
            long remainingTime = Math.max(0, delay - (elapsedTime % delay));
            Thread.sleep(remainingTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); 
        }
    }

    private void updateGrid() {
        gmpPanel.getGridObj().setBlocks(tetrCurrent);

    }
    public static Random randInstance() {
        return R;
    }

    // Initializes the current and on-deck tetrominos
    private void initializeTetrominos() {
        tetrCurrent = createNewTetromino();
        gmpPanel.tetrOnDeck = createNewTetromino();
    }

    // Resets the game state through the CommandCenter
    private void resetGameState() {
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

    // Called when the user presses 'space' to start the game
    private void startGame() {
        initializeTetrominos();
        resetGameState();
        startAutoDownThread();
        stopLoopingSounds();
    }

    private void processOperation(String operation) {
        switch (operation) {
            case "startGame":
                triggerStartGame();
                break;
            case "movingDown":
                gettryMovingDown();
                break;
            case "moveRight":
                tryMoveTetromino("moveRight");
                break;
            case "moveLeft":
                tryMoveTetromino("moveLeft");
                break;
            case "rotate":
                tryMoveTetromino("rotate");;
                break;
            default:
                System.out.println("Unknown operation: " + operation);
        }
    }

    
    private void tryMovingDown() {
        // Uses a test tetromino to see if it can move down on the board
        Tetromino tetrTest = tetrominoCloner.cloneTetromino(tetrCurrent);
        tetrTest.moveDown();
        
        // Check if the tetromino can move down without colliding
        if (gmpPanel.getGridObj().requestDown(tetrTest)) {
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
        gmpPanel.getGridObj().clearGrid(); // Clear the grid
        CommandCenter.getInstance().setbombScore(); // Add score for bomb
        updateHighScore(); // Update high score
        tetrCurrent = gmpPanel.tetrOnDeck; // Set current tetromino to on-deck piece
        gmpPanel.tetrOnDeck = createNewTetromino(); // Generate a new on-deck tetromino
    }
    
    // Method to handle regular tetromino logic
    private void handleRegularTetromino() {
        gmpPanel.getGridObj().addToOccupied(tetrCurrent); // Add tetromino to occupied blocks
        gmpPanel.getGridObj().checkTopRow(); // Check if top row is filled
        gmpPanel.getGridObj().checkCompletedRow(); // Check if any row is completed
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


    public void triggerStartGame() {
        startGame();  // Calls the private startGame method internally
    }


    // creates the next tetromino from the different options available
    private Tetromino createNewTetromino() {
        int nKey = R.nextInt(100);
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
    
    public void tryMoveTetromino(String actionType) {
        Tetromino tetrTest = tetrominoCloner.cloneTetromino(tetrCurrent);;  // Clone the actual Tetromino
        moveTetromino(tetrTest, actionType);  // Pass the cloned Tetromino and action type
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
        return gmpPanel.getGridObj().requestLateral(tetrTest);
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


}


