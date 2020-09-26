package io.cognitionbox.petra.examples.driverlesscars;

import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;
import io.cognitionbox.petra.examples.driverlesscars.objects.junction.Junction;
import io.cognitionbox.petra.examples.driverlesscars.permissions.ReadJunction;
import io.cognitionbox.petra.examples.driverlesscars.permissions.WriteJunction;
import io.cognitionbox.petra.lang.annotations.Extract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Extract
public class Simlulation implements ReadJunction, WriteJunction, Serializable {
    Junction junction = new Junction();

    @Override
    public void moveToIntoJunction(Car car) {
        this.junction.moveIntoJunction(car);
    }

    @Override
    public void moveOutOfJunction(Car car) {
        this.junction.moveOutOfJunction(car);
    }

    @Override
    public boolean carInJunction(Car car) {
        return this.junction.carInJunction(car);
    }

    @Override
    public boolean carsInJunction() {
        return this.junction.carsInJunction();
    }

    @Override
    public int getNoOfCarsInJunction() {
        return junction.getNoOfCarsInJunction();
    }

    @Extract  public Signal getSignalA() {
        return signalA;
    }

    @Extract public Signal getSignalB() {
        return signalB;
    }

    public boolean signalAisRED(){
        return getSignalA().getSignal().isRED();
    }

    public boolean signalBisRED(){
        return getSignalB().getSignal().isRED();
    }

    public boolean signalAisGREEN(){
        return getSignalA().getSignal().isGREEN();
    }

    public boolean signalBisGREEN(){
        return getSignalB().getSignal().isGREEN();
    }

    @Override
    public String toString() {
        return "Simlulation{" +
                "junction=" + junction +
                ", signalA=" + signalA +
                ", signalB=" + signalB +
                '}';
    }

    private Signal signalA = new Signal(SignalState.GREEN);
    private Signal signalB = new Signal(SignalState.RED);

    @Extract public List<Car> getCars() {
        return this.cars;
    }

    private List<Car> cars = new ArrayList<>();

    void loadCars(int noOfCars){
        for (int i=0;i<noOfCars;i++){
           if (i%2==0){
               cars.add(new Car(signalB));
           } else {
               cars.add(new Car(signalA));
           }
        }
    }
}
