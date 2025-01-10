package mvc.model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class GameOpsList<T> {

    private final BlockingQueue<T> queue;
    private final Consumer<T> processor;
    private volatile boolean running; // For controlling the processing loop

    public GameOpsList(Consumer<T> processor) {
        if (processor == null) {
            throw new IllegalArgumentException("Processor cannot be null");
        }
        this.queue = new LinkedBlockingQueue<>(100); // Example capacity
        this.processor = processor;
        this.running = true;

        // Start a separate thread to process the queue
        new Thread(this::processQueue).start();
    }

    public void enqueue(T operation) {
        try {
            queue.put(operation); // Enqueue the operation (blocks if full)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Graceful handling of interruption
        }
    }

    public T dequeue() {
        try {
            return queue.take(); // Blocks if the queue is empty
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Graceful handling of interruption
            return null; // Or handle the error accordingly
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    private void processQueue() {
        while (running) {
            try {
                T operation = queue.take(); // Blocks if the queue is empty
                processor.accept(operation); // Delegate processing to the provided processor
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        running = false;
    }
}
