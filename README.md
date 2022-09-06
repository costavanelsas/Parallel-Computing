# Team 18

-Xint as JVM options setting. You can set this by adding JVM-options to your run configuration. The performance benefits offered by the Java HotSpot Client VM's adaptive compiler will
not be present in this mode, this is perfect for benchmarking our sorting tests. Make sure this option is added to generate the same results as our report.

The maximum amount of data we can provide now is 75898 unique items. For testing purposes 8 threads should be the maximum,
unless you have some special hardware.

## Assignment 1

Our solution of the quicksort algorithm is in our PDF document.

The implementation for assignment 1 can be found in the 1-Sequential folder.
At the top you can change the dataset size. You can do this by changing the **DATASET_SIZE** variable declared at the beginning of our code.

When using IntelliJ, use the following steps to run our program:

> * Navigate to the **src/main/java** package.
> * Open the **SpeedCheckerMain.java** class.
> * Start the **SpeedCheckerMain.java** class by clicking on the small green arrow in the sidebar.

The results are printed in the console.

## Assignment 2


The implementation for assignment 2 can be found in the 2-ParallelThreadLocks folder.

This folder contains the used dataset and our implementation of a parallelized quicksort.

The SpeedCheckerMain class is made to orchestrate the data creation and set up the sorts. At the top you can change the
amount of threads and dataset size. You can do this by changing the **MAX_THREADS** variable the **DATASET_SIZE** variable  declared at the beginning of our code.

When using IntelliJ, use the following steps to run our program:

> * Navigate to the **src/main/java** package.
> * Open the **SpeedCheckerMain.java** class.
> * Start the **SpeedCheckerMain.java** class by clicking on the small green arrow in the sidebar.

The results are printed in the console.
To visually check if the data is sorted, you can uncomment the for-loop on rule number 34-36 (sequential) and rule number 57-59 (TL)

## Assignment 3

The implementation for assignment 3 can be found in the 3-ParallelDistributed src/main/java/RMI folder.

The RMI/MainRMI class is made to orchestrate the data creation and set up the sorts and the master > workers solution. At the top you can change the
amount of threads and dataset size. You can do this by changing the **WORKERS_AMOUNT** variable the **DATASET_SIZE** variable  declared at the beginning of our code.

When using IntelliJ, use the following steps to run our program:

> * Navigate to the **src/main/java/RMI** package.
> * Open the **MainRMI.java** class.
> * Start the **MainRMI.java** class by clicking on the small green arrow in the sidebar.

The results are printed in the console.

## Tests

Each assignment has a test folder with a test class.

When using IntelliJ, use the following steps to run our tests:

> * Navigate to the **src/main/test** package.
> * Open the **SorterTest.java** class.
> * Start the **SorterTest.java** class by clicking on the small green arrow in the sidebar.

The results of the tests will be shown.
