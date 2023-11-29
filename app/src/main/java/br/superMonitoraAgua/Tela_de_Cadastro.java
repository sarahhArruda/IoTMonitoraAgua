package br.superMonitoraAgua;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tela_de_Cadastro extends AppCompatActivity
{

    private Toolbar t;
    private com.google.android.material.textfield.TextInputEditText campoEmail, campoSenha, campoUsuario, campoConfirmar;
    private AppCompatButton cadastrarBotao;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_de_cadastro);
        t = (Toolbar) findViewById(R.id.toolbarCadastro);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //inicialiazar componentes
        campoUsuario =  findViewById(R.id.campoUsuario);
        campoEmail =  findViewById(R.id.emailCadastro);
        campoSenha =  findViewById(R.id.Senha_Cadastro);
        campoConfirmar = findViewById(R.id.Confirmar_Senha_Cadastro);
        cadastrarBotao = findViewById(R.id.cadastrar);

        cadastrarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCadastro(view);
            }
    });
    }

    public void validarCadastro(View view)
    {
        String nomeUsuario = campoUsuario.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();
        String confirmSenha = campoConfirmar.getText().toString();

        Log.d("dados", "nomeUsuario: " + nomeUsuario);
        Log.d("dados", "email: " + email);
        Log.d("dados", "senha: " + senha);
        Log.d("dados", "confirmSenha: " + confirmSenha);

        if (!email.isEmpty()) {
            if (!senha.isEmpty())
            {
                if (senha.equals(confirmSenha))
                {
                    // Os dados estão corretos, chamar o método cadastrarUsuario()
                    Usuario usuario = new Usuario();
                    usuario.setNome(nomeUsuario);
                    usuario.setEmail(email);
                    usuario.setSenha(senha);
                    cadastrarUsuario(usuario);
                }
                if (senha.length() < 6) {
                    Toast.makeText(Tela_de_Cadastro.this, "A senha deve ter pelo menos 6 caracteres!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    // As senhas não conferem
                    Toast.makeText(Tela_de_Cadastro.this, "Senhas não conferem!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // A senha não está preenchida
                Toast.makeText(Tela_de_Cadastro.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }
        } else {
            // O email não está preenchido
            Toast.makeText(Tela_de_Cadastro.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarUsuario(Usuario usuario)
    {
        String email = usuario.getEmail();
        String senha = usuario.getSenha();

        auth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(Tela_de_Cadastro.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            Toast.makeText(Tela_de_Cadastro.this, "Falha ao cadastrar usuário!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void irDashboard(View view)
    {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }
}