package br.com.progvisual2.sdkgooglemaps.model;



import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;

public class Cadastra_Usuario implements Serializable {

    private String _id;
    private String nome;
    private String matricula;
    private String latitude;
    private String longitude;

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

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String login;

    public Cadastra_Usuario() {

    }

    //METODO PARA SALVAR O CADASTRO
    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuarios = firebaseRef.child("usuarios").child(get_id());

        usuarios.setValue(this);

    }

    private String senha;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }



    @Override
    public String toString() {
        return this.nome + "\n" + this.matricula + "\n" + this.login;
    }
}
