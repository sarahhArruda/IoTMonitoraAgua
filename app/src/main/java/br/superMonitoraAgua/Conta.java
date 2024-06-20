package br.superMonitoraAgua;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Conta extends AppCompatActivity
{

    private TextView textViewStatusLogin;
    private Toolbar t;
    private String nomeDeUsuario;
    private FirebaseAuth auth;
    private Usuario usuario;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);
        t = (Toolbar) findViewById(R.id.toolbarConta);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        auth = FirebaseAuth.getInstance();

       // textViewStatusLogin = findViewById(R.id.textViewStatusLogin);
        //updateUI();

        ImageView exitIcon = findViewById(R.id.exitIcon);
        exitIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                caixaDeDialogo(v);
            }
        });
    }

    private void caixaDeDialogo(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Sair")
                .setMessage("Deseja mesmo sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        logout(view);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .show()
        ;
    }

    public void updateUI()
    {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null)
        {
            String mensagemBoasVindas = "Bem-vindo, " + user.getEmail() + "!";
            textViewStatusLogin.setText(mensagemBoasVindas);
        }
    }

    public void logout(View view)
    {
        auth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}