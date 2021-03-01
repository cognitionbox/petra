package io.cognitionbox.petra.examples.driverlesscars.permissions;

import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;

public interface WriteJunction {
    void moveToIntoJunction(Car car);
    void moveOutOfJunction(Car car);
}
