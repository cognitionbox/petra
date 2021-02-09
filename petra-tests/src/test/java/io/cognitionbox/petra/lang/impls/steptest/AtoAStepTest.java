/**
 * Copyright 2016-2020 Aran Hakki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cognitionbox.petra.lang.impls.steptest;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class AtoAStepTest extends StepTest<A> {

  public AtoAStepTest(ExecMode execMode) {
    super(execMode);
  }

  @Override
  protected Supplier<AbstractStep<A>> stepSupplier() {
    return ()->new AtoA();
  }

  @Test
  public void testSimple() {
    RGraphComputer.getConfig().setConstructionGuaranteeChecks(false);
    setInput(new A(1));
    setExpectation(x->x.value==222);
  }
}
