package io.cognitionbox.petra.lang.math;

import java.math.BigDecimal;

public final class BigDecimalUtils {

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

    private static class Interval {
        private enum Type {
            OPEN,
            CLOSED
        }
        private Type type;
        private BigDecimal value;
        private Interval(Type type, BigDecimal value) {
            this.type = type;
            this.value = value;
        }
    }

    public static Interval inc(double value){
        return inc(r(value));
    }

    public static Interval exc(double value){
        return exc(r(value));
    }

    public static Interval inc(BigDecimal value){
        return new Interval(Interval.Type.CLOSED,value);
    }

    public static Interval exc(BigDecimal value){
        return new Interval(Interval.Type.OPEN,value);
    }

    public static boolean range(Interval a, BigDecimal x, Interval b){
        int c1 = x.compareTo(a.value);
        int c2 = x.compareTo(b.value);
        return ((a.type==Interval.Type.CLOSED && c1==0) || c1==1) && ((b.type==Interval.Type.CLOSED && c2==0) || c2==-1);
    }

    static public BigDecimal r(int value){
        return new BigDecimal(value);
    }

    static public BigDecimal r(double value){
        return new BigDecimal(value);
    }

    public static final BigDecimal ZERO = new BigDecimal(0);
    public static final BigDecimal ONE = new BigDecimal(1);
    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal THREE = new BigDecimal(3);
}
