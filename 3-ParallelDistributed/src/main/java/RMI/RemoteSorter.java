package RMI;

import ParallelSort.Car;

import java.net.URISyntaxException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public interface RemoteSorter extends Remote {


    int getLeft() throws RemoteException;

    void setLeft(int left) throws RemoteException;

    void setRight(int right) throws RemoteException;

    int getRight() throws RemoteException;

    void initTask() throws RemoteException;

    void addTask(int from, int to) throws RemoteException;

    void removeTask(Pair<Integer, Integer> task) throws RemoteException;

    Pair<Integer, Integer> getNextTask() throws RemoteException;

    List<Pair<Integer, Integer>> getTaskList() throws RemoteException;

    List<Car> sortList(List<Car> items) throws RemoteException;

    void sortListPart(List<Car> items, int left, int right) throws RemoteException;

    Supplier<Integer> getPartitionTask(List<Car> items, int left, int right) throws RemoteException;

    int partition(Car pivot, List<Car> items, int left, int right, Comparator<Car> comparator) throws RemoteException;

    List<Car> getCars() throws RemoteException;

    void initCars() throws URISyntaxException, RemoteException;

    List<Car> initCarsTask() throws URISyntaxException, RemoteException;
}
