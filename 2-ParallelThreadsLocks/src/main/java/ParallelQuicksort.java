import java.util.*;
import java.util.concurrent.RecursiveAction;

public class ParallelQuicksort<Car> extends RecursiveAction {

    public static final int MINIMAL_PARTITION_SIZE = 15;
    Comparator<Car> comparator;
    List<Car> items;
    private final int left;
    private final int right;

    public ParallelQuicksort(List<Car> data, Comparator<Car> comparator) {
        this.items = data;
        this.left = 0;
        this.right = data.size() - 1;
        this.comparator = comparator;
    }

    public ParallelQuicksort(List<Car> data, int left, int right, Comparator<Car> comparator) {
        this.items = data;
        this.left = left;
        this.right = right;
        this.comparator = comparator;
    }

    // compute is called when this class is invoked by a threadPool
    @Override
    protected void compute() {
        int t = 0;
        int f = 0;

        if (left < right) {
            Car pivot = medianOfThree(items, left, right, comparator);
            int indexOfPartition = partition(pivot, comparator);

            // check if the items contains the pivot, otherwise sort sequentially
            if (indexOfPartition > MINIMAL_PARTITION_SIZE) {
                t = indexOfPartition - 1;
                f = indexOfPartition + 1;
            }

            if ((left - right) > MINIMAL_PARTITION_SIZE || (left - right) < -MINIMAL_PARTITION_SIZE) {

                // split the work concurrently
                List<ParallelQuicksort<Car>> subtasks = splitWork(indexOfPartition);
                invokeAll(subtasks);

                // paste the results back together
                for (ParallelQuicksort<Car> i : subtasks) {
                    i.join();
                }

            } else {
                // Partition too small, sort sequentially
                if (left < t) {
                    PquickSortPart(items, left, indexOfPartition, comparator);
                }
                if (f < right) {
                    PquickSortPart(items, indexOfPartition + 1, right, comparator);
                }
            }
        }
    }

    private List<ParallelQuicksort<Car>> splitWork(int pivot) {
        List<ParallelQuicksort<Car>> subtasks = new ArrayList<>();
        subtasks.add(new ParallelQuicksort<>(items, left, pivot, comparator));
        subtasks.add(new ParallelQuicksort<>(items, pivot + 1, right, comparator));
        return subtasks;
    }

    private int partition(Car pivot, Comparator<Car> comparator) {
        int f = left;
        int t = right;

        if (t > f) {
            // create pivot from the middle of the list
            while (f <= t) {
                // swap items until pivot is in the middle of the given section
                while (f < right && comparator.compare(items.get(f), pivot) < 0) {
                    f += 1;
                }
                while (t > left && comparator.compare(items.get(t), pivot) > 0) {
                    t -= 1;
                }
                if (f <= t) {
                    swap(items, f, t);
                    f += 1;
                    t -= 1;
                }
            }
        }
        return t;
    }

    /**
     * Sorts all items between index positions 'from' and 'to' inclusive by quick
     * sort using the provided comparator for deciding relative ordening of two
     * items are sorted 'in place' without use of an auxiliary list or array
     * or other positions in items
     */
    private void PquickSortPart(List<Car> items, int from, int to, Comparator<Car> comparator) {

        // quick sort the sublist of items between index positions 'from' and 'to'
        // inclusive
        int f = from;
        int t = to;
        Car pivot = medianOfThree(items, f, t, comparator);
        pivot = items.get(partition(pivot, comparator));

        if (t > f) {
            // create pivot from the middle of the list
            while (f <= t) {
                // swap items until pivot is in the middle of the given section
                while (f < to && comparator.compare(items.get(f), pivot) < 0) {
                    f += 1;
                }
                while (t > from && comparator.compare(items.get(t), pivot) > 0) {
                    t -= 1;
                }
                if (f <= t) {
                    swap(items, f, t);
                    f += 1;
                    t -= 1;
                }
            }
        }

        if (from < t) {
            PquickSortPart(items, from, t, comparator);
        }
        if (f < to) {
            PquickSortPart(items, f, to, comparator);
        }
    }


    void swap(List<Car> arr, int from, int to) {
        Car f = arr.get(from);
        arr.set(from, arr.get(to));
        arr.set(to, f);
    }

    Car medianOfThree(List<Car> list, int left, int right, Comparator<Car> comparator) {

        int center = (left + right) / 2;
        if (comparator.compare(list.get(center), list.get(left)) < 0) swap(list, left, center);
        if (comparator.compare(list.get(right), list.get(left)) < 0) swap(list, left, right);
        if (comparator.compare(list.get(right), list.get(center)) < 0) swap(list, center, right);

        return list.get(center);
    }
}

