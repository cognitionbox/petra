package io.cognitionbox.petra.examples.tradingsystem2.objects.marketdata.historical;

import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.PrecisionNum;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DukascopyBarSeries extends BaseBarSeries {
    private ZonedDateTime startDatetime = ZonedDateTime.of(2017, 9, 15, 0, 0, 0, 0, ZoneId.systemDefault());

    public DukascopyBarSeries(String instrument, String filenameStartDate, String filenameEndDate) {
        Path path;
        try {
            path = Paths.get(DukascopyBarSeries.class.getResource('/' + instrument + "_Candlestick_1_M_BID_" + filenameStartDate + '-' + filenameEndDate + ".csv").toURI());
            Files.lines(path).skip(1).forEach(l -> parse(l));
            //Files.lines(path).skip(1).forEach(l->parse(l));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse(String line) {
        String[] values = line.split(",");
        BigDecimal open = new BigDecimal(values[1]);
        BigDecimal high = new BigDecimal(values[2]);
        BigDecimal low = new BigDecimal(values[3]);
        BigDecimal close = new BigDecimal(values[4]);
        BigDecimal vol = new BigDecimal(values[5]);
        try {
            addBar(startDatetime, PrecisionNum.valueOf(open), PrecisionNum.valueOf(high), PrecisionNum.valueOf(low), PrecisionNum.valueOf(close), PrecisionNum.valueOf(vol));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
