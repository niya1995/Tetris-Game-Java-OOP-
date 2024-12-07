package sounds;

import javax.sound.sampled.*;

public class Sound {

    // Method for playing individual sounds (non-looped)
    public static synchronized void playSound(final String strPath) {
        new Thread(() -> {
            try {
                Clip clp = AudioSystem.getClip();
                AudioInputStream aisStream = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream(strPath));
                
                // Debugging output
                if (aisStream == null) {
                    System.err.println("Error: Audio file not found at path: " + strPath);
                    return;
                } else {
                    System.out.println("Audio stream loaded successfully for: " + strPath);
                }

                clp.open(aisStream);
                clp.start();
            } catch (Exception e) {
                System.err.println("Error in playSound method: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    // Method for looping sound clips
    public static Clip clipForLoopFactory(String strPath) {
        Clip clp = null;

        try {
            AudioInputStream aisStream = AudioSystem.getAudioInputStream(Sound.class.getResourceAsStream(strPath));
            
            // Debugging output
            if (aisStream == null) {
                System.err.println("Error: Audio file not found at path: " + strPath);
                return null;
            } else {
                System.out.println("Audio stream loaded successfully for looping: " + strPath);
            }

            clp = AudioSystem.getClip();
            clp.open(aisStream);
        } catch (Exception e) {
            System.err.println("Error in clipForLoopFactory method: " + e.getMessage());
            e.printStackTrace();
        }

        return clp;
    }
}
