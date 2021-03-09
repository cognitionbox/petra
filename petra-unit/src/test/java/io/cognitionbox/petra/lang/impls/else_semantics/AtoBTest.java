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
package io.cognitionbox.petra.lang.impls.else_semantics;


import io.cognitionbox.petra.config.ExecMode;
import io.cognitionbox.petra.lang.impls.BaseExecutionModesTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@Ignore
public class AtoBTest extends BaseExecutionModesTest {

    public AtoBTest(ExecMode execMode) {
        super(execMode);
    }


    @Test
    public void testAtoB() {
        getGraphComputer().getConfig().setConstructionGuaranteeChecks(false);
        getGraphComputer().getConfig().setStrictModeExtraConstructionGuarantee(false);
        List<Value> values = new ArrayList<>();
        values.add(new Value("A"));
        values.add(new Value("A"));
        values.add(new Value("A"));
        values.add(new Value("A"));
        X res = (X) getGraphComputer().eval(new AtoB(), new X(new Value("A"), new Value("A"), values));
        assertThat(res.value1.value).isEqualTo("B");
        assertThat(res.value2.value).isEqualTo("B");
        assertThat(res.values.stream().map(v -> v.value).collect(Collectors.toList())).allMatch(v -> v == "B");
    }

}
