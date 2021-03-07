package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata;

import org.ta4j.core.num.Num;

public interface Quote {
    Num getBid();

    Num getAsk();

    Num getMid();

    Num getSpread();
}
