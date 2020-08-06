package br.com.progvisual2.sdkgooglemaps.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;

public class Ocorrencia_Cad implements Serializable {

    private String _id;
    private String tipo;
    private String descricao;
    private String rua;
    private String cidade;
    private String estado;
    private String bairro;
    private String numero;
    private String cep;
    private Double lat;
    private Double longi;
    private String CaminhoImagem;
    private Cadastra_Usuario Id_user;
    private String dataHora;
    private String NomeCadastrador;

    public Ocorrencia_Cad(String tipo, String descricao, String rua, String cidade, String estado, String caminhoImagem, String nomeCadastrador) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        CaminhoImagem = caminhoImagem;
        NomeCadastrador = nomeCadastrador;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public Ocorrencia_Cad() {

        DatabaseReference ocorrenciaRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("Minhas_Ocorrencias");
        set_id(ocorrenciaRef.push().getKey());
    }

    public String getNomeCadastrador() {
        return NomeCadastrador;
    }

    public void setNomeCadastrador(String nomeCadastrador) {
        NomeCadastrador = nomeCadastrador;
    }

    public String getCaminhoImagem() {
        return CaminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        CaminhoImagem = caminhoImagem;
    }

    public Cadastra_Usuario getId_user() {
        return Id_user;
    }

    public void setId_user(Cadastra_Usuario id_user) {
        Id_user = id_user;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLongi() {
        return longi;
    }

    public void setLongi(Double longi) {
        this.longi = longi;
    }

    public void Remover(){

        DatabaseReference ocorrenciaRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("Minhas_Ocorrencias")
                .child(get_id());
        ocorrenciaRef.removeValue();

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
