/**
 * Copyright (C) 2016-2020 Aran Hakki.
 * <p>
 * This file is part of Petra.
 * <p>
 * Petra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Petra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Petra.  If not, see <https://www.gnu.org/licenses/>.
 */
package io.cognitionbox.petra.examples.simple.paralleloop;

import io.cognitionbox.petra.examples.simple.common.AB;
import io.cognitionbox.petra.examples.simple.common.IncrementA;
import io.cognitionbox.petra.examples.simple.common.IncrementB;
import io.cognitionbox.petra.lang.PGraph;


public class ABtoAB extends PGraph<AB> {
    {
        type(AB.class);
        iterations(a->10);
        pre(x -> x.getA().value >= 0 && x.getA().value <= 10 && x.getB().value >= 0 && x.getB().value <= 10);
        begin();
        step(x -> x.getA(), new IncrementA());
        step(x -> x.getB(), new IncrementB());
        end();
        post(x -> x.getA().value == 10 && x.getB().value == 10);
    }
}