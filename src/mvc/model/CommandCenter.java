package mvc.model;

import mvc.controller.Game;



public class CommandCenter {

    private long mHighScore;
    private int mThreshold;
    private long mScore;
    private boolean mPlaying;
    private boolean mPaused;
    private boolean mLoaded;
    private boolean mGameOver;

    private int rowClearScore = 1000;;
    private int bombScore = 500;


    private static CommandCenter instance = null;


    // Constructor made private - static Utility class only
    private CommandCenter() {
    }


    public static CommandCenter getInstance() {
        if (instance == null) {
            instance = new CommandCenter();
        }
        return instance;
    }


    public void initGame() {
        setScore(0);
        setThreshold(2000);

    }


    public int getThreshold() {
        return mThreshold;
    }

    public void setThreshold(int nThresh) {
        this.mThreshold = nThresh;
    }


    public long getHighScore() {
        return mHighScore;
    }

    public void setRowClearScore() {
        mScore += rowClearScore;
    }

    public void setbombScore() {
        mScore += bombScore;
    }

    public void setHighScore(long lHighScore) {
        this.mHighScore = lHighScore;
    }


    public boolean isLoaded() {
        return mLoaded;
    }

    public void setLoaded(boolean bLoaded) {
        this.mLoaded = bLoaded;
    }


    public boolean isGameOver() {
        return mGameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.mGameOver = gameOver;
    }


    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean bPlaying) {
        this.mPlaying = bPlaying;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public void setPaused(boolean bPaused) {
        this.mPaused = bPaused;
    }


    public long getScore() {
        return mScore;
    }

    public void setScore(long lParam) {
        mScore = lParam;
    }

    public void addScore(long lParam) {
        mScore += lParam;
    }

    //	once the score gets above threshold, the game gets quicker
    public void checkThreshold() {
        if (mScore >= mThreshold) {
            if (Game.getAutoDelay() > 30) { // minimum delay limit.
                Game.setnAutoDelay(Game.getAutoDelay() - 50); // Increase speed by reducing the delay.
            }
            mThreshold += 2000;
        }
    }


}
