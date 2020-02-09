package io.cognitionbox.petra.examples.tradingsystem.objects;

public class RandomFeed implements Feed {

    private InstrumentId instrument;

    public RandomFeed(InstrumentId instrument) {
        this.instrument = instrument;
    }

    @Override
    public Tick sourceTick() {
        return new Tick(instrument, 1.0, 1.0);
    }

    @Override
    public void sinkTick(Tick tick) {

    }


    @Override
    public InstrumentId getInstrument() {
        return instrument;
    }
}
