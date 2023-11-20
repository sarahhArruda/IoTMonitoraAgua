package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

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



import java.util.ArrayList;

public class Dashboard extends AppCompatActivity
{

    private BottomNavigationView navigationView;

    private Toolbar t;
    private LineChart lineChart;

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
                        case (R.id.pontosItemMenu):
                            startActivity(new Intent(this, Pontos_de_Coleta.class));

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
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 20f));
        entries.add(new Entry(1, 35f));
        entries.add(new Entry(2, 20f));
        entries.add(new Entry(3, 38f));

        LineDataSet set1 = new LineDataSet(entries, "Temperatura");
        set1.setFillAlpha(110);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLUE);
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

        String[] dias = new String[]{"Dado 01", "Dado 02", "Dado 03", "Dado 04"};

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

        LimitLine limiteInferior = new LimitLine(20f, "Limite Inferior");
        limiteInferior.setLineWidth(2f);
        limiteInferior.enableDashedLine(10f, 10f, 0f);
        limiteInferior.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        limiteInferior.setTextSize(10f);

        esquerda.addLimitLine(limiteSuperior);
        esquerda.addLimitLine(limiteInferior);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

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