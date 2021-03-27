package io.cognitionbox.petra.lang;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class Cover implements Serializable {
    private List<Integer> kaseIds;
    public Cover(Integer... kaseIds) {
        this.kaseIds = Arrays.asList(kaseIds);
    }

    public List<Integer> getKaseIds() {
        return kaseIds;
    }


}
