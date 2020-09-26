package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class StateTransformerStep<X, P> implements TransformerStep {
    public IFunction<X, P> getTransformer() {
        return transformer;
    }
    public AbstractStep getStep() {
        return step;
    }
    private IFunction<X, P> transformer;
    private AbstractStep step;

    public StateTransformerStep(IFunction<X, P> transformer, AbstractStep step) {
        this.transformer = transformer;
        this.step = step;
    }
}