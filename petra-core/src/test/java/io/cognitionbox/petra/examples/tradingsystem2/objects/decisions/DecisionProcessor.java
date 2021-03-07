package io.cognitionbox.petra.examples.tradingsystem2.objects.decisions;

import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;

public interface DecisionProcessor {
    // change state of decision NEW -> OPEN -> CLOSE
    void processDecision(Quote quote, Decision decision);
}
