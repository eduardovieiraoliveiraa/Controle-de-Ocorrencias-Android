package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.security.spec.ECField;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;
import br.com.progvisual2.sdkgooglemaps.model.Cadastra_Usuario;
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    ImageView imageView;
    TextView txcadastro;
    EditText edtemail, edtsenha;
    CircularProgressButton btentrar;

    private FirebaseAuth autenticacao;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.animacao);
        txcadastro = findViewById(R.id.cadastro);
        edtemail = findViewById(R.id.usuario);
        edtsenha = findViewById(R.id.senha);
        btentrar = findViewById(R.id.Entrar);
        getSupportActionBar().hide();
        edtemail.setText("");
        edtsenha.setText("");

        Permissoes.validarPermissoes(permissoes, this, 1);


        //FUNÇÃO QUE FAZ A IMAGEM INTERAGIR PARA O LADO AO CLICAR
        AnimationDrawable animation2 = (AnimationDrawable) imageView.getDrawable();
        animation2.start();
        Animation deslocamento2 = new TranslateAnimation(0, 0, 0, 100);
        deslocamento2.setDuration(3000);
        imageView.startAnimation(deslocamento2);
        imageView.setElevation(200);

        //---------------------AREA DESTINADA PARA METODOS DE BOTAO-----------------------
        txcadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            ChamaCadastro(view);

            }
        });

        btentrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarLoginUsuario(view);

            }
        });


    }


    public void validarLoginUsuario(View view){

        String txemail = edtemail.getText().toString();
        String txsenha = edtsenha.getText().toString();

        if( !txemail.isEmpty() ){
            if( !txsenha.isEmpty() ){

                Cadastra_Usuario usuario = new Cadastra_Usuario();
                usuario.setLogin(txemail);
                usuario.setSenha(txsenha);
                logarUsuario(usuario);

            }else{
                Toast.makeText(Login.this,
                        "Preencha a senha!", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(Login.this,
                    "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
        }

    }

    public void logarUsuario(final Cadastra_Usuario usuario){
                btentrar.startAnimation();
                autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
                autenticacao.signInWithEmailAndPassword(
                        usuario.getLogin(), usuario.getSenha()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            btentrar.doneLoadingAnimation(Color.parseColor("#008000"), BitmapFactory.decodeResource(getResources(), R.drawable.success));
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
                            UsuarioFirebase.redirecionaUsuarioLogado(Login.this);
                        }else{
                            try{
                                String excecao = "";
                                try {
                                    throw task.getException();
                                }catch ( FirebaseAuthInvalidUserException e){
                                    btentrar.revertAnimation();
                                    excecao = "Usuário não cadastrado!";

                                }catch ( FirebaseAuthInvalidCredentialsException e){
                                    btentrar.revertAnimation();
                                    excecao = "E-mail e senha não correspondem a nenhum cadastro";
                                }catch ( Exception e){
                                    btentrar.revertAnimation();
                                    excecao = "Erro ao cadastrar usuario: " +e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(Login.this,
                                        excecao, Toast.LENGTH_SHORT).show();


                            }catch (Exception e){
                                btentrar.revertAnimation();
                                e.printStackTrace();

                            }

                        }

                    }
                });

    }

    //METODO CRIADO PARA IMAGEM INTERAGIR
    public void interage(View view){

        AnimationDrawable animation = (AnimationDrawable) imageView.getDrawable();
        animation.start();
        Animation deslocamento = new TranslateAnimation(100, 0, 0, 100);
        deslocamento.setDuration(6000);
        imageView.startAnimation(deslocamento);
    }

    public void ChamaCadastro(final View view){

        Intent intent = new Intent(this, Cadastro_login.class);
        startActivity(intent);
    }

    public void ChamaMenu(View view){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            //PERMISSAO DENIED(NEGADA)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //ALERTA
                alertavalidacaopermissao();
            }}};

    public void alertavalidacaopermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
