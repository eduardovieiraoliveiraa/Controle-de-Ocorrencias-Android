package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;
import br.com.progvisual2.sdkgooglemaps.model.Cadastra_Usuario;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class Cadastro_login extends AppCompatActivity {

    CircularProgressButton btcadastrar;
    EditText edtnome, edtmatricula, edtlogin, edtsenha, edtConfSenha;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    //recupera o objeto que permite manipular o user
    private FirebaseAuth autenticacao = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_login);
        btcadastrar = findViewById(R.id.cadastrarUsuario);
        edtnome = findViewById(R.id.nome);
        edtmatricula = findViewById(R.id.matricula);
        edtlogin = findViewById(R.id.login);
        edtsenha = findViewById(R.id.senha);
        edtConfSenha = findViewById(R.id.confsenha);

        /* OFF*/
        if(referencia == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("usuarios");
            scoresRef.keepSynced(true);
        }

        //---------------------AREA DESTINADA PARA METODOS DE BOTAO-----------------------
        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarcadastrousuario(view);

            }
        });
    }

    //METODO PARA VALIDAR OS CAMPOS DO CADASTRO
    public void validarcadastrousuario(View view){

        String txnome = edtnome.getText().toString();
        String txmatricula = edtmatricula.getText().toString();
        String txlogin = edtlogin.getText().toString();
        String txsenha = edtsenha.getText().toString();
        String txconfirmasenha= edtConfSenha.getText().toString();


        if(!txnome.isEmpty()){
            if(!txmatricula.isEmpty()){
                if(!txlogin.isEmpty()){
                    if(!txsenha.isEmpty()){
                        if(txconfirmasenha.equals(txsenha)) {

                            Cadastra_Usuario user = new Cadastra_Usuario();

                            user.setNome(txnome);
                            user.setMatricula(txmatricula);
                            user.setLogin(txlogin);
                            user.setSenha(txsenha);
                            cadastrarUsuario(user);

                        }else{
                            Toast.makeText(Cadastro_login.this,
                                    "Senha incoerentes, preencha novamente!", Toast.LENGTH_SHORT).show();
                            edtsenha.setText("");
                            edtConfSenha.setText("");
                        }
                    }else{
                        Toast.makeText(Cadastro_login.this,
                                "Preencha a senha!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Cadastro_login.this,
                            "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(Cadastro_login.this,
                        "Preencha a matricula!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(Cadastro_login.this,
                    "Preencha o nome!", Toast.LENGTH_SHORT).show();
        }

    }


    //METODO QUE SALVA OS DADOS
    public void cadastrarUsuario(final Cadastra_Usuario user){

        btcadastrar.startAnimation();
    autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    autenticacao.createUserWithEmailAndPassword(
            user.getLogin(),
            user.getSenha()
    ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {


        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {

            if (task.isSuccessful()) {

                try{
                    btcadastrar.doneLoadingAnimation(Color.parseColor("#008000"), BitmapFactory.decodeResource(getResources(), R.drawable.success));
                    AsyncTask<Void, Void, Void> demoLoad = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                Thread.sleep(500);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    demoLoad.execute();
                    String idUsuario = task.getResult().getUser().getUid();
                    user.set_id(idUsuario);

                    //METODO QUE SALVA
                    user.salvar();
                    //ATUALIZAR NOME NO FIREBASE
                    UsuarioFirebase.atualizarnomeusuario(user.getNome());
                    limpacampos();
                    redirecionaLogin();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{

                String excecao = "";

                try {
                    throw task.getException();
                }catch ( FirebaseAuthWeakPasswordException e){
                    btcadastrar.revertAnimation();
                    excecao = "Digite uma senha mais forte!";
                }catch ( FirebaseAuthInvalidCredentialsException e){
                    btcadastrar.revertAnimation();
                    excecao = "Por favor, digite um e-mail válido";
                }catch ( FirebaseAuthUserCollisionException e){
                    btcadastrar.revertAnimation();
                    excecao = "Esta conta já está cadastrada";
                }catch ( Exception e){
                    btcadastrar.revertAnimation();
                    excecao = "Erro ao cadastrar usuario: " +e.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(Cadastro_login.this,
                        excecao, Toast.LENGTH_SHORT).show();

            }
        }
    });

    }
    public void limpacampos(){
        edtnome.setText("");
        edtmatricula.setText("");
        edtlogin.setText("");
        edtConfSenha.setText("");
        edtsenha.setText("");
    }

    public void redirecionaLogin(){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }
    public void VoltaLogin(View view){

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }





}
