package ParallelSort;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class SpeedCheckerMain {

    public static final int MAX_THREADS = 8;
    public static final int DATASET_SIZE = 50000;

    public static void main(String[] args) throws URISyntaxException {


        ForkJoinPool pool = new ForkJoinPool(MAX_THREADS);
        DatasetCreator datasetCreator = new DatasetCreator();
        datasetCreator.listToCars(DATASET_SIZE);
        List<Car> cars = datasetCreator.getCars();
        List<Car> cars2 = new ArrayList<>(cars);

        Comparator<Car> comparator = Comparator.comparing(Car::getName).thenComparing(Car::getLocation).thenComparing(Car::getMileage);
        ParallelQuicksort<Car> pSorter = new ParallelQuicksort<>(cars2, comparator);
        Quicksort<Car> sorter = new Quicksort<>();

        System.out.println("Threads: " + MAX_THREADS);
        System.out.println("Total size: " + cars.size());
        long sequentialStarted = System.currentTimeMillis();
        sorter.quickSort(cars, comparator);
        long sequentialFinished = System.currentTimeMillis();
        System.out.printf("Sequential sorting took: %,d ms %n", sequentialFinished - sequentialStarted);
        System.out.println();

//        for (int i = 0; i < 10; i++) {
//            System.out.println(i + " " + cars.get(i + 10).getMileage() + " " + cars.get(i + 10000).getName() + " " + cars.get(i + 10000).getLocation());
//        }

        System.out.println("Total size: " + cars2.size());
        long parallelStarted = System.currentTimeMillis();
        pool.invoke(pSorter);
        long parallelFinished = System.currentTimeMillis();
        System.out.printf("Parallel sorting took: %,d ms %n", parallelFinished - parallelStarted);
        pool.shutdown();

//        for (int i = 0; i < 10; i++) {
//            System.out.println(i + " " + cars2.get(i + 10).getMileage() + " " + cars2.get(i + 10000).getName() + " " + cars2.get(i + 10000).getLocation());
//        }
    }
}
