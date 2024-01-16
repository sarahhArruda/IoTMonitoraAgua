package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Turbidez extends AppCompatActivity {

    private BarChart barChart;
    private static final int REQUEST_CODE_PERMISSION = 123;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference valorAtual = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference valorGrafico04 = FirebaseDatabase.getInstance().getReference();
    private TextView valorCardTurb;
    private Toolbar t;

    private AppCompatButton btnExportarTurb;

    @SuppressLint("MissingInflatedId")
    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turbidez);

        t = (Toolbar) findViewById(R.id.toolbarTurbidez);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        barChart = findViewById(R.id.BarrasGraficoTurb);
        valorCardTurb = findViewById(R.id.valorAtualParametroTurbidez);

        BarDataSet barDataSet1 = new BarDataSet(dataSetValores(), "Turbidez");

        barDataSet1.setColor(Color.rgb(5, 58, 147));

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);

        barChart.setData(barData);
        barChart.invalidate();

        InserirValoresTurb();

        btnExportarTurb = findViewById(R.id.exportarTurb);
        btnExportarTurb.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                            ExportarDadosTurb();
                    }
                }
        );

    }
    public void ExportarDadosTurb()
    {
        ArrayList<String> arrayList = new ArrayList<>();

        List<Map<String, Object>> userList = new ArrayList<>();
        DatabaseReference exportar = FirebaseDatabase.getInstance().getReference();
        exportar.child("PAI").child("Sensor").child("Turbidez").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot userSnapshot : snapshot.getChildren())
                {
                    Map<String, Object> user = (Map<String, Object>) userSnapshot.getValue();
                    userList.add(user);
                    if (userList.size() == 10) break;
                }

                for (int i = userList.size() - 1; i >= 0; i--)
                {
                    Map<String, Object> user = userList.get(i);
                    float valor = Float.parseFloat(user.get("Turbidez").toString());
                    String valorString = String.valueOf(valor);
                    arrayList.add(valorString);
                }

                // Converter o ArrayList para JSON
                String json = new Gson().toJson(arrayList);

                // Exibir o JSON resultante no Logcat
                Log.d("JSON Resultante", json);

                // Salvar o JSON em um arquivo
                saveJsonToFile(json);

                // Exibir mensagem de sucesso
                Toast.makeText(Turbidez.this, "Exportação feita com Sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Exibir mensagem de erro
                Toast.makeText(Turbidez.this, "Erro ao adicionar!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveJsonToFile(String json)
    {
        try
        {
            // Verificar se o armazenamento externo está disponível para escrita
            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state))
            {
                // Obter o diretório de downloads no armazenamento externo
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

                // Criar o arquivo onde o JSON será salvo
                File file = new File(downloadsDir, "dados_turbidez.json");

                // Criar um fluxo de saída para escrever no arquivo
                try (FileOutputStream fos = new FileOutputStream(file);
                     OutputStreamWriter osw = new OutputStreamWriter(fos))
                {
                    // Escrever o JSON no arquivo
                    osw.write(json);
                }

                // Exibir mensagem de sucesso no Logcat
                Log.d("Salvando JSON", "JSON salvo com sucesso em: " + file.getAbsolutePath());
            }
            else
            {
                // Exibir mensagem de erro no Logcat se o armazenamento externo não estiver disponível
                Log.e("Salvando JSON", "Armazenamento externo não disponível para escrita");
            }
        }
        catch (IOException e)
        {
            // Lidar com exceções de I/O
            e.printStackTrace();
            Log.e("Salvando JSON", "Erro ao salvar JSON em arquivo");
        }
    }

    // Método para lidar com a resposta do usuário à solicitação de permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // A permissão foi concedida, execute o código para exportar dados
                ExportarDadosTurb();
            }
            else
            {
                // A permissão foi negada pelo usuário
                Log.e("Permissão Negada", "O usuário não concedeu permissão para escrever no armazenamento externo");
            }
        }
    }

    public void InserirValoresTurb()
    {
        valorAtual.child("PAI").child("LastRecord").child("Turbidez").addValueEventListener(
                new ValueEventListener()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String resultado = snapshot.getValue().toString();
                        valorCardTurb.setText(resultado + "");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(Turbidez.this, "Erro ao carregar!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        referencia.child("PAI").child("Sensor").child("Turbidez").addValueEventListener(
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
                        Toast.makeText(Turbidez.this, "Erro!", Toast.LENGTH_SHORT).show();
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
            float valor = Float.parseFloat(user.get("Turbidez").toString());
            dataValores.add(new BarEntry(index++, valor));
        }
        return dataValores;
    }

    private void atualizarGrafico(ArrayList<BarEntry> novosValores)
    {
        BarDataSet novoDataSet = new BarDataSet(novosValores, "Turbidez");
        novoDataSet.setColor(Color.rgb(5, 58, 147));

        BarData novoBarData = new BarData();
        novoBarData.addDataSet(novoDataSet);

        barChart.setData(novoBarData);
        barChart.invalidate();
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
    public void atualizarTurb(View view)
    {
        InserirValoresTurb();
        toastAtualizar(view);
    }
    public void toastAtualizar(View view)
    {
        Toast.makeText(
                getApplicationContext(), "Valores Atualizados!",Toast.LENGTH_LONG
        ).show();
    }
}