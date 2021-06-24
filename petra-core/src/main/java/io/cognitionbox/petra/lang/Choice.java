package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.config.ExecMode;
import org.javatuples.Pair;

import java.util.List;

public class Choice {
    final List<Pair<ExecMode, List<TransformerStep>>> choice;
    public Choice(List<Pair<ExecMode, List<TransformerStep>>> choice) {
        this.choice = choice;
    }
}
