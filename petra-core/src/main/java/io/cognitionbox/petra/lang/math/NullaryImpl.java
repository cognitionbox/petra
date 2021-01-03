package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.RW;

import java.math.BigDecimal;

import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rw;

public class NullaryImpl implements Nullary {
    private final RW<BigDecimal> res;
    public NullaryImpl(BigDecimal res) {
        this.res = rw(res);
    }

    @Override
    public BigDecimal result() {
        return res.get();
    }

    @Override
    public void result(BigDecimal result) {
        this.res.set(result);
    }
}
