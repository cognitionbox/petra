package io.cognitionbox.petra.examples.driverlesscars.steps;

import io.cognitionbox.petra.examples.driverlesscars.Simlulation;
import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.thereExists;

public class MoveCarOutOfJunction extends PEdge<Simlulation> {
    {
        type(Simlulation.class);
        pc(x->x.carsInJunction() && thereExists(Car.class,x.getCars(), c->x.carInJunction(c)));
        func(x->{
            x.getCars().stream().filter(c->x.carInJunction(c)).forEach(c->x.moveOutOfJunction(c));
            System.out.println("getNoOfCarsInJunction = "+x.getNoOfCarsInJunction());
            return x;
        });
        qc(x->thereExists(Car.class,x.getCars(), c->!x.carInJunction(c)));
    }
}
