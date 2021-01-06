package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.RW;

import java.math.BigDecimal;

import static io.cognitionbox.petra.util.Petra.rw;

public class IntNullary implements IOp<Integer> {
    private final RW<Integer> res;
    public IntNullary(Integer res) {
        this.res = rw(res);
    }

    @Override
    public Integer result() {
        return res.get();
    }

    @Override
    public void result(Integer result) {
        this.res.set(result);
    }
}
