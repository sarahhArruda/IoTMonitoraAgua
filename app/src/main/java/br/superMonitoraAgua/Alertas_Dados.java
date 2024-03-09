package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Alertas_Dados extends AppCompatActivity {
    private Toolbar t;
    private ListView listaAlertas;
    private String[] alertas = {
            "Nível de água baixo.",
            "A qualidade da água está comprometida. Considere ações corretivas.",
            "Alerta de anomalia detectada nos sensores. Verifique e calibre os dispositivos.",
            "Nível de pH crítico detectado. Tome medidas imediatas para corrigir o pH da água."
    };

    // Adicione um array de imagens correspondentes aos alertas
    private int[] imagens = {R.drawable.baseline_notifications_24, R.drawable.baseline_notifications_24, R.drawable.baseline_notifications_24, R.drawable.baseline_notifications_24};

    private String[] tempos =
    {
            "há 30 minutos",
            "17:45",
            "12:25",
            "06:00"
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertas_dados);

        // Inicializar a toolbar
        t = findViewById(R.id.toolbarAlerta);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Inicializar a ListView e o adaptador
        listaAlertas = findViewById(R.id.listaAlertas);

        // Utilizar um adaptador personalizado que exibe uma imagem e um nome para cada alerta
        CustomListAdapter adapter = new CustomListAdapter(this, alertas, imagens, tempos);
        listaAlertas.setAdapter(adapter);
    }
}
