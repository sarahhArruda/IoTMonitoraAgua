package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class Pontos_de_Coleta extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MAX_SNIPPET_LENGTH = 100;
    private GoogleMap gMap;
    private Toolbar t;
    private List<PontoDeColeta> pontosDeColeta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pontos_de_coleta);

        t = (Toolbar) findViewById(R.id.toolbarPontosDeColeta);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Inicialização da lista de pontos de coleta (exemplo)
        pontosDeColeta = new ArrayList<>();
        pontosDeColeta.add(new PontoDeColeta(
                "Estação - Parque Rio Negro.",
                -3.1285807995330948, -60.035260838468474,
                "Parque Rio Negro - São Raimundo.",
                "30ºC, umidade relativa: 80%, IQA: 26.",
                "mercúrio detectado acima dos limites seguros para consumo humano.",
                "a água está escura e persistente, sem cheiro perceptível."
        ));
        pontosDeColeta.add(new PontoDeColeta(
                "Estação - UFAM",
                -3.101599496000846, -59.9773994153261,
                "UFAM - Coroado/Japiim",
                "31ºC, umidade relativa: 70%, céu parcialmente nublado.",
                "Sem contaminantes.",
                "Presença ocasional de lixo flutuante."
        ));
        pontosDeColeta.add(new PontoDeColeta(
                "Estação - CEASA",
                -3.133210864441333, -59.937903418407984,
                "CEASA",
                "28ºC, umidade relativa: 85%, choveu nas últimas 24 horas.",
                "Elevada concentração de mercúrio.",
                "Água com coloração azul-esverdeada, própria para banho"
        ));
        pontosDeColeta.add(new PontoDeColeta(
                "Estação - Darcy Vargas",
                -3.0926282295216834, -60.01542615952619,
                "Darcy Vargas",
                "26ºC, umidade relativa: 90%, choveu abundantemente nas últimas 24 horas.",
                "Niveis elevados de metais pesados.",
                "Água turva devido ao aumento do volume de água."
        ));
        pontosDeColeta.add(new PontoDeColeta(
                "Estação - Porto de Manaus",
                -3.1387616488883388, -60.02626486787432,
                "Porto de Manaus",
                "29ºC, umidade relativa: 64%, céu ensolarado.",
                "Elevada carga orgânica, indicando contaminação por esgoto.",
                "Água com odor fétido, provavelmente devido ao esgoto despejado diretamente no rio."
        ));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        // Adicionar marcadores para cada ponto de coleta no mapa
        for (PontoDeColeta ponto : pontosDeColeta) {
            adicionarMarcador(ponto);
        }
    }

    private void adicionarMarcador(PontoDeColeta ponto) {
        LatLng localizacao = new LatLng(ponto.getLatitude(), ponto.getLongitude());

        // Criar um snippet com um resumo das informações
        String snippet = "Endereço: " + ponto.getEndereco() + "\n" +
                "Clique para mais detalhes";

        MarkerOptions markerOptions = new MarkerOptions()
                .position(localizacao)
                .title(ponto.getNome())
                .snippet(snippet);

        Marker marker = gMap.addMarker(markerOptions);

        // Associar o objeto ponto de coleta ao marcador usando a tag
        marker.setTag(ponto);

        // Adicionar um listener de clique ao marcador
        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Exibir uma janela de informações personalizada ao clicar no marcador
                exibirJanelaInformacoes(marker);
                return true;
            }
        });

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 10));
    }
    private void exibirJanelaInformacoes(Marker marker)
    {
        // Obter o objeto ponto de coleta associado ao marcador
        PontoDeColeta ponto = (PontoDeColeta) marker.getTag();

        // Criar um AlertDialog com um layout personalizado para exibir todas as informações do ponto de coleta
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ponto.getNome());

        // Construir o conteúdo do AlertDialog com todas as informações do ponto de coleta
        StringBuilder message = new StringBuilder();
        message.append("Endereço: ").append(ponto.getEndereco()).append("\n");
        message.append("Condições Climáticas: ").append(ponto.getCondicoesClimaticas()).append("\n");
        message.append("Contaminantes: ").append(ponto.getContaminantes()).append("\n");
        message.append("Aparência da Água: ").append(ponto.getAparenciaDaAgua()).append("\n");

        builder.setMessage(message.toString());

        // Adicionar um botão de fechar
        builder.setPositiveButton("Fechar", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        // Exibir o AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //classe para estilização dos pontos de coleta e etc
    public class PontoDeColeta
    {
        private String nome;
        private double latitude;
        private double longitude;
        private String endereco;
        private String condicoesClimaticas;
        private String contaminantes;
        private String aparenciaDaAgua;

        public String getCondicoesClimaticas() {
            return condicoesClimaticas;
        }

        public void setCondicoesClimaticas(String condicoesClimaticas) {
            this.condicoesClimaticas = condicoesClimaticas;
        }

        public String getContaminantes() {
            return contaminantes;
        }

        public void setContaminantes(String contaminantes) {
            this.contaminantes = contaminantes;
        }

        public String getAparenciaDaAgua() {
            return aparenciaDaAgua;
        }

        public void setAparenciaDaAgua(String aparenciaDaAgua) {
            this.aparenciaDaAgua = aparenciaDaAgua;
        }

        public PontoDeColeta(String nome, double latitude, double longitude, String endereco, String condicoesClimaticas, String contaminantes, String aparenciaDaAgua)
        {
            this.nome = nome;
            this.latitude = latitude;
            this.longitude = longitude;
            this.endereco = endereco;
            this.condicoesClimaticas = condicoesClimaticas;
            this.contaminantes = contaminantes;
            this.aparenciaDaAgua = aparenciaDaAgua;
        }

        public String getNome()
        {
            return nome;
        }

        public void setNome(String nome)
        {
            this.nome = nome;
        }

        public double getLatitude()
        {
            return latitude;
        }

        public void setLatitude(double latitude)
        {
            this.latitude = latitude;
        }

        public double getLongitude()
        {
            return longitude;
        }

        public void setLongitude(double longitude)
        {
            this.longitude = longitude;
        }

        public String getEndereco()
        {
            return endereco;
        }

        public void setEndereco(String endereco)
        {
            this.endereco = endereco;
        }
    }
}
