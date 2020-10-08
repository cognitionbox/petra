package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.RandomFeed;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class SubscribeToFeed extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(x->!x.hasFeed());
        func(x ->x.setFeed(new RandomFeed()));
        post(x->x.hasFeed());
    }
}
