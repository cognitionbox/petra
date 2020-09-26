package io.cognitionbox.petra.examples.driverlesscars.objects.junction;

import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;
import io.cognitionbox.petra.examples.driverlesscars.permissions.ReadJunction;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Junction implements ReadJunction, Serializable {
    private Set<Car> carsCrossingJunction = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public boolean carsInJunction() {
        return !this.carsCrossingJunction.isEmpty();
    }

    @Override
    public int getNoOfCarsInJunction() {return carsCrossingJunction.size();}

    public boolean carInJunction(Car car) {
        return this.carsCrossingJunction.contains(car);
    }

    public void moveIntoJunction(Car car) {carsCrossingJunction.add(car);
    }

    public void moveOutOfJunction(Car car) {carsCrossingJunction.remove(car);
    }

    @Override
    public String toString() {
        return "Junction{" +
                "carsCrossingJunction=" + carsCrossingJunction +
                '}';
    }
}
