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