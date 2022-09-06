package ParallelSort;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatasetCreator implements Serializable {

    public List<Car> getCars() {
        return cars;
    }

    private final List<Car> cars = new ArrayList<>();

    public void listToCars(int size) throws URISyntaxException {

        List<List<String>> lines = null;
        lines = readCSV();
        if (size > lines.size()) {
            // size guard
            size = lines.size() - 2;
        }

        for (int line = 1; line < size + 1; line++) {
            // skip first line
            Car car = Car.lineToCar(lines.get(line));
            cars.add(car);

        }
//        for (List<String> line : lines.subList(1, lines.size())) {
//            // skip first line
//            ParallelSort.Car car = ParallelSort.Car.lineToCar(line);
//            cars.add(car);
//
//        }
    }


    public List<List<String>> readCSV() throws URISyntaxException {
        List<List<String>> csvLines = new ArrayList<>();

        File file = new File(getClass().getResource("../usedCarsFinal.csv").toURI());

        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                csvLines.add(Arrays.asList(values));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        return csvLines;
    }
}
