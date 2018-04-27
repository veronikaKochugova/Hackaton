package com.spbpu.hackaton.chartstemplate;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.EnumsAnchor;
import com.anychart.anychart.Position;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView btmNavigationView;
    private AnyChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chartView = findViewById(R.id.chartView);

        btmNavigationView = findViewById(R.id.btmNavView);
        btmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pieChartItem: {
                    }
                    break;
                    case R.id.barChartItem: {
                        Cartesian bar = AnyChart.column();

                        List<DataEntry> data = new ArrayList<>();
                        Random random = new Random();
                        for (int i = 0; i < 20; ++i) {
                            data.add(new ValueDataEntry(String.valueOf(i + 1), random.nextInt(200)));
                        }
                        CartesianSeriesColumn column = bar.column(data);
                        column.getTooltip()
                                .setTitleFormat("{%X}")
                                .setPosition(Position.CENTER_BOTTOM)
                                .setAnchor(EnumsAnchor.CENTER_BOTTOM)
                                .setOffsetX(0d)
                                .setOffsetY(5d)
                                .setFormat("${%Value}{groupsSeparator: }");
                        bar.setAnimation(true);
                        bar.setTitle("Hackathon bar chart test");

                        bar.getXAxis().setTitle("Numbers");
                        bar.getYAxis().setTitle("Different numbers");

                        chartView.setChart(bar);
                    }
                    break;
                    case R.id.lineChartItem:
                        break;
                }
                return false;
            }
        });
    }
}
