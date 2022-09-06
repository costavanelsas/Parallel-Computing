package RMI;

import ParallelSort.Car;
import ParallelSort.DatasetCreator;
import ParallelSort.Quicksort;

import java.io.Serial;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static RMI.MainRMI.DATASET_SIZE;

public class Master extends UnicastRemoteObject implements RemoteSorter, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public List<Car> masterCars;
    public List<Pair<Integer, Integer>> taskBag = new ArrayList<>();
    public int left;
    public int right;

    protected Master() throws RemoteException {
        super();
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public void setLeft(int left) {
        this.left = left;
    }

    @Override
    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public void initTask() {
        taskBag = new ArrayList<>();
    }

    @Override
    public void addTask(int from, int to) {
        taskBag.add(new Pair<>(from, to));
    }

    @Override
    public void removeTask(Pair<Integer, Integer> task) {
        taskBag.remove(task);
    }

    @Override
    public Pair<Integer, Integer> getNextTask() {
        Pair<Integer, Integer> task = taskBag.get(0);
        taskBag.remove(0);
        return task;
    }

    @Override
    public List<Pair<Integer, Integer>> getTaskList() {
        return taskBag;
    }

    @Override
    public List<Car> sortList(List<Car> items) {
        Quicksort<Car> sort = new Quicksort<>();
        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);

        sort.quickSort(items, comparator);

        return items;
    }

    @Override
    public void sortListPart(List<Car> items, int left, int right) {
        Quicksort<Car> sort = new Quicksort<>();
        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);

        sort.quickSortPartPublic(items, left, right, comparator);

    }

    @Override
    synchronized public Supplier<Integer> getPartitionTask(List<Car> items, int left, int right) {
        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);

        Car pivot = medianOfThree(items, left, right, comparator);

        // deliver task
        return (Supplier<Integer> & Serializable)
                () -> partition(pivot, items, left, right, comparator);

    }

    @Override
    public int partition(Car pivot, List<Car> items, int left, int right, Comparator<Car> comparator) {
        return 0;
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

    @Override
    public List<Car> getCars() {
        return masterCars;
    }

    @Override
    public void initCars() throws URISyntaxException {
        DatasetCreator datasetCreator = new DatasetCreator();
        datasetCreator.listToCars(DATASET_SIZE);

        masterCars = datasetCreator.getCars();
    }

    @Override
    // task to be delivered
    public List<Car> initCarsTask() {
        DatasetCreator datasetCreator = new DatasetCreator();
        try {
            datasetCreator.listToCars(DATASET_SIZE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return datasetCreator.getCars();
    }

}
