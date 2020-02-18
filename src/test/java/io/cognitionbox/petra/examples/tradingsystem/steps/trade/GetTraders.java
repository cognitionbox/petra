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
package io.cognitionbox.petra.examples.tradingsystem.steps.trade;


import io.cognitionbox.petra.examples.tradingsystem.objects.State;
import io.cognitionbox.petra.examples.tradingsystem.objects.Traders;
import io.cognitionbox.petra.lang.PEdge;

import static io.cognitionbox.petra.util.Petra.readOnly;
import static io.cognitionbox.petra.util.Petra.returns;

public class GetTraders extends PEdge<State, Traders> {
    {
        pre(readOnly(State.class,x->x.stateOk()));
        func(
                x -> {
                    return x.getTraders();
                }
        );
        post(returns(Traders.class, x->true));
    }
}