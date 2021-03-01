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
package io.cognitionbox.petra.core.impl;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class ObjectCopyerViaSerializationTest {

    /* Need to demo with more complex objects*/
    @Test
    public void copy() throws IOException, ClassNotFoundException {
        ObjectCopyerViaSerialization oc = new ObjectCopyerViaSerialization();
        Integer i = 7;
        Integer ci = oc.copy(i);
        assertThat(ci).isEqualTo(i);
        assertThat(ci).isNotSameAs(i);
    }
}