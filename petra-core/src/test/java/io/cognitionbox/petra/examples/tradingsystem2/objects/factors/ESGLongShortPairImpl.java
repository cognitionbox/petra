package io.cognitionbox.petra.examples.tradingsystem2.objects.factors;

public class ESGLongShortPairImpl implements ESGLongShortPair {
    private String toLong;
    private String toShort;

    @Override
    public String getToLong() {
        return toLong;
    }

    @Override
    public void setToLong(String toLong) {
        this.toLong = toLong;
    }

    @Override
    public String getToShort() {
        return toShort;
    }

    @Override
    public void setToShort(String toShort) {
        this.toShort = toShort;
    }
}
