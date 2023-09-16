package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void irTelaCadastro(View view)
    {
        Intent intent = new Intent(this, Tela_de_Cadastro.class);
        startActivity(intent);
    }
}