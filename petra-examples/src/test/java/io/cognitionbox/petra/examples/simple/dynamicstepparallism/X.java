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
package io.cognitionbox.petra.examples.simple.dynamicstepparallism;


import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.lang.annotations.Extract;

//@Extract
class X {
    A a1 = new A();
    A a2 = new A();

    @Extract
    public A getA1() {
        return a1;
    }

    @Extract
    public A getA2() {
        return a2;
    }

    @Extract
    public AList getaList() {
        return aList;
    }


    AList aList = new AList();

    {
        aList.add(new A());
        aList.add(new A());
        aList.add(new A());
        aList.add(new A());
    }
}