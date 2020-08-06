package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import br.com.progvisual2.sdkgooglemaps.R;

public class MapsCorp extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_corp);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map34);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng l = new LatLng(-24.850250,-51.824795);
        mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(l, 6)
        );

        //PEGANDO A LATITUDE E LONGITUDE DAS CORPORAÇÕES E DEPOIS BUSCANDO OS DADOS DE ENDEREÇO
        LatLng patobranco = new LatLng(-26.215629,-52.676103);
        Address PatoDados = buscarEnderecoLatLong(-26.215629,-52.676103);

        LatLng beltrao = new LatLng(-26.072324,-53.063416);
        Address beltraoDados = buscarEnderecoLatLong(-26.072324,-53.063416);

        LatLng palmas = new LatLng(-26.480647,-52.008566);
        Address palmasDados = buscarEnderecoLatLong(-26.480647,-52.008566);

        LatLng abelardoLuz = new LatLng(-26.553899,-52.317789);
        Address abelardoDados = buscarEnderecoLatLong(-26.553899,-52.317789);

        LatLng clevelandia = new LatLng(-26.410249,-52.356555);
        Address clevelandiaDados = buscarEnderecoLatLong(-26.410249,-52.356555);


        mMap.addMarker(new MarkerOptions().position(patobranco).title("Corpo de Bombeiros de Pato Branco - PR")
                .snippet(PatoDados.getThoroughfare())
                .snippet( PatoDados.getPostalCode())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );
        mMap.addMarker(new MarkerOptions().position(beltrao).title("Corpo de Bombeiros de Francisco Beltrão - PR")
                .snippet(beltraoDados.getThoroughfare())
                .snippet(beltraoDados.getPostalCode())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))

        );

        mMap.addMarker(new MarkerOptions().position(palmas).title("Corpo de Bombeiros de Palmas - PR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))
                .snippet(palmasDados.getThoroughfare())
                .snippet(palmasDados.getPostalCode())

        );

        mMap.addMarker(new MarkerOptions().position(abelardoLuz).title("Corpo de Bombeiros de Abelardo-Luz - SC")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))
                .snippet(abelardoDados.getThoroughfare())
                .snippet( abelardoDados.getPostalCode())

        );
        mMap.addMarker(new MarkerOptions().position(clevelandia).title("Corpo de Bombeiros de Clevelandia - PR")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.corporacao24px))
                .snippet(clevelandiaDados.getThoroughfare())
                .snippet(clevelandiaDados.getPostalCode())

        );
    }


    //METODO QUE BUSCA O ENDEREÇO ATRAVES DA LATITUDE E LONGITUDE
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

    @Override
    public void onBackPressed() {

        Intent i = new Intent(MapsCorp.this, br.com.progvisual2.sdkgooglemaps.activity.Menu.class);
        startActivity(i);
    }
}
