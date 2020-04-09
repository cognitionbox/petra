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
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.util.impl.PList;

import io.cognitionbox.petra.util.Petra;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;


import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class ExtractAnnotationTest extends BaseExecutionModesTest {


  public ExtractAnnotationTest(ExecMode execMode) {
    super(execMode);
  }

  @Extract
  public static class IntListWithExtract extends PList<Integer> {}

  public static class IntListWithNoExtract extends PList<Integer> {}


  public static class JoinAllWithExtract extends PGraph<IntListWithExtract, String> {
    {
      pre(rc(IntListWithExtract.class, x->true));
      post(Petra.rt(String.class, x->true)); // must use equals instead of == due to serialization
    }
  }


  public static class StepIntListWithExtract extends PEdge<IntListWithExtract, String> {
    {
      pre(rc(IntListWithExtract.class, x->true));
      func(x->"done");
      post(Petra.rt(String.class, x->true));
    }
  }


  public static class JoinAllWithNoExtract extends PGraph<IntListWithNoExtract, String> {
    {
      pre(rc(IntListWithNoExtract.class, x->true));
      step(new StepIntListWithNoExtract());
      post(Petra.rt(String.class, x->true)); // must use equals instead of == due to serialization
    }
  }


  public static class StepIntListWithNoExtract extends PEdge<IntListWithNoExtract, String> {
    {
      pre(rc(IntListWithNoExtract.class, x->true));
      func(x->"done");
      post(Petra.rt(String.class, x->true));
    }
  }

  @Test
  public void testWithAnnotation() {
    IntListWithExtract input = new IntListWithExtract();
    input.addAll(Arrays.asList(1, 2, 3, 4));
    JoinAllWithExtract joinAll = new JoinAllWithExtract();
    joinAll.
            joinSome(new TestWithAnnotationPureJoin());
    String result = (String) getGraphComputer().computeWithInput(joinAll,input);
    assertThat(result).isEqualTo("done");
  }

  public static class TestWithAnnotationPureJoin extends PJoin<Integer,String> {
    {
      pre(rc(Integer.class, x->true));
      func(x->"done");
      post(Petra.rt(String.class, x->x.equals("done")));
    }
  }

  @Test
  public void testWithNoAnnotation() {
    IntListWithNoExtract input = new IntListWithNoExtract();
    input.addAll(Arrays.asList(1, 2, 3, 4));
    JoinAllWithNoExtract joinAll = new JoinAllWithNoExtract();
    String result = (String) getGraphComputer().computeWithInput(joinAll,input);
    Assertions.assertThat(result).isNotNull();
  }

  public static class TestWithNoAnnotationPureJoin extends PJoin<Integer,String> {
    {
      pre(rc(Integer.class, x->true));
      func(x->"done");
      post(Petra.rt(String.class, x->x.equals("done")));
    }
  }

}
