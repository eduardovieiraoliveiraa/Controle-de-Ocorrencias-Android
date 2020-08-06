package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;

public class AbreCardView extends AppCompatActivity {

    EditText edRua, edCidade, edEstado, edTipo, edDesc, edLongitude, edLatitude, edNumero, edAtendente, eddata;
    ImageView foto;
    private Ocorrencia_Cad ocorrenciaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abre_card_view);
        inicializarComponentes();

        //RECEBENDO OS DAODS DA INTENT PASSADA NA LISTACTIVIT
        ocorrenciaSelecionada = (Ocorrencia_Cad) getIntent().getSerializableExtra("ocorrenciaSelecionada");
        if(ocorrenciaSelecionada != null){

            edTipo.setText( ocorrenciaSelecionada.getTipo());
            edDesc.setText( ocorrenciaSelecionada.getDescricao());
            edRua.setText( ocorrenciaSelecionada.getRua());
            edCidade.setText( ocorrenciaSelecionada.getCidade());
            edEstado.setText( ocorrenciaSelecionada.getEstado());
            edNumero.setText( ocorrenciaSelecionada.getNumero());
            edLatitude.setText("Latitude: " + ocorrenciaSelecionada.getLat().toString());
            edLongitude.setText("Longitude: " +ocorrenciaSelecionada.getLongi().toString());
            edAtendente.setText( ocorrenciaSelecionada.getNomeCadastrador());
            eddata.setText( ocorrenciaSelecionada.getDataHora());
            Picasso.with(this).load(ocorrenciaSelecionada.getCaminhoImagem()).into(foto);

        }
    }

    public void inicializarComponentes(){

        edTipo = findViewById(R.id.tipoCard);
        edDesc = findViewById(R.id.descCard);
        edRua = findViewById(R.id.ruaCard);
        edCidade = findViewById(R.id.cidadeCard);
        edEstado = findViewById(R.id.estadoCard);
        edNumero = findViewById(R.id.numeroCard);
        edLatitude = findViewById(R.id.latitudeCard);
        edLongitude = findViewById(R.id.longitudeCard);
        edAtendente = findViewById(R.id.atendenteCard);
        eddata = findViewById(R.id.dataCard);
        foto = findViewById(R.id.fotoCard);

        edTipo.setEnabled(false);
        edDesc.setEnabled(false);
        edRua.setEnabled(false);
        edCidade.setEnabled(false);
        edEstado.setEnabled(false);
        edNumero.setEnabled(false);
        edLatitude.setEnabled(false);
        edLongitude.setEnabled(false);
        edAtendente.setEnabled(false);
        eddata.setEnabled(false);

    }
}
