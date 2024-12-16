package mvc.model;

import java.util.Objects;

public class CollisionOp {

    // Enum Operation can stay as-is, but making it final is a good practice.
    public enum Operation {
        ADD, REMOVE
    }

    // Marking fields as final for immutability
    private final Movable mMovable;
    private final Operation mOperation;

    // Constructor: Validate inputs to ensure the object is always in a valid state.
    public CollisionOp(Movable movable, Operation operation) {
        if (movable == null || operation == null) {
            throw new IllegalArgumentException("Movable and operation cannot be null");
        }
        this.mMovable = movable;
        this.mOperation = operation;
    }

    // Getters
    public Movable getMovable() {
        return mMovable;
    }

    public Operation getOperation() {
        return mOperation;
    }

    // Override toString for better debugging output
    @Override
    public String toString() {
        return "CollisionOp{movable=" + mMovable + ", operation=" + mOperation + '}';
    }

    // Override equals and hashCode for correct behavior in collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CollisionOp that = (CollisionOp) obj;
        return mMovable.equals(that.mMovable) && mOperation == that.mOperation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mMovable, mOperation);
    }
}
