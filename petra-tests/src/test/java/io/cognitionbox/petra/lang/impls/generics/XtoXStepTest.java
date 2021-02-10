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
package io.cognitionbox.petra.lang.impls.generics;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.AbstractStep;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@RunWith(Parameterized.class)
public class XtoXStepTest extends StepTest<X> {

  public XtoXStepTest(ExecMode execMode) {
    super(execMode);
  }

  @Override
  protected Supplier<AbstractStep<X>> stepSupplier() {
    return ()->new XtoX();
  }

  @Test
  public void testSimple1() {
    RGraphComputer.getConfig().setConstructionGuaranteeChecks(false);
    List<A> list = new ArrayList<>();
    list.add(new B(0));
    list.add(new C(0));
    setInput(new X(list));
    setExpectation(x->x.aList.stream().allMatch(a->a.value==1));
  }
}
