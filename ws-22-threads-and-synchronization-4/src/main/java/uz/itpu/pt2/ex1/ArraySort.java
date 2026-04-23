package uz.itpu.pt2.ex1;

public class ArraySort extends Thread {
    private int[] items;

    public ArraySort(int[] items) {
        this.items = items;
    }

    @Override
    public void run() {
        int n = items.length;

        // Outer cycle: determines the number of passes
        for (int i = 0; i < n; i++) {
            // Inner cycle: Selection sort for descending order
            for (int j = i + 1; j < n; j++) {
                if (items[j] > items[i]) {
                    // Swap elements
                    int temp = items[i];
                    items[i] = items[j];
                    items[j] = temp;
                }
            }

            // After each iteration, the element at items[i] is in its final position.
            // Release a permit to let the main thread print the current state.
            SemaphoreDemo.sortSemaphore.release();

            // Small sleep to ensure the main thread has time to acquire the permit
            // and print before this thread continues to the next iteration.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

