package io.cognitionbox.petra.examples.kases.math;

import java.math.BigDecimal;

public final class R {

    static public boolean notNull(BigDecimal value){
        return value!=null;
    }

    static public boolean isNeg(BigDecimal value){
        return value.compareTo(BigDecimal.ZERO)==-1;
    }

    static public boolean isPos(BigDecimal value){
        return value.compareTo(BigDecimal.ZERO)==1;
    }

    static public boolean isZero(BigDecimal value){
        return value.compareTo(BigDecimal.ZERO)==0;
    }

    public static final BigDecimal ZERO = new BigDecimal(0);
    public static final BigDecimal ONE = new BigDecimal(1);
    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal THREE = new BigDecimal(3);
}
