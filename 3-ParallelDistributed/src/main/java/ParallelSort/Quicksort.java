package ParallelSort;

import junit.runner.Sorter;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Quicksort<E> extends Sorter implements Serializable {

    /**
     * Sorts all items by quick sort using the provided comparator for deciding
     * relative ordening of two items Items are sorted 'in place' without use of an
     * auxiliary list or array
     *
     * @return the items sorted in place
     */
    public List<Car> quickSort(List<Car> items, Comparator<Car> comparator) {
        // sort the complete list of items from position 0 till size-1, including
        // position size
        this.quickSortPart(items, 0, items.size() - 1, comparator);
        return items;
    }

    /**
     * Sorts all items between index positions 'from' and 'to' inclusive by quick
     * sort using the provided comparator for deciding relative ordening of two
     * items Items are sorted 'in place' without use of an auxiliary list or array
     * or other positions in items
     * <p>
     * // create pivot from the middle of the list
     * E pivot = items.get((F + T) / 2);
     */
    private void quickSortPart(List<Car> items, int from, int to, Comparator<Car> comparator) {

        // quick sort the sublist of items between index positions 'from' and 'to'
        // inclusive
        int f = from;
        int t = to;

        if (t > f) {
            // create pivot from the middle of the list
            Car pivot = medianOfThree(items, f, t, comparator);
            while (f <= t) {
                //
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
            quickSortPart(items, from, t, comparator);
        }
        if (f < to) {
            quickSortPart(items, f, to, comparator);
        }

    }

    void swap(List<Car> arr, int from, int to) {
        Car f = arr.get(from);
        arr.set(from, arr.get(to));
        arr.set(to, f);
    }

    Car medianOfThree(List<Car> list, int left, int right, Comparator<Car> comparator) {

        int center = (left + right) / 2;
        if (comparator.compare(list.get(center), list.get(left)) < 0)
            swap(list, left, center);
        if (comparator.compare(list.get(right), list.get(left)) < 0)
            swap(list, left, right);
        if (comparator.compare(list.get(right), list.get(center)) < 0)
            swap(list, center, right);

        return list.get(center);
    }

    public void quickSortPartPublic(List<Car> items, int from, int to, Comparator<Car> comparator) {

        // quick sort the sublist of items between index positions 'from' and 'to'
        // inclusive
        int f = from;
        int t = to;

        if (t > f) {
            // create pivot from the middle of the list
            Car pivot = medianOfThree(items, f, t, comparator);
            while (f <= t) {
                //
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
            quickSortPart(items, from, t, comparator);
        }
        if (f < to) {
            quickSortPart(items, f, to, comparator);
        }

    }

}
