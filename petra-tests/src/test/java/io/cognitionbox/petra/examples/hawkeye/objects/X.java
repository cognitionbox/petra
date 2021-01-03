package io.cognitionbox.petra.examples.hawkeye.objects;

import io.cognitionbox.petra.lang.RO;
import io.cognitionbox.petra.lang.math.Nullary;
import io.cognitionbox.petra.lang.math.NullaryImpl;
import io.cognitionbox.petra.lang.math.UnaryImpl;
import io.cognitionbox.petra.util.impl.PList;

import java.math.BigDecimal;
import java.util.List;

import static io.cognitionbox.petra.lang.math.BigDecimalUtils.r;
import static io.cognitionbox.petra.util.Petra.ro;
import static io.cognitionbox.petra.util.Petra.rw;

public class X {
    public final Nullary i;
    public X(int in) {
        this.i = new NullaryImpl(r(in));
    }

    private boolean eq(int i){
        return this.i.result().equals(r(i));
    }

    public boolean isOne(){
        return eq(1);
    }

    public boolean isTwo(){
        return eq(2);
    }

    public boolean isFour(){
        return eq(4);
    }

    public boolean isEight(){
        return eq(8);
    }

    public boolean isSixteen(){
        return eq(16);
    }
}
