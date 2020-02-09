/**
 * Copyright (C) 2016-2020 Aran Hakki.
 *
 * This file is part of Petra.
 *
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.lang.impls;

import io.cognitionbox.petra.lang.annotations.Extract;
import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.PGraph;
import io.cognitionbox.petra.lang.PJoin;
import io.cognitionbox.petra.util.impl.PList;

import io.cognitionbox.petra.util.Petra;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;


import static io.cognitionbox.petra.util.Petra.readConsume;
import static io.cognitionbox.petra.util.Petra.returns;
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
      pre(readConsume(IntListWithExtract.class, x->true));
      post(Petra.returns(String.class, x->true)); // must use equals instead of == due to serialization
    }
  }


  public static class StepIntListWithExtract extends PEdge<IntListWithExtract, String> {
    {
      pre(readConsume(IntListWithExtract.class, x->true));
      func(x->"done");
      post(Petra.returns(String.class, x->true));
    }
  }


  public static class JoinAllWithNoExtract extends PGraph<IntListWithNoExtract, String> {
    {
      pre(readConsume(IntListWithNoExtract.class, x->true));
      step(new StepIntListWithNoExtract());
      post(Petra.returns(String.class, x->true)); // must use equals instead of == due to serialization
    }
  }


  public static class StepIntListWithNoExtract extends PEdge<IntListWithNoExtract, String> {
    {
      pre(readConsume(IntListWithNoExtract.class, x->true));
      func(x->"done");
      post(Petra.returns(String.class, x->true));
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
      pre(readConsume(Integer.class, x->true));
      func(x->"done");
      post(Petra.returns(String.class, x->x.equals("done")));
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
      pre(readConsume(Integer.class, x->true));
      func(x->"done");
      post(Petra.returns(String.class, x->x.equals("done")));
    }
  }

}
