package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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

public class Dashboard extends AppCompatActivity
{

    private BottomNavigationView navigationView;

    private Toolbar t;
    private LineChart lineChart;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private Handler handler = new Handler();
    private final int INTERVALO_ATUALIZACAO = 10 * 60 * 1000; // 10 minutos em milissegundos

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        t = findViewById(R.id.toolbarDashboard);



        lineChart = (LineChart) findViewById(R.id.LinhasGraficoDashBoard);
       inserirDados();
       

        navigationView = (BottomNavigationView) findViewById(R.id.navegacaoDash);
        navigationView.setOnNavigationItemSelectedListener
                (
                item ->
                {
                    switch (item.getItemId())
                    {
                        case (R.id.Home):
                            startActivity(new Intent(this, Dashboard.class));
                            return true;

                        case (R.id.dadosItemMenu):
                            startActivity(new Intent(this, Dados.class));

                            return true;
                        case (R.id.notificaçõesDados):
                            startActivity(new Intent(this, Alertas_Dados.class));

                            return true;

                        case (R.id.usuarioItemMenu):
                            startActivity(new Intent(this, Conta.class));
                            return true;
                        default:
                            return false;
                    }
                }
        );
    }

    //faz a inserção de dados no gráfico de barras do parametro de temperatura
    public void inserirDados()
    {
        View view = null;
        
        referencia.child("PAI").child("Sensor").child("Temperatura").addValueEventListener
        (
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        List<Map<String, Object>> userList = new ArrayList<>();
                        for (DataSnapshot userSnapshot : snapshot.getChildren())
                        {
                            Map<String, Object> user = (Map<String, Object>) userSnapshot.getValue();
                            userList.add(user);
                            if(userList.size() == 5) break;
                        }
                        ArrayList<Entry> yValues = new ArrayList<>();
                        int index = 0;

                        for (int i = userList.size() - 1; i >= 0; i--)
                        {
                            Map<String, Object> user = userList.get(i);
                            float a = 0;
                            String b = "0";
                            for (Map.Entry<String, Object> entry : user.entrySet())
                            {
                                if ("Temperatura".equals(entry.getKey()))
                                {
                                    a = Float.parseFloat(entry.getValue().toString());
                                }
                                else
                                {
                                    b = entry.getValue().toString();
                                }
                                Log.i("FirebaseData", a + " : " + b);
                            }
                            yValues.add(new Entry(index++, a));
                        }

                        LineDataSet set1 = new LineDataSet(yValues, "Temperatura");
                        set1.setFillAlpha(110);
                        set1.setLineWidth(3f);
                        set1.setValueTextSize(10f);
                        set1.setCircleColor(Color.RED); // Cor dos pontos de dados
                        set1.setCircleHoleColor(Color.WHITE); // Cor do centro dos pontos
                        set1.setValueTextColor(Color.BLUE); // Cor do texto
                        set1.setLineWidth(3f); // Espessura da linha
                        set1.setCircleRadius(6f); // Tamanho dos pontos de dados
                        set1.setDrawValues(true); // Exibir valores nos pontos de dados

                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set1);

                        LineData data = new LineData(dataSets);
                        lineChart.setData(data);

                        String[] dias = new String[]{"Dia 01", "Dia 02", "Dia 03", "Dia 04", "Dia 05"};

                        XAxis xAxis = lineChart.getXAxis();
                        xAxis.setValueFormatter(new MyAxisFormatter(dias));
                        xAxis.setGranularity(1f);

                        YAxis esquerda = lineChart.getAxisLeft();
                        YAxis direita = lineChart.getAxisRight();
                        direita.removeAllLimitLines();
                        direita.setAxisMinimum(0f);
                        direita.setAxisMaximum(100f);
                        esquerda.removeAllLimitLines();
                        esquerda.setAxisMaximum(100f);
                        esquerda.setAxisMinimum(0f);
                        esquerda.enableGridDashedLine(10f, 10f, 0);
                        esquerda.setDrawLimitLinesBehindData(true);

                        LimitLine limiteSuperior = new LimitLine(60f, "Limite Superior");
                        limiteSuperior.setLineWidth(2f);
                        limiteSuperior.enableDashedLine(10f, 10f, 0f);
                        limiteSuperior.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                        limiteSuperior.setTextSize(10f);

                        LimitLine limiteInferior = new LimitLine(10f, "Limite Inferior");
                        limiteInferior.setLineWidth(2f);
                        limiteInferior.enableDashedLine(10f, 10f, 0f);
                        limiteInferior.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
                        limiteInferior.setTextSize(10f);

                        esquerda.addLimitLine(limiteSuperior);
                        esquerda.addLimitLine(limiteInferior);

                        esquerda.enableGridDashedLine(10f, 10f, 0);
                        esquerda.setDrawLimitLinesBehindData(true);

                        lineChart.notifyDataSetChanged();
                        lineChart.invalidate();
                        iniciarAtualizacaoAutomatica(view);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                }
        );
    }

    private void iniciarAtualizacaoAutomatica(View view) {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                inserirDados();
                toastAtualizarDash(view);
                // Chama a função novamente após o intervalo de atualização
                handler.postDelayed(this, INTERVALO_ATUALIZACAO);
            }
        }, INTERVALO_ATUALIZACAO);
    }

    public void toastAtualizarDash(View view)
    {
        Toast.makeText(
                getApplicationContext(), "Valores Atualizados!",Toast.LENGTH_LONG
        ).show();
    }

    @Override
    protected void onDestroy() {
        // Remove o callback do handler para evitar vazamentos de memória quando a atividade é destruída
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
    public void irCardSensorTDS(View view)
    {
        Intent intent = new Intent(this, Sensor_TDS.class);
        startActivity(intent);
    }
    public static class MyAxisFormatter extends ValueFormatter
    {
        public String[] mdias;

        public MyAxisFormatter(String[] dias) {
            this.mdias = dias;
        }

        @Override
        public String getFormattedValue(float value) {
            return mdias[(int) value];
        }
    }
}