package io.cognitionbox.petra.examples.tradingsystem.objects;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Decision implements Serializable {

    final static private AtomicInteger idCounter = new AtomicInteger(0);
    final private Tick tick;
    final private LocalTime time;
    final private Trader.Direction dir;
    final private Double limit;
    final private Double stop;
    final private Double exit;
    final private Double qty;
    private Integer id = idCounter.getAndIncrement();

    public Decision(Tick tick, LocalTime time, Trader.Direction dir, Double limit, Double stop, Double exit, Double qty) {
        this.tick = tick;
        this.time = time;
        this.dir = dir;
        this.limit = limit;
        this.stop = stop;
        this.exit = exit;
        this.qty = qty;
    }

    public Double exposure() {
        return Math.abs(limit - stop) * qty;
    }

    public String toString(){
        return this.dir.name();
    }

}