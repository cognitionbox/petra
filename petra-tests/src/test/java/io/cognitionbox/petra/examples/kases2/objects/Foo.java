package io.cognitionbox.petra.examples.kases2.objects;

import io.cognitionbox.petra.lang.PIterable;
import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.V;
import io.cognitionbox.petra.util.impl.PList;

import java.math.BigDecimal;
import java.util.List;

import static io.cognitionbox.petra.util.Petra.ro;

public class Foo {
    public V<Boolean> bool;
    private final RO<ABC> abc1 = ro(new ABCImpl());
    //private final RO<ABC> abc2 = ro(new ABCImpl());

    public ABC getAbc1() {
        return abc1.get();
    }

    //public ABC getAbc2() {
    //    return abc2.get();
    //}
}
