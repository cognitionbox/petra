/**
 * Copyright 2016-2020 Aran Hakki
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package petra.lang.impls.generics;

import io.cognitionbox.petra.core.IStep;
import io.cognitionbox.petra.lang.RGraphComputer;
import io.cognitionbox.petra.lang.StepTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XtoXStepTest extends StepTest<X> {

    @Test
    public void testSimple1() {
        RGraphComputer.getConfig().setConstructionGuaranteeChecks(false);
        List<A> list = new ArrayList<>();
        list.add(new B(0));
        list.add(new C(0));
        setInput(new X(list));
        setExpectation(x -> x.aList.stream().allMatch(a -> a.value == 1));
    }

    @Override
    protected Class<? extends IStep<X>> stepClass() {
        return XtoX.class;
    }
}
