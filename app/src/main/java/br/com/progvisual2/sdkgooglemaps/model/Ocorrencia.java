package br.com.progvisual2.sdkgooglemaps.model;

import com.google.firebase.database.DatabaseReference;

import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;

public class Ocorrencia {

    private String id;
    private String latitude;
    private String longitude;
    private Cadastra_Usuario Id_user;
    private  Ocorrencia_Cad Id_oc;

    public Ocorrencia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Cadastra_Usuario getId_user() {
        return Id_user;
    }

    public void setId_user(Cadastra_Usuario id_user) {
        Id_user = id_user;
    }

    public Ocorrencia_Cad getId_oc() {
        return Id_oc;
    }

    public void setId_oc(Ocorrencia_Cad id_oc) {
        Id_oc = id_oc;
    }



}
