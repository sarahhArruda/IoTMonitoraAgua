package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

public class Tela_de_Cadastro extends AppCompatActivity
{

    private Toolbar t;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_cadastro);
        t = (Toolbar) findViewById(R.id.toolbarCadastro);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }
    public void irDashboard(View view)
    {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}