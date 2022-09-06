import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

public class SpeedCheckerMain {

    public static final int DATASET_SIZE = 60000;

    public static void main(String[] args) throws URISyntaxException {

        DatasetCreator datasetCreator = new DatasetCreator();
        Quicksort<Car> sorter = new Quicksort<>();
        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);

        datasetCreator.listToCars(DATASET_SIZE);
        List<Car> cars = datasetCreator.getCars();

        System.out.println("Total size: " + cars.size());

        System.out.println("\nFirst 10 cars before sorting:");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + cars.get(i).getModelYear() + " " + cars.get(i).getName() + " " + cars.get(i).getLocation());
        }

        System.out.println("\n **** Start sorting ****");
        long sequentialStarted = System.currentTimeMillis();
        sorter.quickSort(cars, comparator);
        long sequentialFinished = System.currentTimeMillis();
        System.out.printf("%n Sequential sorting took: %,d ms %n", sequentialFinished - sequentialStarted);
        System.out.println("\n **** Sorting finished ****");

        System.out.println("\nResult after sorting:");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + " " + cars.get(i).getModelYear() + " " + cars.get(i).getName() + " " + cars.get(i).getLocation());
        }

    }
}
