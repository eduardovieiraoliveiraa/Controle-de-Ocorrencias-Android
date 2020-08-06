package br.com.progvisual2.sdkgooglemaps.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.helper.Permissao;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;
import br.com.progvisual2.sdkgooglemaps.model.Cadastra_Usuario;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;
import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class MapsOcorrencia extends AppCompatActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera, verificarEnd, btlocalizacao;
    EditText edtlocal, edtcidade, edtdesc, edttipo;
    Button btcadastrar;
    LatLng localLatLong;
    TextView img;
    Bitmap imagem = null;
    CheckBox checaEnd;
    private FirebaseAuth auteticacao;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static  final int SELECAO_CAMERA = 100;
    StorageReference storageReference;
    private  String identificadorUsuario;
    CircleImageView imageViewOc;
    AlertDialog alertDialog;
    Uri url;
    Ocorrencia_Cad cad;
    DatabaseReference firebaseRef;
    private android.app.AlertDialog dialog;


    @Override
    public void onBackPressed() {

        Intent i = new Intent(MapsOcorrencia.this, br.com.progvisual2.sdkgooglemaps.activity.Menu.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_ocorrencia);
        inicializarFindView();

        storageReference = ConfiguracaoFirebase.getfirebasestorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);
        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Ocorrencia_Cad cad = new Ocorrencia_Cad();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, SELECAO_CAMERA);
                }else{
                    System.out.println("erro camera");
                }

            }
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validarCampos()){
                    if(checaEnd.isChecked()){
                        try {
                            edtcidade.setEnabled(true);
                            edtlocal.setEnabled(true);
                            salvar(retornaObjetosPutEndereco());
                            AfterSave();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }else if(!checaEnd.isChecked()){
                        try {
                            salvar(retornaObjetosPutLatLong());
                            AfterSave();
                            Intent intent = new Intent(MapsOcorrencia.this, Menu.class);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                }else{
                    Toast.makeText(MapsOcorrencia.this, "Preencha os campos Tipo, Descrição e Registre uma foto da Ocorrencia!", Toast.LENGTH_LONG).show();
                }
            }
        });

        verificarEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checaEnd.isChecked()){
                    DialogconfirmaDados(retornaObjetosPutEndereco());
                }else if(!checaEnd.isChecked()){
                    DialogconfirmaDados(retornaObjetosPutLatLong());
                }


            }
        });



    }

    //METODO QUE VALIDA O ACESSO A CAMERA
    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK){
            try{

                switch ( requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;

                }

                if(imagem != null){
                    imageViewOc.setImageBitmap(imagem);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    //METODO ITENT CAMERA
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoresultado : grantResults){

            if(permissaoresultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }
    }

    //ALERTA PARA USAR A PERMISSAO
    private  void alertaValidacaoPermissao(){

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    //METODO DO MAPS
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       recuperarLocalizacaoUsuario();
    }

    //METODO QUE RETORNA O OBEJTO OCORRENCIA DE ACORDO COM O ADDRESS PREENCHIDO
    public Ocorrencia_Cad retornaObjetosPutEndereco (){

        String endereco = edtlocal.getText().toString();
        String cidade = edtcidade.getText().toString();
        String txdesc = edtdesc.getText().toString();
        String txtipo = edttipo.getText().toString();
        String local = edtlocal.getText().toString()+cidade;

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);


        Ocorrencia_Cad oc = new Ocorrencia_Cad();
        Cadastra_Usuario user = UsuarioFirebase.getDadosUsuarioLogado();

        Address address = buscarEnderecoRua(local);
        oc.setDescricao(txdesc);
        oc.setTipo(txtipo);
        oc.setCidade( address.getSubAdminArea());
        oc.setEstado( address.getAdminArea());
        oc.setCep( address.getPostalCode());
        oc.setBairro( address.getSubLocality());
        oc.setRua( address.getThoroughfare());
        oc.setNumero(address.getFeatureName());
        oc.setLat( address.getLatitude());
        oc.setLongi( address.getLongitude());
        oc.setDataHora(date.toString());
        oc.setNomeCadastrador(user.getNome());

        return  oc;
    };

    //METODO QUE RETORNA O OBEJTO OCORRENCIA DE ACORDO COM A LATIUDE E LONGITUDE
    public Ocorrencia_Cad retornaObjetosPutLatLong (){

        String endereco = edtlocal.getText().toString();
        String txdesc = edtdesc.getText().toString();
        String txtipo = edttipo.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        dateFormat.format(date);

        Ocorrencia_Cad oc = new Ocorrencia_Cad();
        Cadastra_Usuario user = UsuarioFirebase.getDadosUsuarioLogado();

        Address address = buscarEnderecoLatLong(localLatLong.latitude, localLatLong.longitude);
        oc.setDescricao(txdesc);
        oc.setTipo(txtipo);
        oc.setCidade( address.getSubAdminArea());
        oc.setEstado( address.getAdminArea());
        oc.setCep( address.getPostalCode());
        oc.setBairro( address.getSubLocality());
        oc.setRua( address.getThoroughfare());
        oc.setNumero(address.getFeatureName());
        oc.setLat( address.getLatitude());
        oc.setLongi( address.getLongitude());
        oc.setDataHora(date.toString());
        oc.setNomeCadastrador(user.getNome());
        return  oc;
    };

    //METODO QUE CONFIRMA O ENDEREÇO ANTES DE SALVAR
    public void DialogconfirmaDados(Ocorrencia_Cad cad){

        StringBuilder mensagem = new StringBuilder();


        mensagem.append( "\nInformações de Endereço  \n");
        mensagem.append( "\nRua: " +    cad.getRua());
        mensagem.append( "\nNumero: " + cad.getNumero());
        mensagem.append( "\nBairro: " + cad.getBairro());
        mensagem.append( "\nCidade: " + cad.getCidade());
        mensagem.append( "\nEstado: " + cad.getEstado());
        mensagem.append( "\nCep: " + cad.getCep());

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Confirme os dados")
                .setMessage(mensagem);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //METODO PARA VALIDAR OS CAMPOS DO CADASTRO DA OCORRENCIA
    public Boolean validarCampos(){

        String endereco = edtlocal.getText().toString();
        String txdesc = edtdesc.getText().toString();
        String txtipo = edttipo.getText().toString();
        if(imagem != null) {
            if (!txdesc.isEmpty()) {
                if (!txtipo.isEmpty()) {

                    return true;

                } else {
                    Toast.makeText(this, "Informe a descrição da ocorrencia", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Informe a descrição da ocorrencia", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Por favor registre uma foto da ocorrencia", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //METODO QUE RECUPERA A LOCALIZAÇÃO DO USUARIO ATRAVES DO GPS
    private void recuperarLocalizacaoUsuario(){

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                //recupepra
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();
                localLatLong = new LatLng(latitude,longitude);

                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions()
                                .position(localLatLong)
                );

                mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(localLatLong, 6)

                );


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(

                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }

    }

    //METDO QUE BUSCA O ENDEREÇO ATRAVES DE UM GEOCODER, PASSANDO OS DADOS ELE RETORNA O ENDEREÇO CORRETO
    public Address buscarEnderecoRua(String endereco){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> listaEndereco = geocoder.getFromLocationName(endereco, 1);
            if( listaEndereco != null && listaEndereco.size() > 0){
                Address address = listaEndereco.get(0);

                return  address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //METDO QUE BUSCA O ENDEREÇO ATRAVES DE UM GEOCODER, PASSANDO LATITUDE E LONGITUDE
    public Address buscarEnderecoLatLong(Double lat, Double longi){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> listaEndereco = geocoder.getFromLocation(lat, longi, 1);
            if( listaEndereco != null && listaEndereco.size() > 0){
                Address address = listaEndereco.get(0);

                return  address;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //METODO QUE SALVA A IMAGEM NO STORAGE E A OCORRENCIA NO REALTIME
    public void salvar(final Ocorrencia_Cad ocorrencia){

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Ocorrencia")
                .setCancelable(false)
                .build();
        dialog.show();

        if(imagem != null){

            ocorrencia.set_id(UUID.randomUUID().toString());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            final StorageReference imageRef = storageReference

                    .child("Ocorrencias")
                    .child(ocorrencia.get_id()+".jpeg");

            UploadTask uploadTask = imageRef.putBytes( dadosImagem);
            imageRef.putBytes(dadosImagem).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ocorrencia.setCaminhoImagem(uri.toString());

                            firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
                            firebaseRef.child("Minhas_Ocorrencias").child(ocorrencia.get_id()).setValue(ocorrencia)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(MapsOcorrencia.this, Menu.class);
                                            startActivity(intent);
                                            Toast.makeText(MapsOcorrencia.this, "Ocorrencia salva com sucesso!", Toast.LENGTH_LONG).show();


                                        }
                                    });

                        }

                    });
                }});
        }else {
            dialog.dismiss();
            Toast.makeText(MapsOcorrencia.this, "Não foi possivel salvar!", Toast.LENGTH_LONG).show();
        }
    };




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ocorrencia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.consultar :
               Intent i = new Intent(MapsOcorrencia.this, ListaOcorrencia.class);
               startActivity(i);
               break;

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("WrongViewCast")
    private void inicializarFindView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cadastrar uma ocorrencia");
        setSupportActionBar(toolbar);

        //Configuracoes Iniciais
        auteticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        edtlocal = findViewById(R.id.local2);
        edtdesc = findViewById(R.id.descricao);
        edttipo = findViewById(R.id.tipoCard);
        btcadastrar = findViewById(R.id.CadastrarOcorrenciaMaps);
        imageViewOc = findViewById(R.id.ImageviewOc);
        imageButtonCamera = findViewById(R.id.imageButtonCamera);
        checaEnd = findViewById(R.id.putEndereco);
        edtcidade = findViewById(R.id.cidadeOc);
        verificarEnd = findViewById(R.id.btVerificarLocation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void AfterSave(){
        Intent intent = new Intent(MapsOcorrencia.this, Menu.class);
        startActivity(intent);
        Toast.makeText(MapsOcorrencia.this, "Ocorrencia salva com sucesso!", Toast.LENGTH_LONG).show();
    }

}
