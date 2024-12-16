package mvc.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameOpsList {

    // Use BlockingQueue instead of LinkedList
    private final BlockingQueue<CollisionOp> queue;

    public GameOpsList() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void enqueue(Movable mov, CollisionOp.Operation operation) {
        // Add operation to the queue
        try {
            queue.put(new CollisionOp(mov, operation)); // Blocks if the queue is full
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle thread interruption
        }
    }

    public CollisionOp dequeue() {
        try {
            return queue.take(); // Blocks if the queue is empty
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle thread interruption
            return null; // Or handle the error accordingly
        }
    }
}
