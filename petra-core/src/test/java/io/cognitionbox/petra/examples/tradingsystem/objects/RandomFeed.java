package io.cognitionbox.petra.examples.tradingsystem.objects;

public class RandomFeed implements Feed {
    @Override
    public Tick sourceTick() {
        return new Tick(Math.random(), Math.random());
    }
}
