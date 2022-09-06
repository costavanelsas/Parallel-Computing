package RMI;

import ParallelSort.Car;
import ParallelSort.Quicksort;

import java.net.URISyntaxException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Comparator;
import java.util.List;

public class Worker {

    public static void main(String[] args) throws NotBoundException, RemoteException, URISyntaxException, InterruptedException {
        String registerHost = "";
        String serviceName = "";
        String objective = "";
        int workersAmount = 0;
        int workerId = 0;

        // assign given arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--registerHost" -> {
                    i++;
                    registerHost = args[i];
                }
                case "--masterServiceName" -> {
                    i++;
                    serviceName = args[i];
                }
                case "--workerId" -> {
                    i++;
                    workerId = Integer.parseInt(args[i]);
                }
                case "--objective" -> {
                    i++;
                    objective = args[i];
                }
                case "--workersAmount" -> {
                    i++;
                    workersAmount = Integer.parseInt(args[i]);
                }
            }
        }

        // retrieve master
        Registry registry = LocateRegistry.getRegistry(registerHost, MainRMI.SERVER_PORT);
        RemoteSorter remoteSorterMaster =
                (RemoteSorter) registry.lookup("//" + serviceName);

        System.out.println("worker objective: " + objective);

        // run code according to objective
        if (objective.equals("init")) {
            remoteSorterMaster.initCars();
            remoteSorterMaster.setRight(remoteSorterMaster.getCars().size() - 1);
            remoteSorterMaster.setLeft(0);
            System.out.println("Dataset created\n");

        } else if (objective.equals("partition")) {
            pre(remoteSorterMaster, workersAmount);
            System.out.println("List pre-partitioned\n");
        } else {
            sortPart(remoteSorterMaster, workerId);
            System.out.println("Worker-" + workerId + " has completed sorting");
        }
    }

    /**
     * Hier "proberen" we de auto's om de pivot(s) heen te krijgen op volgorde
     * zodat de lijst al klaarstaat om gesorteert te worden
     */
    public static void pre(RemoteSorter remoteSorterMaster, int workersAmount) throws RemoteException {
        int right = remoteSorterMaster.getRight();
        int left = remoteSorterMaster.getLeft();

        // partition until all workers have a task
        // create tasks for workers

        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);
        Car pivot = medianOfThree(remoteSorterMaster.getCars(), left, right, comparator);
        int indexOfPartition = partition(remoteSorterMaster, pivot, comparator);
        partition(remoteSorterMaster, indexOfPartition, pivot, remoteSorterMaster.getCars(), left, right, comparator);

    }

    /**
     * Hier kijken we hoeveel tasks er zijn en sorteren we die aan de hand van het workerId
     */
    public static void sortPart(RemoteSorter remoteSorterMaster, int workerId) throws RemoteException {

        List<Pair<Integer, Integer>> taskList = remoteSorterMaster.getTaskList();
        if (!taskList.isEmpty()) {
            // sort it's part
            remoteSorterMaster.sortListPart(remoteSorterMaster.getCars(), taskList.get(workerId).getL(), taskList.get(workerId).getR());
        } else {
            System.out.println("no more tasks");
        }
    }

    /**
     * Hier zetten we de auto's om de pivot en voegen we de uiteindes van de sublists toe aan de tasklist
     * door de sublists op te slaan weten de workers straks welk deel ze moeten sorteren
     */
    public static void partition(RemoteSorter remoteSorterMaster, int index, Car pivot, List<Car> items, int left, int right, Comparator<Car> comparator) throws RemoteException {
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
        // add divider pair to tasks

        remoteSorterMaster.addTask(left, index);
        remoteSorterMaster.addTask(index - 1, right);
    }

    private static int partition(RemoteSorter remoteSorterMaster, Car pivot, Comparator<Car> comparator) throws RemoteException {
        int f = remoteSorterMaster.getLeft();
        int t = remoteSorterMaster.getRight();
        List<Car> items = remoteSorterMaster.getCars();

        if (t > f) {
            // create pivot from the middle of the list
            while (f <= t) {
                // swap items until pivot is in the middle of the given section
                while (f < remoteSorterMaster.getRight() && comparator.compare(items.get(f), pivot) < 0) {
                    f += 1;
                }
                while (t > remoteSorterMaster.getLeft() && comparator.compare(items.get(t), pivot) > 0) {
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

    static void swap(List<Car> arr, int from, int to) {
        Car f = arr.get(from);
        arr.set(from, arr.get(to));
        arr.set(to, f);
    }

    static Car medianOfThree(List<Car> list, int left, int right, Comparator<Car> comparator) {

        int center = (left + right) / 2;
        if (comparator.compare(list.get(center), list.get(left)) < 0) swap(list, left, center);
        if (comparator.compare(list.get(right), list.get(left)) < 0) swap(list, left, right);
        if (comparator.compare(list.get(right), list.get(center)) < 0) swap(list, center, right);

        return list.get(center);
    }
}
