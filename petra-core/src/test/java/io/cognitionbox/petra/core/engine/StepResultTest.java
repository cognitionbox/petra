package io.cognitionbox.petra.core.engine;

import io.cognitionbox.petra.core.Id;
import io.cognitionbox.petra.core.engine.petri.IToken;
import io.cognitionbox.petra.core.engine.petri.impl.Token;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertSame;

public class StepResultTest {

    @Test
    public void shouldNotChangeParameters() {
        IToken<Id> expectedInput = new Token<>(new Id(UUID.randomUUID().toString()));
        IToken<Id> expectedOutput = new Token<>(new Id(UUID.randomUUID().toString()));

        StepResult result = new StepResult(expectedInput, expectedOutput);

        assertSame(expectedInput, result.getInput());
        assertSame(expectedOutput, result.getOutputValue());

    }

}