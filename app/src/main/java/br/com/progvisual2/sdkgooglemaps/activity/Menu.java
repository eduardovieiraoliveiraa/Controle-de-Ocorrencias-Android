package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;
import br.com.progvisual2.sdkgooglemaps.model.Cadastra_Usuario;

public class Menu extends AppCompatActivity {

    ImageButton btocorrenciasMap, btcadastraOc, btmapa, btinfo;
    private FirebaseAuth auteticacao;
    TextView txnome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btocorrenciasMap = findViewById(R.id.ocorrenciaMap);
        btcadastraOc = findViewById(R.id.cadastraOcorrencia);
        btinfo = findViewById(R.id.info);
      //  txnome = findViewById(R.id.nomeUser);
        btmapa = findViewById(R.id.mapa);

//        usuario();


        //---------------------AREA DESTINADA PARA METODOS DE BOTAO-----------------------
        btocorrenciasMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                consultaOcorrenciasMaps(view);
            }
        });

        btcadastraOc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cadastraOcorrenciaMaps(view);

            }
        });

        btmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcessoeMaps(view);
            }
        });

        btinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                informacoes(view);
            }
        });
    }

    public void AcessoeMaps(View view){

        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    public void usuario(){

        Cadastra_Usuario cad = UsuarioFirebase.getDadosUsuarioLogado();
       String nome = cad.getNome();

       Cadastra_Usuario use = UsuarioFirebase.getDadosUsuarioLogado();
       use.getLogin();
      //  txnome.setText( use.getLogin());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
           // String uid = user.getUid();
         //   txnome.setText(name.toUpperCase());
        }


    }

    public void consultaOcorrenciasMaps(View view){
        Intent intent = new Intent(this, ListaOcorrencia.class);
        startActivity(intent);

    }

    public void cadastraOcorrenciaMaps(View view){

        Intent intentOcorrencia = new Intent(this, MapsOcorrencia.class);
        startActivity(intentOcorrencia);

    }

    public void informacoes(View view){
        Intent intentOcorrencia = new Intent(this, MapsCorp.class);
        startActivity(intentOcorrencia);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sair, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menusair :
                Intent intent = new Intent(Menu.this, Login.class);
                startActivity(intent);
                auteticacao.signOut();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        StringBuilder mensagem = new StringBuilder();


        mensagem.append( "Sua sessão será encerrada");

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Encerrando Sessão...")
                .setMessage(mensagem);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Menu.this, Login.class);
                startActivity(intent);
                auteticacao.signOut();
                finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
