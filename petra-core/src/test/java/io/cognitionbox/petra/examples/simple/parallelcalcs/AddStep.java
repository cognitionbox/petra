package io.cognitionbox.petra.examples.simple.parallelcalcs;

import io.cognitionbox.petra.lang.PEdge;

import java.time.LocalTime;

public class AddStep extends PEdge<AddPositiveNumbers> {
    {
        type(AddPositiveNumbers.class);
        kase(x->true, x->true);
        func(x->{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            x.result.set(x.a + x.b);
           System.out.println(x.result+" "+ LocalTime.now()+" "+Thread.currentThread());
        });
    }
}
