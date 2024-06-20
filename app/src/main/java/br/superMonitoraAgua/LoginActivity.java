package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity
{
    private com.google.android.material.textfield.TextInputEditText campoSenha, campoEmail;
    private ProgressBar barra;
    Usuario usuario;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoEmail = findViewById(R.id.campoEmailLogin);
        campoSenha = findViewById(R.id.campoSenhaLogin);
        barra = findViewById(R.id.barraLogin);

    }

    public void validarCredenciais(View view)
    {
        String email = campoEmail.getText().toString().trim();
        String senha = campoSenha.getText().toString().trim();

        if (!email.isEmpty() && !senha.isEmpty())
        {

            barra.setVisibility(View.VISIBLE);
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    manipularResultadoLogin(task, view);

                }
            });
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void manipularResultadoLogin(Task<AuthResult> task, View view)
    {
        if (!isFinishing())
        {
            if (task.isSuccessful())
            {
                FirebaseUser usuario = auth.getCurrentUser();
                if (usuario != null)
                {
                    Log.d("LoginActivity", "Usuário logado: " + usuario.getEmail());
                    Toast.makeText(LoginActivity.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                    irDashboard(view);
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Falha ao fazer login!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Log.e("LoginActivity", "Erro no login: " + task.getException());
                Toast.makeText(LoginActivity.this, "Falha ao fazer login. Verifique suas credenciais.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void irDashboard(View view)
    {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finishAffinity();
    }

    public void irTelaCadastro(View view)
    {
        Intent intent = new Intent(this, Tela_de_Cadastro.class);
        startActivity(intent);
    }

}