package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;

public class MapsSatelite extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;
    CheckBox cbsatelite, cbstreet;
    List<Ocorrencia_Cad> ocorrencias = new ArrayList<>();
    private DatabaseReference ocorrenciasRef;
    Button normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //VALIDAR PERMISSOES
        Permissoes.validarPermissoes(permissoes, this, 1);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        normal = findViewById(R.id.btnormal);
        Ocorrencia_Cad oc = new Ocorrencia_Cad();

        ocorrenciasRef = FirebaseDatabase.getInstance().getReference().child("Minhas_Ocorrencias");

        ocorrenciasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obtect:dataSnapshot.getChildren()){
                    Ocorrencia_Cad cad = obtect.getValue(Ocorrencia_Cad.class);
                    ocorrencias.add(cad);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapsSatelite.this, MapsActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng l = new LatLng(-24.850250,-51.824795);
        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(l, 6)
        );

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Objeto responsavel por gerenciar a localização do usuario
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localização","onLocationChanged: " + location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                for (Ocorrencia_Cad cad: ocorrencias){

                    Double lat = cad.getLat();
                    Double longi = cad.getLongi();
                    LatLng localusuario = new LatLng(lat, longi);
                    mMap.addMarker(new MarkerOptions().position(localusuario).title(cad.getTipo())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_oc_24px))

                    );

                }
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

        /*
         * 1- Provedor da Localização
         * 2-Tempo minimo entre atualizações de localização(Milissegundos)
         * 3-Distancia minima entre atualizações de localização(Metrps)
         * 4-Location Listener(atraves disso recebe a atu)
         * */

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            locationManager.requestLocationUpdates(

                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }

    }

    //METODO DE PERMISSÃO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            //PERMISSAO DENIED(NEGADA)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //ALERTA
                alertavalidacaopermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //RECUPERA LOCALIZAÇÃO DO USUARIO

                /*
                 * 1- Provedor da Localização
                 * 2-Tempo minimo entre atualizações de localização(Milissegundos)
                 * 3-Distancia minima entre atualizações de localização(Metrps)
                 * 4-Location Listener(atraves disso recebe a atu)
                 * */

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                    locationManager.requestLocationUpdates(

                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener

                    );
                    return;
                }

            }
        }
    }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapsnormal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menunormal :
                Intent i = new Intent(MapsSatelite.this, MapsActivity.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(MapsSatelite.this, br.com.progvisual2.sdkgooglemaps.activity.Menu.class);
        startActivity(i);
    }
}
