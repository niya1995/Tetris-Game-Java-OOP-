package mvc.model;

public class Templates {
    public static final boolean[][][] T_SHAPE = {
        {
            {false, false, true, false},
            {false, true, true, false},
            {false, false, true, false},
            {false, false, false, false}
        },
        {
            {false, false, false, false},
            {false, true, true, true},
            {false, false, true, false},
            {false, false, false, false}
        },
        {
            {false, false, true, false},
            {false, false, true, true},
            {false, false, true, false},
            {false, false, false, false}
        },
        {
            {false, false, true, false},
            {false, true, true, true},
            {false, false, false, false},
            {false, false, false, false}
        }
    };

    public static final boolean[][][] Z_SHAPE = {
        {
            {false, true, true, false},
            {false, false, true, true},
            {false, false, false, false},
            {false, false, false, false}
        },
        {
            {false, false, true, false},
            {false, true, true, false},
            {false, true, false, false},
            {false, false, false, false}
        },
    };

    // Add other shapes like L_SHAPE, O_SHAPE, etc.
}
