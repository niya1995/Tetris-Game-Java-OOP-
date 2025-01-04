package mvc.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameOpsList<T> {

    // Use BlockingQueue to hold operations
    private final BlockingQueue<T> queue;

    public GameOpsList() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void enqueue(T operation) {
        // Add operation to the queue
        try {
            queue.put(operation); // Blocks if the queue is full
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle thread interruption
        }
    }

    public T dequeue() {
        try {
            return queue.take(); // Blocks if the queue is empty
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Handle thread interruption
            return null; // Or handle the error accordingly
        }
    }
}
