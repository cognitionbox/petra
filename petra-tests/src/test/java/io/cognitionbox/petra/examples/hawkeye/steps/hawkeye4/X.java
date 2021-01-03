package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye4;

import io.cognitionbox.petra.lang.math.Nullary;
import io.cognitionbox.petra.lang.math.NullaryImpl;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.*;

public class X {
    public final Nullary i;
    public X(double in) {
        this.i = new NullaryImpl(r(in));
    }
    public boolean isInRangeA(){
        return isInRangeX() || isInRangeY();
    }
    public boolean isInRangeB(){
        return range(exc(1.0),i.result(),inc(2.1));
    }

    public boolean isInRangeC(){
        return range(exc(3.0),i.result(),inc(4.1));
    }

    private boolean isInRangeX(){
        return range(inc(0.1),i.result(),inc(0.25));
    }

    private boolean isInRangeY(){
        return range(inc(0.25),i.result(),inc(1.0));
    }
}
