package io.cognitionbox.petra.examples.simple.compose;


import io.cognitionbox.petra.lang.PGraph;

/*
 * Think we don't need the reflective deconstructions at runtime now,
 * which means we only need to use the framework to create thread per parallel run,
 * as apposed to creating threads per deconstructed object.
 */
public class DependancyGraph extends PGraph<X> {
    {
       type(X.class);
       pc(a->true);
       step(new AtoC());
       step(new PrintF());
       step(new CtoD());
       step(new PrintE());
       qc(a->true);
    }
}