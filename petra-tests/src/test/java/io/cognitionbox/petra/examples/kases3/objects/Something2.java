package io.cognitionbox.petra.examples.kases3.objects;

import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.RW;
import io.cognitionbox.petra.util.impl.PList;

import java.util.List;

import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rw;

public class Something2 {
    final public RW<Integer> in = rw(null);
    final public RW<Integer> out = rw(null);

    final public List<RO<Integer>> lookup = new PList<>();
    {
        lookup.add(ro(1));
        lookup.add(ro(1));
        lookup.add(ro(1));
        lookup.add(ro(1));
        lookup.add(ro(1));
        lookup.add(ro(2));
        lookup.add(ro(2));
        lookup.add(ro(2));
        lookup.add(ro(2));
        lookup.add(ro(2));
    }

    public Something2(int in) {
        this.in.set(in);
    }
}
