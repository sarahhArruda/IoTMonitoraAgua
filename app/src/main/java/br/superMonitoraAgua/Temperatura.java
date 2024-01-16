package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.firebase.database.*;
import com.google.gson.Gson;

import android.Manifest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Temperatura extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 123;
    private Toolbar t;
    private DatabaseReference refCardTemp = FirebaseDatabase.getInstance().getReference();
    private TextView valorCardTemp;
    private DatabaseReference valorGrafico01Temp = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference exportar = FirebaseDatabase.getInstance().getReference();
    private AppCompatButton botaoExportar;

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        t = (Toolbar) findViewById(R.id.toolbarSensorTemperatura);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        barChart = findViewById(R.id.BarrasGraficoTemperatura);
        valorCardTemp = findViewById(R.id.valorAtualParametroTemperatura);
        botaoExportar = findViewById(R.id.exportarTemperatura);

        BarDataSet barDataSet1 = new BarDataSet(dataSetValores(), "Temperatura");

        barDataSet1.setColor(Color.rgb(5, 58, 147));

        BarData barData = new BarData();
        barData.addDataSet(barDataSet1);

        barChart.setData(barData);
        barChart.invalidate();

        InserirValoresTemperatura();

        botaoExportar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Adicionando logs para depuração
                Log.d("MinhaTag", "Botão de exportar clicado.");

                // Executando código para exportar dados sem verificar permissão
                ExportarDadosTemp();
            }
        });
    }
    public void ExportarDadosTemp() 
    {
        ArrayList<String> arrayList = new ArrayList<>();

        List<Map<String, Object>> userList = new ArrayList<>();
        DatabaseReference exportar = FirebaseDatabase.getInstance().getReference();
        exportar.child("PAI").child("Sensor").child("Temperatura").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Map<String, Object> user = (Map<String, Object>) userSnapshot.getValue();
                    userList.add(user);
                    if (userList.size() == 10) break;
                }

                for (int i = userList.size() - 1; i >= 0; i--) {
                    Map<String, Object> user = userList.get(i);
                    float valor = Float.parseFloat(user.get("Temperatura").toString());
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
                Toast.makeText(Temperatura.this, "Exportação feita com Sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Exibir mensagem de erro
                Toast.makeText(Temperatura.this, "Erro ao adicionar!", Toast.LENGTH_SHORT).show();
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
                File file = new File(downloadsDir, "dados.json");

                // Criar um fluxo de saída para escrever no arquivo
                try (FileOutputStream fos = new FileOutputStream(file);
                     OutputStreamWriter osw = new OutputStreamWriter(fos)) {
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
                ExportarDadosTemp();
            }
            else
            {
                // A permissão foi negada pelo usuário
                Log.e("Permissão Negada", "O usuário não concedeu permissão para escrever no armazenamento externo");
            }
        }
    }
    public void InserirValoresTemperatura()
    {
        refCardTemp.child("PAI").child("LastRecord").child("Temperatura").addValueEventListener(
                new ValueEventListener()
                {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        String resultado = snapshot.getValue().toString();
                        valorCardTemp.setText(resultado + " °C");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        Toast.makeText(Temperatura.this, "Erro ao carregar!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        valorGrafico01Temp.child("PAI").child("Sensor").child("Temperatura").addValueEventListener(
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
                        Toast.makeText(Temperatura.this, "Erro!", Toast.LENGTH_SHORT).show();
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
            float valor = Float.parseFloat(user.get("Temperatura").toString());
            dataValores.add(new BarEntry(index++, valor));
        }

        return dataValores;
    }

    private void atualizarGrafico(ArrayList<BarEntry> novosValores)
    {
        BarDataSet novoDataSet = new BarDataSet(novosValores, "Temperatura");
        novoDataSet.setColor(Color.rgb(5, 58, 147));

        BarData novoBarData = new BarData();
        novoBarData.addDataSet(novoDataSet);

        barChart.setData(novoBarData);
        barChart.invalidate();

        Toast.makeText(this, "Valores Atualizados!", Toast.LENGTH_SHORT).show();
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
        InserirValoresTemperatura();
        toastAtualizar(view);
    }
    public void toastAtualizar(View view)
    {
        Toast.makeText(
                getApplicationContext(), "Valores Atualizados!",Toast.LENGTH_LONG
        ).show();
    }
}