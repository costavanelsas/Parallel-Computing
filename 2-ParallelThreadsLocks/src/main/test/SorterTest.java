import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.*;

class SorterTest {

    protected Quicksort<Car> sorter = new Quicksort<>();
    protected List<Car> manyCars;
    protected Comparator<Car> comparator = Comparator.comparing(Car::getModelYear)
            .thenComparing(Car::getName)
            .thenComparing(Car::getLocation);


    @BeforeEach
    void setup() throws URISyntaxException {
        DatasetCreator dsCars1 = new DatasetCreator();
        dsCars1.listToCars(SpeedCheckerMain.DATASET_SIZE);

        manyCars = dsCars1.getCars();
    }

    /**
     * Test to check if the dataset is not empty
     */
    @Test
    void datasetIsNotEmptyTest() throws URISyntaxException {
        DatasetCreator emptyDataset = new DatasetCreator();
        emptyDataset.listToCars(0);

        assertTrue(emptyDataset.getCars().isEmpty());
        assertFalse(manyCars.isEmpty());
    }

    /**
     * Test to check if a correct name value is added to the dataset
     */
    @Test
    void testAdd() {
        List<Car> addCarsTest;
        DatasetCreator dsCars1 = new DatasetCreator();
        addCarsTest = dsCars1.getCars();

        addCarsTest.add(new Car("Porsche 911"));

        System.out.println(addCarsTest.get(0).getName());
        assertTrue(addCarsTest.get(0).getName().equals("Porsche 911"));
    }

    /**
     * Test to check if the dataset works on empty list
     */
    @Test
    void emptyListTest() {
        DatasetCreator ds = new DatasetCreator();

        List<Car> cars = ds.getCars();
        List<Car> cars2 = ds.getCars();

        cars.sort(Car.compareCars());

        sorter.quickSort(cars2, Car.compareCars());
        assertTrue(cars.isEmpty());
        assertTrue(cars2.isEmpty());
    }

    /**
     * Test to check if our quicksort results the same as the java collection sort
     *
     * @throws URISyntaxException
     */
    @Test
    void quickSortAndCollectionSortResultInSameOrder() throws URISyntaxException {

        DatasetCreator dsCars1 = new DatasetCreator();
        dsCars1.listToCars(1000);

        List<Car> cars = dsCars1.getCars();

        List<Car> fewSortedCars = new ArrayList<>(cars);

        Collections.shuffle(fewSortedCars);
        Collections.shuffle(cars);

        sorter.quickSort(fewSortedCars, Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation));
        cars.sort(Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation));
        assertEquals(fewSortedCars, cars);

        sorter.quickSort(fewSortedCars, comparator);
        cars.sort(comparator);
        assertEquals(fewSortedCars, cars);
        assertEquals(fewSortedCars.size(), cars.size());

        //visually check if the cars are sorted
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + cars.get(i).getModelYear()
                    + " " + cars.get(i).getName()
                    + " " + cars.get(i).getLocation());
        }

        System.out.println("\n*****************\n");

        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + fewSortedCars.get(i).getModelYear()
                    + " " + fewSortedCars.get(i).getName()
                    + " " + fewSortedCars.get(i).getLocation());
        }
    }

    /**
     * Test to check if the quicksort handles duplicates
     */
    @Test
    void quickSortDuplicates() {
        List<Car> duplicateCars = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                duplicateCars.add(manyCars.get(j));
            }
        }
        List<Car> sortedDuplicateCars = new ArrayList<>(duplicateCars);

        duplicateCars.sort(comparator);

        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            sorter.quickSort(sortedDuplicateCars, comparator);
            assertEquals(duplicateCars, sortedDuplicateCars);
        });
    }

    /**
     * Test if the parallel quicksort works the same as our other quicksort algorithm
     *
     * @throws URISyntaxException
     */
    @Test
    void parallelQuicksort() throws URISyntaxException {

        ForkJoinPool pool = new ForkJoinPool(SpeedCheckerMain.MAX_THREADS);
        DatasetCreator datasetCreator = new DatasetCreator();
        datasetCreator.listToCars(75000);
        List<Car> cars = datasetCreator.getCars();

        Quicksort<Car> sorter = new Quicksort<>();

        System.out.println("Threads: " + SpeedCheckerMain.MAX_THREADS);

        sorter.quickSort(cars, comparator);

        List<Car> cars2 = new ArrayList<>(cars);
        ParallelQuicksort<Car> pSorter = new ParallelQuicksort<>(cars2, comparator);

        pool.invoke(pSorter);
        pool.shutdown();

        assertEquals(cars, cars2);
    }
}