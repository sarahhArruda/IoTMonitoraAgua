package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sensor_TDS extends AppCompatActivity
{
    private BarChart graficoBarra;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference valorAtual = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference valorGrafico01 = FirebaseDatabase.getInstance().getReference();
    private TextView valParametroTDS, valParametroCDE;
    private Toolbar t;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_tds);

        t = (Toolbar) findViewById(R.id.toolbarSensorTDS);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        graficoBarra = findViewById(R.id.BarrasGraficoTDS);

        valParametroTDS = (TextView) findViewById(R.id.valorAtualParametroTDS);
        valParametroCDE = (TextView) findViewById(R.id.valorAtualParametroCDE);

        BarDataSet barDataSet1 = new BarDataSet(dataSetValores(), "TDS");

        barDataSet1.setColor(Color.rgb(5, 58, 147));

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);

        graficoBarra.setData(barData);
        graficoBarra.invalidate();

        InserirDadosTDS();


    }

    //insere dados nos gráficos e cards
    public void InserirDadosTDS()
    {

        //insere o ultimo valor coletado e insere no card do parametro de TDS
        valorAtual.child("PAI").child("LastRecord").child("TDS").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String resultado = snapshot.getValue().toString();
                        valParametroTDS.setText(resultado + " mg/L");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(Sensor_TDS.this, "Erro!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        referencia.child("PAI").child("LastRecord").child("Condutividade").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String resultado = snapshot.getValue().toString();
                        valParametroCDE.setText(resultado + " µS/cm");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Sensor_TDS.this, "Erro!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        //insere os dados e plota o grafico do paremtro TDS
        valorGrafico01.child("PAI").child("Sensor").child("TDS").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        ArrayList<BarEntry> dataValores = dataSetValoresFirebase(snapshot);
                        atualizarGrafico(dataValores);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(Sensor_TDS.this, "Erro!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
    private ArrayList<BarEntry> dataSetValoresFirebase(DataSnapshot dataSnapshot)
    {
        ArrayList<BarEntry> dataValores = new ArrayList<>();

        List<Map<String, Object>> userList = new ArrayList<>();
        for (DataSnapshot userSnapshot : dataSnapshot.getChildren())
        {
            Map<String, Object> user = (Map<String, Object>) userSnapshot.getValue();
            userList.add(user);
            if (userList.size() == 5) break;
        }

        int index = 0;
        for (int i = userList.size() - 1; i >= 0; i--)
        {
            Map<String, Object> user = userList.get(i);
            float valor = Float.parseFloat(user.get("TDS").toString());
            dataValores.add(new BarEntry(index++, valor));
        }

        return dataValores;
    }

    private void atualizarGrafico(ArrayList<BarEntry> novosValores)
    {
        BarDataSet novoDataSet = new BarDataSet(novosValores, "TDS");
        novoDataSet.setColor(Color.rgb(5, 58, 147));

        BarData novoBarData = new BarData();
        novoBarData.addDataSet(novoDataSet);

        graficoBarra.setData(novoBarData);
        graficoBarra.invalidate();
    }


    //feito pra plotar um gráfico com valores ficticios que são atualizados e substituidos por valores que estão no Firebase
    private ArrayList<BarEntry> dataSetValores()
    {
        ArrayList<BarEntry> dataValores = new ArrayList<>();
        dataValores.add(new BarEntry(0, 20f));
        dataValores.add(new BarEntry(1, 10f));
        dataValores.add(new BarEntry(2, 5f));
        dataValores.add(new BarEntry(3, 16f));
        dataValores.add(new BarEntry(4, 16f));
        return dataValores;
    }

    public void atualizar(View view)
    {
        InserirDadosTDS();
        toastAtualizar(view);
    }

    //mostra mensagem dizendo que os valores foram atualizados
    public void toastAtualizar(View view)
    {
        Toast.makeText(
                getApplicationContext(), "Valores Atualizados!",Toast.LENGTH_LONG
        ).show();
    }
}