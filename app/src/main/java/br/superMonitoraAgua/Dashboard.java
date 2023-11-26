package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity
{

    private BottomNavigationView navigationView;
    private Toolbar t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        t = findViewById(R.id.toolbarSensorPh);

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
}