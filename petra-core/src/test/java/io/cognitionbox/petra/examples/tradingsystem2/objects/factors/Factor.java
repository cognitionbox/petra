package io.cognitionbox.petra.examples.tradingsystem2.objects.factors;

import org.ta4j.core.num.Num;

import java.util.List;

public interface Factor {
    List<Num> getData();

    Num getTwoWeekMovingAvg();
}
