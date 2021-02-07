package io.cognitionbox.petra.examples.tradingsystem2.objects;

public interface DecisionProcessor {
    // change state of decision NEW -> OPEN -> CLOSE
    void processDecision(Quote quote, Decision decision);
}
