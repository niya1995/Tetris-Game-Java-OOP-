package mvc.model;

import java.awt.*;


public class LPiece extends Tetromino{
    public LPiece() {
        super();
        initialize();
        setColor(Color.green);
    }

    public void initialize() {
        for (int i = 0; i < DIM; i++) {
            if (i == 0) {
                setColoredSquare(i, 0, 0, false);
                setColoredSquare(i, 0, 1, false);
                setColoredSquare(i, 0, 2, false);
                setColoredSquare(i, 0, 3, false);
            
                setColoredSquare(i, 1, 0, true);
                setColoredSquare(i, 1, 1, true);
                setColoredSquare(i, 1, 2, true);
                setColoredSquare(i, 1, 3, false);
            
                setColoredSquare(i, 2, 0, false);
                setColoredSquare(i, 2, 1, false);
                setColoredSquare(i, 2, 2, true);
                setColoredSquare(i, 2, 3, false);
            
                setColoredSquare(i, 3, 0, false);
                setColoredSquare(i, 3, 1, false);
                setColoredSquare(i, 3, 2, false);
                setColoredSquare(i, 3, 3, false);
            
            } else if (i == 1) {
                setColoredSquare(i, 0, 0, false);
                setColoredSquare(i, 0, 1, false);
                setColoredSquare(i, 0, 2, false);
                setColoredSquare(i, 0, 3, false);
            
                setColoredSquare(i, 1, 0, false);
                setColoredSquare(i, 1, 1, false);
                setColoredSquare(i, 1, 2, true);
                setColoredSquare(i, 1, 3, true);
            
                setColoredSquare(i, 2, 0, false);
                setColoredSquare(i, 2, 1, false);
                setColoredSquare(i, 2, 2, true);
                setColoredSquare(i, 2, 3, false);
            
                setColoredSquare(i, 3, 0, false);
                setColoredSquare(i, 3, 1, false);
                setColoredSquare(i, 3, 2, true);
                setColoredSquare(i, 3, 3, false);
            
            } else if (i == 2) {
                setColoredSquare(i, 0, 0, false);
                setColoredSquare(i, 0, 1, false);
                setColoredSquare(i, 0, 2, false);
                setColoredSquare(i, 0, 3, false);
            
                setColoredSquare(i, 1, 0, false);
                setColoredSquare(i, 1, 1, true);
                setColoredSquare(i, 1, 2, false);
                setColoredSquare(i, 1, 3, false);
            
                setColoredSquare(i, 2, 0, false);
                setColoredSquare(i, 2, 1, true);
                setColoredSquare(i, 2, 2, true);
                setColoredSquare(i, 2, 3, true);
            
                setColoredSquare(i, 3, 0, false);
                setColoredSquare(i, 3, 1, false);
                setColoredSquare(i, 3, 2, false);
                setColoredSquare(i, 3, 3, false);
            
            } else {
                setColoredSquare(i, 0, 0, false);
                setColoredSquare(i, 0, 1, false);
                setColoredSquare(i, 0, 2, false);
                setColoredSquare(i, 0, 3, true);
            
                setColoredSquare(i, 1, 0, false);
                setColoredSquare(i, 1, 1, false);
                setColoredSquare(i, 1, 2, false);
                setColoredSquare(i, 1, 3, true);
            
                setColoredSquare(i, 2, 0, false);
                setColoredSquare(i, 2, 1, false);
                setColoredSquare(i, 2, 2, true);
                setColoredSquare(i, 2, 3, true);
            
                setColoredSquare(i, 3, 0, false);
                setColoredSquare(i, 3, 1, false);
                setColoredSquare(i, 3, 2, false);
                setColoredSquare(i, 3, 3, false);
            }
            
        }

    }
}
