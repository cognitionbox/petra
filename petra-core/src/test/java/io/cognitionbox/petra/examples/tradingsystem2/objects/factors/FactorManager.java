package io.cognitionbox.petra.examples.tradingsystem2.objects.factors;

import org.javatuples.Pair;

import java.util.Set;

public interface FactorManager {
    Set<String> getTopESGStocksToGoLong();

    Set<String> getBottomESGStocksToGoShort();

    Factor getFactorForInstrumentAndKey(String instrument, String factorKey);

    String getFactor();

    String computeESGToGoLong(String factorKey);

    String computeESGToGoshort(String factorKey);

    Pair<String, String> reshuffle(); // compute which to go long and short
}
