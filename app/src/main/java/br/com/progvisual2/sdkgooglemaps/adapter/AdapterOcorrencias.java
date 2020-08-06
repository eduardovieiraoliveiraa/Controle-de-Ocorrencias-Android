package br.com.progvisual2.sdkgooglemaps.adapter;

import android.content.Context;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;

public class AdapterOcorrencias extends RecyclerView.Adapter<AdapterOcorrencias.MyviewHolder> {

    private List<Ocorrencia_Cad> ocorrencias;
    private Context contex;

   public AdapterOcorrencias(ArrayList<Ocorrencia_Cad> ocorrencias) {
        this.ocorrencias = ocorrencias;

    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ocorrencia, parent, false);
        return new MyviewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        holder.Tipo.setText("TITULO DE TESTE");
        holder.Rua.setText("teste 01");


      //  List<String> urlFotos = new ArrayList<>();
      //  urlFotos.add(ocorrencia.getCaminhoImagem());
       // String urlCapa = urlFotos.get(0);

       // Picasso.get().load(urlCapa).into(holder.foto);

    }

    @Override
    public int getItemCount() {
        return ocorrencias.size();
    }

    class  MyviewHolder extends RecyclerView.ViewHolder{

        private TextView Tipo;
        private TextView Rua;
        private ImageView foto;

        MyviewHolder(View itemview){

            super(itemview);

            Tipo = itemview.findViewById(R.id.txTipo);
            Rua = itemview.findViewById(R.id.txRua);
            foto = itemview.findViewById(R.id.imageOcorrencia);



        }
    }



}
