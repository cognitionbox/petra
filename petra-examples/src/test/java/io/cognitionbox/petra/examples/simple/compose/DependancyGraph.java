package io.cognitionbox.petra.examples.simple.compose;


import io.cognitionbox.petra.lang.PGraph;
import static io.cognitionbox.petra.util.Petra.*;

/*
 * Think we don't need the reflective deconstructions at runtime now,
 * which means we only need to use the framework to create thread per parallel run,
 * as apposed to creating threads per deconstructed object.
 */
public class DependancyGraph extends PGraph<X> {
    {
       pre(rw(X.class, a->true));
       step(new AtoC());
       step(new PrintF());
       step(new CtoD());
       step(new PrintE());
       post(rt(X.class, a->true));
    }
}