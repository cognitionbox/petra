package io.cognitionbox.petra.lang.impls.kases3.objects;
import io.cognitionbox.petra.lang.Ref;
import io.cognitionbox.petra.util.impl.PList;

import java.util.List;

import static io.cognitionbox.petra.util.Petra.ref;

public class Something2 {
    final public Ref<Integer> in = ref(null);
    final public Ref<Integer> out = ref(null);

    final public List<Ref<Integer>> lookup = new PList<>();
    {
        lookup.add(ref(1));
        lookup.add(ref(1));
        lookup.add(ref(1));
        lookup.add(ref(2));
        lookup.add(ref(2));
        lookup.add(ref(2));
    }

    public Something2(int in) {
        this.in.set(in);
    }
}
