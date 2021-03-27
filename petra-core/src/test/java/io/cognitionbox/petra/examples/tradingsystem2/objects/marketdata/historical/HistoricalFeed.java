package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical;

import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.AbstractFeed;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.Quote;
import io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.QuoteImp;
import org.ta4j.core.num.Num;
import org.ta4j.core.num.PrecisionNum;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class HistoricalFeed extends AbstractFeed {
    private final Map<String, List<Quote>> quoteSeriesMap = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> quoteDayMap = new ConcurrentHashMap<>();

    @Override
    public boolean isStarted(String instrument) {
        return quoteSeriesMap.containsKey(instrument);
    }

    public List<Quote> getQuoteDiffsForInstrument(String instrument) {
        final List<Quote> diffs = new ArrayList<>();
        final List<Quote> instrumentQuotes = quoteSeriesMap.get(instrument);
        for (int i = 0; i < instrumentQuotes.size() - 1; i++) {
            // The current day
            final Quote q1 = instrumentQuotes.get(i);
            // The next day
            final Quote q2 = instrumentQuotes.get(i + 1);

            Num delta = q2.getMid().minus(q1.getMid());

            Quote diff = new QuoteImp(delta, delta, delta, PrecisionNum.valueOf(0));
            diffs.add(diff);
        }

        return diffs;
    }

    @Override
    public void start(String instrument) {
        final List<String> lines = fileLines(instrument);
        final List<Quote> quotes = linesToQuotes(lines);
        quoteSeriesMap.put(instrument, quotes);
    }

    private List<String> fileLines(String instrument) {
        Path path;
        final List<String> list = new ArrayList<>();
        try {
            String filenameStartDate = "12.02.2018";
            String filenameEndDate = "12.02.2021";
            path = Paths.get(HistoricalFeed.class.getResource("/" + instrument + "_Candlestick_1_D_BID_" + filenameStartDate + "-" + filenameEndDate + ".csv").toURI());

            Files.lines(path).skip(1).forEach(list::add);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private List<Quote> linesToQuotes(List<String> lines) {
        final List<Quote> quotes = new ArrayList<>();
        for (String line : lines) {
            String[] splitLine = line.split(",");

            // CSV FORMAT: Gmt time,Open,High,Low,Close,Volume
            Num close = PrecisionNum.valueOf(splitLine[4]);

            Quote quote = new QuoteImp(close, close, close, PrecisionNum.valueOf(0));
            quotes.add(quote);
        }
        return quotes;
    }

    public void step(String instrument) {
        quoteDayMap.putIfAbsent(instrument,new AtomicInteger(0));
        quoteDayMap.get(instrument).incrementAndGet();
    }

    public int getDay(String instrument) {
        return quoteDayMap.getOrDefault(instrument,new AtomicInteger(0)).get();
    }


    @Override
    public Quote getQuote(String instrument) {
        if (!quoteSeriesMap.containsKey(instrument)) return null;

        final List<Quote> quotes = quoteSeriesMap.get(instrument);
        if (quotes.isEmpty()) return null;

        return quotes.get(getDay(instrument));

    }

    @Override
    public void addQuote(String instrument, Quote quote) {
        quoteSeriesMap.get(instrument).add(quote);
    }
}
