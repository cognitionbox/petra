package io.cognitionbox.petra.lang;

import io.cognitionbox.petra.util.function.IFunction;

public class Forall<X, P> {
    public IFunction<X, Iterable<P>> getIterableTransformer() {
        return iterableTransformer;
    }
    public AbstractStep getStep() {
        return step;
    }
    private IFunction<X, Iterable<P>> iterableTransformer;
    private AbstractStep step;
    public Forall(IFunction<X, Iterable<P>> iterableTransformer, AbstractStep step) {
        this.iterableTransformer = iterableTransformer;
        this.step = step;
    }
}