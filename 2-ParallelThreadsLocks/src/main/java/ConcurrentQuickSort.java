import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ConcurrentQuickSort<T extends Comparable<T>> extends RecursiveAction {
    private List<T> list;
    private int size;
    private int begin;
    private int end;


    public ConcurrentQuickSort(List<T> list, int begin, int end) {
        this.list = list;
        this.size = this.list.size();
        this.begin = begin;
        this.end = end;

    }


    private int partition(int begin, int end) {

        T x = list.get(begin);
        int i = begin;
        int j = end;

        while (true) {
            while (list.get(j).compareTo(x) > 0) {
                j--;
            }

            while (list.get(i).compareTo(x) < 0) {
                i++;
            }

            if (i < j) {
                swap(i, j);
                i++;
                j--;

            } else
                return j;
        }
    }

    public void doSort(int begin, int end) {
        int q;
        if (begin < end) {
            q = partition(begin, end);

            doSort(begin, q);
            doSort(q + 1, end);
        }
    }

    private void swap(int first, int second) {
        T temp = list.get(first);
        list.set(first, list.get(second));
        list.set(second, temp);
    }

    @Override
    protected void compute() {

        int q;
        if (begin < end) {

            q = partition(begin, end);
            List<ConcurrentQuickSort<T>> subtasks = splitWork(q);
            for (ConcurrentQuickSort<T> t : subtasks) {
                t.fork();
            }


        }
    }

    private List<ConcurrentQuickSort<T>> splitWork(int q) {
        List<ConcurrentQuickSort<T>> subtasks = new ArrayList<>();
        subtasks.add(new ConcurrentQuickSort<>(this.list, begin, q));
        subtasks.add(new ConcurrentQuickSort<>(this.list, q + 1, end));
        return subtasks;
    }

    public boolean isCorrect(List<T> other) {
        return list.equals(other);
    }
}