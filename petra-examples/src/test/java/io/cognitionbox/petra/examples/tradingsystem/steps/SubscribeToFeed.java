package io.cognitionbox.petra.examples.tradingsystem.steps;

import io.cognitionbox.petra.examples.tradingsystem.objects.RandomFeed;
import io.cognitionbox.petra.examples.tradingsystem.objects.Trader;
import io.cognitionbox.petra.lang.PEdge;

public class SubscribeToFeed extends PEdge<Trader> {
    {
        type(Trader.class);
        pre(trader->!trader.hasFeed());
        func(trader ->trader.setFeed(new RandomFeed()));
        post(trader->trader.hasFeed());
    }
}
