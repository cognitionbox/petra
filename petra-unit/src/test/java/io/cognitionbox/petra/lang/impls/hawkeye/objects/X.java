package io.cognitionbox.petra.lang.impls.hawkeye.objects;

import java.math.BigDecimal;

public class X {
    public BigDecimal i;
    public X(int in) {
        this.i = BigDecimal.valueOf(in);
    }

    private boolean eq(int i){
        return this.i.intValue()==i;
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
