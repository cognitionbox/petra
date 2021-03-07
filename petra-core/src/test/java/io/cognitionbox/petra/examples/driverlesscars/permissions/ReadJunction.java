package io.cognitionbox.petra.examples.driverlesscars.permissions;

import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;

public interface ReadJunction {
    boolean carsInJunction();

    boolean carInJunction(Car car);

    int getNoOfCarsInJunction();
}
