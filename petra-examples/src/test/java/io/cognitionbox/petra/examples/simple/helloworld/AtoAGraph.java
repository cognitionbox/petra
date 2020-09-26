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
package io.cognitionbox.petra.examples.simple.helloworld;

import io.cognitionbox.petra.lang.PGraph;

import static io.cognitionbox.petra.util.Petra.rt;


public class AtoAGraph extends PGraph<A> {
    {
       type(A.class);
       pre(x->getCurrentIteration()==0);
       step(new AtoA());
       post(x->getCurrentIteration()==1);
    }
}