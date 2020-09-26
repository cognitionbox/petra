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
package io.cognitionbox.petra.examples.simple.autoretry;

import io.cognitionbox.petra.core.IRollback;
import io.cognitionbox.petra.examples.simple.common.A;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.lang.annotations.Feedback;

import static io.cognitionbox.petra.util.Petra.rt;

@Feedback
public class IncrementA extends PEdge<A> implements IRollback<A> {
    {
       type(A.class);
       pre(a->a.value>=0 && a.value<10);
       func(a->{
            if (Math.random()>=0.2){
                throw new IllegalStateException();
            }
            a.value++;
            System.out.println("A="+a.value);
        });
        post(a->a.value>=0 && a.value<=10);
    }

    @Override
    public void capture(A a) {
        a.setCaptured(a.value);
    }

    @Override
    public void rollback(A a) {
        a.value = a.getCaptured();
    }
}