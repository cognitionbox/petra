package io.cognitionbox.petra.examples.kases2.objects;

import io.cognitionbox.petra.lang.PGraphIterable;
import io.cognitionbox.petra.util.impl.PList;

import java.math.BigDecimal;
import java.util.List;

public class Foo implements PGraphIterable<BigDecimal> {

    private ABC abc1 = new ABCImpl();
    private ABC abc2 = new ABCImpl();

    public ABC getAbc1() {
        return abc1;
    }

    public ABC getAbc2() {
        return abc1;
    }

    private List<BigDecimal> RList = new PList<>();
    private BigDecimal sum = BigDecimal.ZERO;

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public List<BigDecimal> getRList() {
        return RList;
    }

    public void setRList(List<BigDecimal> RList) {
        this.RList = RList;
    }

    @Override
    public String toString() {
        return "ListMultSum{" +
                "RList=" + RList +
                ", sum=" + sum +
                ", abc1=" + abc1 +
                ", abc2=" + abc2 +
                '}';
    }

    BigDecimal r = null;
    @Override
    public BigDecimal iterationValue() {
        return r;
    }

    @Override
    public void setIterationValue(BigDecimal value) {
        this.r = value;
    }
    //    private Set<R> RSet = new PSet<>();
//    private Map<String,R> RMap = new PMap<>();
//
//    public Set<R> getRSet() {
//        return RSet;
//    }
//
//    public void setRSet(Set<R> RSet) {
//        this.RSet = RSet;
//    }
//
//    public Map<String, R> getRMap() {
//        return RMap;
//    }
//
//    public void setRMap(Map<String, R> RMap) {
//        this.RMap = RMap;
//    }
}
