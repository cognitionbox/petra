package io.cognitionbox.petra.examples.tradingsystem2.steps.quant;

import com.google.common.primitives.Doubles;
import io.cognitionbox.petra.examples.tradingsystem2.objects.traders.Trader;
import io.cognitionbox.petra.lang.PEdge;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.ta4j.core.num.PrecisionNum;

import java.util.stream.Collectors;

public class CalcCovarianceForTrader extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(t -> t.getCovariance() == null);
        func(t -> {
            double[] instrumentArray = Doubles.toArray(t.getQuoteDiffs().stream().map(q -> q.getMid().doubleValue()).collect(Collectors.toList()));
            double[] benchmarkArray = Doubles.toArray(t.getBenchmarkDiffs().stream().map(q -> q.getMid().doubleValue()).collect(Collectors.toList()));
            t.setCovariance(PrecisionNum.valueOf(new Covariance().covariance(instrumentArray, benchmarkArray)));
        });
        post(t -> t.getCovariance() != null);
    }
}


