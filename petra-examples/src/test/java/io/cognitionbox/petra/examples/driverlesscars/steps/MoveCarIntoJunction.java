package io.cognitionbox.petra.examples.driverlesscars.steps;

import io.cognitionbox.petra.examples.driverlesscars.Simlulation;
import io.cognitionbox.petra.examples.driverlesscars.objects.car.Car;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.thereExists;

public class MoveCarIntoJunction extends PEdge<Simlulation> {
    {
        type(Simlulation.class);
        pre(x->!x.carsInJunction() &&
                thereExists(Car.class,x.getCars(),c->c.getSignal().isGREEN() &&
                !x.carInJunction(c)));
        func(x->{
            x.getCars().stream()
                    .filter(c->c.getSignal().isGREEN() && !x.carInJunction(c))
                    .limit(1)
                    .forEach(c->x.moveToIntoJunction(c));
            System.out.println("getNoOfCarsInJunction = "+x.getNoOfCarsInJunction());
            return x;
        });
        post(x->x.getCars().stream().filter(c->x.carInJunction(c)).count()==1);
    }
}
