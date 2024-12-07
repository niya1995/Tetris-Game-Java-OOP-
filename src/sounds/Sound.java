package sounds;


import javax.sound.sampled.*;

public class Sound {
    private static Clip clpBomb;
    private static Clip clpMusicBackground;

    // Method to initialize and load the sounds
    public static void initializeSounds() {
        clpBomb = clipForLoopFactory("explosion-02.wav");
        clpMusicBackground = clipForLoopFactory("tetris_tone_loop_1_.wav");
    }

    // Method to load a sound clip
    public static Clip clipForLoopFactory(String soundFile) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Sound.class.getResource(soundFile));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to play bomb sound
    public static void playBombSound() {
        if (clpBomb != null) {
            clpBomb.stop();            // Stop if already playing
            clpBomb.flush();           // Reset sound buffer
            clpBomb.setFramePosition(0); // Start from beginning
            clpBomb.start();           // Start playing
        }
    }

    // Method to play background music
    public static void playBackgroundMusic(boolean isMuted) {
        if (!isMuted && clpMusicBackground != null) {
            clpMusicBackground.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music if not muted
        }
    }

    // Method to stop all looping sounds (such as background music and bomb sounds)
    public static void stopLoopingSounds(Clip... clpClips) {
        for (Clip clp : clpClips) {
            if (clp != null) {
                clp.stop();  // Stop each clip
            }
        }
    }

    // Method to mute background music
    public static void muteBackgroundMusic() {
        if (clpMusicBackground != null) {
            clpMusicBackground.stop();  // Stop the background music
        }
    }

    // Method to handle toggle mute functionality
    public static void toggleMute(boolean isMuted) {
        if (isMuted) {
            stopLoopingSounds(clpMusicBackground, clpBomb); // Stop all looping sounds
        } else {
            playBackgroundMusic(false);  // Play background music if not muted
        }
    }
}
