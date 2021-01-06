package io.cognitionbox.petra.examples.hawkeye.steps.hawkeye6.objects;

import io.cognitionbox.petra.lang.RW;
import io.cognitionbox.petra.lang.math.IntNullary;
import io.cognitionbox.petra.lang.math.IntUnary;
import io.cognitionbox.petra.util.impl.PList;

import java.util.List;

import static io.cognitionbox.petra.util.Petra.rw;

public class Outer {
    public final List<RW<Integer>> integers;
    public final IntNullary i = new IntNullary(0);
    public final IntNullary j = new IntNullary(0);
    public final IntUnary lengthMinusI;
    public final RW<Integer> jMin = rw();
    public Outer(List<RW<Integer>> integers) {
        this.integers = integers;
        this.lengthMinusI = new IntUnary(this.integers.size());
    }
    public void swap(List<RW<Integer>> integers, int a, int b){
        int tmp = integers.get(b).get();
        integers.set(b,integers.get(a));
        integers.set(a,rw(tmp));
    }
}
