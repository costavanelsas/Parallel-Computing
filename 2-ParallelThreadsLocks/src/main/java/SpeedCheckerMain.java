import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class SpeedCheckerMain {

    public static final int MAX_THREADS = 8;
    public static final int DATASET_SIZE = 60000;

    public static void main(String[] args) throws URISyntaxException {

        ForkJoinPool pool = new ForkJoinPool(MAX_THREADS);
        DatasetCreator datasetCreator = new DatasetCreator();
        datasetCreator.listToCars(DATASET_SIZE);
        List<Car> cars = datasetCreator.getCars();

        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);

        Quicksort<Car> sorter = new Quicksort<>();

        System.out.println("Threads: " + MAX_THREADS);
        System.out.println("Total dataset size: " + cars.size() + " cars");
        long sequentialStarted = System.currentTimeMillis();
        sorter.quickSort(cars, comparator);
        long sequentialFinished = System.currentTimeMillis();
        System.out.printf("Sequential sorting took: %,d ms %n", sequentialFinished - sequentialStarted);

//        // Loop to visually check if the sequential sort works
//        for (int i = 0; i < 10; i++) {
//            System.out.println(i + " " + cars.get(i + 1000).getModelYear() + " " + cars.get(i + 1000).getName() + " " + cars.get(i + 1000).getLocation());
//        }

        List<Car> cars2 = new ArrayList<>(cars);
        ParallelQuicksort<Car> pSorter = new ParallelQuicksort<>(cars2, comparator);

        long parallelStarted = System.currentTimeMillis();
        pool.invoke(pSorter);
        long parallelFinished = System.currentTimeMillis();
        System.out.printf("%nParallel sorting took: %,d ms %n", parallelFinished - parallelStarted);

        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
            System.out.printf("Main: Pool size: %d\n", pool.getPoolSize());
            System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
            System.out.printf("******************************************\n");
        } while (!pSorter.isDone());

        pool.shutdown();

//        // Loop to visually check if the par sort works
//        for (int i = 0; i < 10; i++) {
//            System.out.println(i + " " + cars2.get(i + 1000).getModelYear() + " " + cars2.get(i + 1000).getName() + " " + cars2.get(i + 1000).getLocation());
//        }
    }
}
