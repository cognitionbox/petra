package io.cognitionbox.petra.lang.math;

import io.cognitionbox.petra.lang.RW;

import static io.cognitionbox.petra.util.Petra.rw;

public class IntUnary implements IIntegerOp, IOp<Integer> {
    private final RW<Integer> a;
    private final RW<Integer> res;
    public IntUnary(Integer res) {
        this.res = rw(res);
        this.a = rw();
    }

    @Override
    public Integer result() {
        return res.get();
    }

    @Override
    public void result(Integer result) {
        this.res.set(result);
    }

    public Integer a() {
        return a.get();
    }

    public void a(Integer a) {
        this.a.set(a);
    }
}
