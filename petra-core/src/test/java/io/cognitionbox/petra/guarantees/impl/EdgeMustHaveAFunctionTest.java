package io.cognitionbox.petra.guarantees.impl;

import io.cognitionbox.petra.lang.PEdge;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EdgeMustHaveAFunctionTest {

    @Test
    public void shouldSucceedIfEdgeHasAFunction() {

        EdgeMustHaveAFunction check = new EdgeMustHaveAFunction();

        PEdge<Object> edge = new PEdge();
        edge.func(o -> {});

        boolean result = check.test(edge);

        assertTrue(result);

    }

    @Test
    public void shouldFailIfEdgeDoesNotHaveFunction() {

        EdgeMustHaveAFunction check = new EdgeMustHaveAFunction();

        PEdge<Object> edge = new PEdge<>();
        edge.func(null);

        boolean result = check.test(edge);

        assertFalse(result);

    }

}