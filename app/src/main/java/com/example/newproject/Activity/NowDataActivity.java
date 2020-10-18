package com.example.newproject.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.newproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class NowDataActivity extends BaseActivity {

    PieChart stuff_Chart, service_Chart;
    ArrayList<Entry> stuff_entries, service_entries;
    ArrayList<String> stuff_label, service_label;
    PieDataSet stuff_dataset, service_dataset;
    PieData stuff_data, service_data;

    Toolbar toolbar;
    TextView toolbar_title;
    private String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nowdata);

        stuff_Chart = (PieChart) findViewById(R.id.stuff);
        service_Chart = (PieChart) findViewById(R.id.service);

        stuff_entries = new ArrayList<>();
        service_entries = new ArrayList<>();

        stuff_label = new ArrayList<String>();
        service_label = new ArrayList<String>();

        AddValuesToPIEENTRY();
        AddValuesToPieEntryLabels();

        stuff_dataset = new PieDataSet(stuff_entries, "");
        stuff_data = new PieData(stuff_label, stuff_dataset);
        stuff_dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        stuff_Chart.setData(stuff_data);
        stuff_Chart.animateY(2000);

        service_dataset = new PieDataSet(service_entries, "");
        service_data = new PieData(service_label, service_dataset);
        service_dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        service_Chart.setData(service_data);
        service_Chart.animateY(2000);

        toolbar = (Toolbar)findViewById(R.id.top_toolbar);
        toolbar_title = (TextView)findViewById(R.id.toolbar_title);

        Intent intent = getIntent();
        loc = intent.getStringExtra("name");
        System.out.println(loc);
        setTitle();
    }
    public void setTitle(){
        toolbar_title.setText(loc);
    }
    public void AddValuesToPIEENTRY(){
        stuff_entries.add(new BarEntry(1f, 0));
        stuff_entries.add(new BarEntry(5f, 1));
        stuff_entries.add(new BarEntry(9f, 2));
        stuff_entries.add(new BarEntry(2f, 3));

        service_entries.add(new BarEntry(10f, 0));
        service_entries.add(new BarEntry(2f, 1));
        service_entries.add(new BarEntry(3f, 2));
        service_entries.add(new BarEntry(6f, 3));

    }

    public void AddValuesToPieEntryLabels(){
        stuff_label.add("식품");
        stuff_label.add("의류");
        stuff_label.add("생활용품");
        stuff_label.add("기타");

        service_label.add("보건/의료");
        service_label.add("재해/재난");
        service_label.add("농/어촌");
        service_label.add("기타");
    }
}
