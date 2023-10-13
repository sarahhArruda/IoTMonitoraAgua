package br.superMonitoraAgua;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashScreenAppAgua extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_app_agua);

        //configuração tela de apresentação do app com o nome
        Handler handle = new Handler();
        handle.postDelayed(new Runnable()
        {@Override
            public void run()
            {
                mostrarTelaLogin();
            }
        }, 2000);
    }
    //metodo para chamar a tela de login após 2 segundos
    public void mostrarTelaLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}