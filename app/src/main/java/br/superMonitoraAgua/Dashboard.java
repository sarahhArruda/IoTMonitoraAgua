package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.superMonitoraAgua.fragment.HomeFragment;
import br.superMonitoraAgua.fragment.SensoresFragment;

public class Dashboard extends AppCompatActivity
{

    private BottomNavigationView navigationView;
    private Toolbar t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        t = findViewById(R.id.toolbarDashboard);

        navigationView = (BottomNavigationView) findViewById(R.id.navegacaoDash);
        navigationView.setOnNavigationItemSelectedListener
                (
                item ->
                {
                    switch (item.getItemId())
                    {
                        case (R.id.Home):
                            startActivity(new Intent(this, Dashboard.class));
                            finish();
                            return true;

                        case (R.id.dadosItemMenu):
                            startActivity(new Intent(this, Dados.class));
                            finish();
                            return true;
                        case (R.id.pontosItemMenu):
                            startActivity(new Intent(this, Pontos_de_Coleta.class));
                            finish();
                            return true;

                        case (R.id.usuarioItemMenu):
                            startActivity(new Intent(this, Conta.class));
                            finish();
                            return true;
                        default:
                            return false;
                    }
                }
        );
    }
}