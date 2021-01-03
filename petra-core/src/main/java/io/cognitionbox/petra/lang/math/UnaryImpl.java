package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.RW;

import java.math.BigDecimal;

import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rw;

public class UnaryImpl extends NullaryImpl implements Unary {
    private final RO<BigDecimal> a;
    public UnaryImpl(BigDecimal a) {
        super(null);
        this.a = ro(a);
    }

    @Override
    public BigDecimal a() {
        return a.get();
    }
}
