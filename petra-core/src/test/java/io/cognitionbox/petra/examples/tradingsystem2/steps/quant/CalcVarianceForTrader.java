package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import com.google.common.primitives.Doubles;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.ta4j.core.num.PrecisionNum;

import java.util.stream.Collectors;

public class CalcVarianceForTrader extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(t -> t.getVariance() == null);
        func(t -> {
            double[] array = Doubles.toArray(t.getBenchmarkDiffs().stream().map(q -> q.getMid().doubleValue()).collect(Collectors.toList()));
            t.setVariance(PrecisionNum.valueOf(new Variance().evaluate(array)));
        });
        post(t -> t.getVariance() != null);
    }
}

