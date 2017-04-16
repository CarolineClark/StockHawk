package com.udacity.stockhawk.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.udacity.stockhawk.R;

import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    private XYPlot plot;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        plot = (XYPlot) findViewById(R.id.plot);
        plotStockHistory();
    }

    private void plotStockHistory() {
        String history = getIntent().getExtras().getString("history");
        Timber.d(history);
        String[] historyList = history.split("\n");
        List<Long> dates = new ArrayList<>();
        List<BigDecimal> closes = new ArrayList<>();

        for (String line: historyList) {
            String[] components = line.split(", ");
            String dateInMillis = components[0];
            String close = components[1];
            dates.add(0, Long.parseLong(dateInMillis));
            closes.add(new BigDecimal(close));
        }
        Long[] dateArray = new Long[dates.size()];
        dateArray = dates.toArray(dateArray);

        BigDecimal[] closeArray = new BigDecimal[closes.size()];
        closeArray = closes.toArray(closeArray);

        createGraph(dateArray, closeArray);
    }

    private void createGraph(final Number[] domainLabels, Number[] series1Numbers) {
        XYSeries series1 = new SimpleXYSeries(Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, Color.GREEN, null, null);

        plot.addSeries(series1, series1Format);

        plot.getGraph().setMarginBottom(150);
        plot.getGraph().setMarginLeft(50);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                String date = new SimpleDateFormat("yy-MM-dd").format(new Date((Long)domainLabels[i]));
                return toAppendTo.append(date);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }
}
