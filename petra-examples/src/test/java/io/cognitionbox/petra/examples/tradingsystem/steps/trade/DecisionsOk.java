package io.cognitionbox.petra.examples.tradingsystem.steps.trade;

public interface DecisionsOk {
    default boolean decisionsOk(){
        return !isEmpty();
    }

    boolean isEmpty();
}
