package io.cognitionbox.petra.examples.simple.parallelcalcs;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.par;

public class ParCalculator extends PGraph<Calculations> {
    {
        type(Calculations.class);
        kase(
                calculations -> calculations.checkOk(),
                calculations ->calculations.addPositiveNumbers1.result.get()==11 &&
                        calculations.addPositiveNumbers2.result.get()==11);

        steps(par(),x->x.list,Print.class);
        step(par(),x->x.addPositiveNumbers1,AddStep.class);
        step(par(),x->x.addPositiveNumbers2,AddStep.class);
        esak();
    }
}
