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
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;

public class MapsBombeiros extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //VALIDAR PERMISSOES
        Permissoes.validarPermissoes(permissoes, this, 1);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng l = new LatLng(-24.850250,-51.824795);
        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(l, 6)
        );
        LatLng patobranco = new LatLng(-26.215629,-52.676103);
        Address PatoDados = buscarEnderecoLatLong(-26.215629,-52.676103);
        LatLng beltrao = new LatLng(-26.072324,-53.063416);
        LatLng palmas = new LatLng(-26.480647,-52.008566);
        LatLng abelardoLuz = new LatLng(-26.553899,-52.317789);
        LatLng clevelandia = new LatLng(-26.410249,-52.356555);
        mMap.addMarker(new MarkerOptions().position(patobranco).title("Corpo de Bombeiros de Pato Branco - PR")
                .snippet(PatoDados.getThoroughfare() + PatoDados.getPostalCode())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );
        mMap.addMarker(new MarkerOptions().position(beltrao).title("Corpo de Bombeiros de Francisco Beltrão - PR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );

        mMap.addMarker(new MarkerOptions().position(palmas).title("Corpo de Bombeiros de Palmas - PR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );

        mMap.addMarker(new MarkerOptions().position(abelardoLuz).title("Corpo de Bombeiros de Abelardo-Luz - SC")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );
        mMap.addMarker(new MarkerOptions().position(clevelandia).title("Corpo de Bombeiros de Clevelandia - PR")
                .snippet("tets")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localização","onLocationChanged: " + location.toString());

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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_satelite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menusatelite :
                Intent i = new Intent(MapsBombeiros.this, Menu.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(MapsBombeiros.this, br.com.progvisual2.sdkgooglemaps.activity.Menu.class);
        startActivity(i);
    }

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
}
