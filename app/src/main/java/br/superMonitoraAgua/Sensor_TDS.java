package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class Sensor_TDS extends AppCompatActivity
{
    private BarChart graficoBarra;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_tds);

        graficoBarra = findViewById(R.id.BarrasGraficoTDS);

        BarDataSet barDataSet1 = new BarDataSet(dataSetValores(), "TDS");

        barDataSet1.setColor(Color.rgb(5, 58, 147));

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);

        graficoBarra.setData(barData);
        graficoBarra.invalidate();
    }

    private ArrayList<BarEntry> dataSetValores()
    {
        ArrayList<BarEntry> dataValores = new ArrayList<>();
        dataValores.add(new BarEntry(0, 20f));
        dataValores.add(new BarEntry(1, 10f));
        dataValores.add(new BarEntry(2, 5f));
        dataValores.add(new BarEntry(3, 16f));

        return dataValores;
    }
}