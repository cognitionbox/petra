package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.core.IStep;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Ignore {
    private final Class<? extends IStep> stepClazz;
    private final List<Integer> kaseIds;
    public Ignore(Class<? extends IStep> stepClazz, Integer... kaseIds) {
        this.stepClazz = stepClazz;
        this.kaseIds = Arrays.asList(kaseIds);
    }

    public List<Integer> getKaseIds() {
        return kaseIds;
    }

    public Class<? extends IStep> getStepClazz() {
        return stepClazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ignore ignore = (Ignore) o;
        return stepClazz.equals(ignore.stepClazz) &&
                kaseIds.equals(ignore.kaseIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stepClazz, kaseIds);
    }
}
