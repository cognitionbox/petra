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
package io.cognitionbox.petra.examples.tradingsystem.steps.trade;


import io.cognitionbox.petra.examples.tradingsystem.objects.Decisions;
import io.cognitionbox.petra.examples.tradingsystem.objects.TraderTick;
import io.cognitionbox.petra.examples.tradingsystem.steps.TraderTickOk;
import io.cognitionbox.petra.lang.PEdge;
import io.cognitionbox.petra.util.Petra;

import static io.cognitionbox.petra.util.Petra.rc;
import static io.cognitionbox.petra.util.Petra.rt;


public class Trade extends PEdge<TraderTick, Decisions> {
    {
       pre(rc(TraderTickOk.class, x->x.traderTickOk()));
       func(
                x -> {
                    //int y = 1/0;
                    return x.trader().runStrategy(x.tick());
                }
        );
        post(Petra.rt(DecisionsOk.class, x->x.decisionsOk()));
    }
}