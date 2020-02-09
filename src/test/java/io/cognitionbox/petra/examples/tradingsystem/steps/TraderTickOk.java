package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.Tick;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;

public interface TraderTickOk {
    default public boolean traderTickOk(){
        return tick()!=null && trader()!=null;
    }

    public Trader trader();

    public Tick tick();
}
