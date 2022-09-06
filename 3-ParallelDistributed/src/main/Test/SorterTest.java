import ParallelSort.Car;
import ParallelSort.DatasetCreator;
import ParallelSort.Quicksort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        dsCars1.listToCars(250);

        manyCars = dsCars1.getCars();
    }

    @Test
    void datasetIsNotEmptyTest() throws URISyntaxException {
        DatasetCreator emptyDataset = new DatasetCreator();
        emptyDataset.listToCars(0);

        assertTrue(emptyDataset.getCars().isEmpty());
        assertFalse(manyCars.isEmpty());
    }

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

    @Test
    void quickSortDuplicates() {
        List<Car> duplicateCars = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 100; j++) {
                duplicateCars.add(manyCars.get(j));
            }
        }
        List<Car> sortedDuplicateCars= new ArrayList<>(duplicateCars);

        duplicateCars.sort(comparator);

        assertTimeoutPreemptively(Duration.ofSeconds(1), () -> {
            sorter.quickSort(sortedDuplicateCars, comparator);
            assertEquals(duplicateCars, sortedDuplicateCars);
        });
    }
}