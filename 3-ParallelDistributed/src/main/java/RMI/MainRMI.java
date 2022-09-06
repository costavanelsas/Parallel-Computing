package RMI;

import ParallelSort.Car;
import ParallelSort.DatasetCreator;
import ParallelSort.Quicksort;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.List;

public class MainRMI {
    // Orchestrate factors
    public static final int WORKERS_AMOUNT = 2;
    public static final int DATASET_SIZE = 25000;

    public static final int SERVER_PORT = 1299;
    public static final String SERVICE_NAME = "/RMI_sorter";
    public static final String REGISTER_HOST = "localhost";

    private static final String JAVA_BIN = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    private static final String CLASSPATH = System.getProperty("java.class.path");

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {

        DatasetCreator datasetCreator = new DatasetCreator();
        datasetCreator.listToCars(DATASET_SIZE);
        List<Car> cars = datasetCreator.getCars();

        //sequential part
        Quicksort<Car> seq = new Quicksort<>();
        Comparator<Car> comparator = Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);
        System.out.println("Sequential sorting started...");
        long sequentialStarted = System.currentTimeMillis();
        seq.quickSort(cars, comparator);
        long sequentialFinished = System.currentTimeMillis();
        System.out.printf("Sequential sorting took: %,d ms %n", sequentialFinished - sequentialStarted);
        System.out.println();

        // register master
        Master sorter = new Master();
        registerMaster(sorter);

        // launch workers
        Process[] workers = launchWorkersAtLocalHost();

        // wait for workers to sort
        shutdownWorkers(workers);

        // shut down program
        UnicastRemoteObject.unexportObject(sorter, true);
    }

    private static void registerMaster(RemoteSorter sorter) throws RemoteException {
        Registry registry;

        // create a new registry at the local host
        registry = LocateRegistry.createRegistry(SERVER_PORT);

        // register the master service object instance
        registry.rebind("//" + MainRMI.SERVICE_NAME, sorter);
    }

    private static Process buildProcess(String objective, int workerId) throws IOException {
        // restart the current main with child worker command line arguments
        ProcessBuilder child = new ProcessBuilder(
                JAVA_BIN, "-classpath", CLASSPATH, Worker.class.getCanonicalName(),
                "--registerHost", REGISTER_HOST,
                "--masterServiceName", MainRMI.SERVICE_NAME,
                "--workerId", workerId + "",
                "--objective", objective,
                "--workersAmount", WORKERS_AMOUNT + ""
        );

        // start process
        return child.inheritIO().start();
    }


    private static Process[] launchWorkersAtLocalHost() throws IOException, InterruptedException {
        Process[] workers = new Process[MainRMI.WORKERS_AMOUNT];

        // split list using worker
        Process initWorker = buildProcess("init", 1);
        initWorker.waitFor();

        // prepare list dividers for sorting (pivots)
        Process partitionWorker = buildProcess("partition", 1);
        partitionWorker.waitFor();

        // launch the worker processes
        for (int childId = MainRMI.WORKERS_AMOUNT - 1; childId >= 0; childId--) {

            workers[childId] = buildProcess("sort", childId);
        }

        System.out.println("Dataset size: " + DATASET_SIZE);
        System.out.printf("%d worker processes have been launched for sorting\n", MainRMI.WORKERS_AMOUNT);
        return workers;
    }

    private static void shutdownWorkers(Process[] workers) throws InterruptedException {

        System.out.println("Waiting for workers to complete... ");
        long rmiStarted = System.currentTimeMillis();

        for (Process worker : workers) {
            worker.waitFor();
        }

        long rmiFinished = System.currentTimeMillis();
        System.out.println("Workers completed");
        System.out.printf("RMI sorting took: %,d ms %n", rmiFinished - rmiStarted);
    }

}
