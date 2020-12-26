package io.cognitionbox.petra.examples.kases.objects;

import io.cognitionbox.petra.examples.kases.math.*;

import java.math.BigDecimal;

public interface ABC extends BiMath, UnMath {
    UnMath toUn();
    BiMath toBi();
}
