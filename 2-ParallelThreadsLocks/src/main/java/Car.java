import java.util.Comparator;
import java.util.List;

public class Car {
    public static final int NAME_FIELD = 0;
    public static final int LOCATION_FIELD = 1;
    public static final int MODEL_YEAR_FIELD = 2;
    public static final int MILEAGE_FIELD = 3;

    private String name;
    private String location;
    private int modelYear;
    private String mileage;

    public Car(String name) {
        this.name = name;
    }

    public static Car lineToCar(List<String> line) {
        String[] fields = line.toArray(new String[0]);

        final Car car = new Car(fields[NAME_FIELD]);

        car.setName(fields[NAME_FIELD]);
        car.setLocation(fields[LOCATION_FIELD]);
        car.setModelYear(Integer.parseInt(fields[MODEL_YEAR_FIELD]));
        car.setMileage(fields[MILEAGE_FIELD]);

        return car;
    }

    public static Comparator<Car> compareCars(){
        return Comparator.comparing(Car::getModelYear).thenComparing(Car::getName).thenComparing(Car::getLocation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getModelYear() {
        return modelYear;
    }

    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return getModelYear() == car.getModelYear()
                && name.equals(car.name)
                && getLocation().equals(car.getLocation());
    }
}
