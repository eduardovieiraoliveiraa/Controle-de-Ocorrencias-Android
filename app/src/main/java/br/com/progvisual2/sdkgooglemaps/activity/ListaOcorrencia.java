package br.com.progvisual2.sdkgooglemaps.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import br.com.progvisual2.sdkgooglemaps.R;
import br.com.progvisual2.sdkgooglemaps.adapter.AdapterOcorrencias;
import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;
import br.com.progvisual2.sdkgooglemaps.config.RecyclerItemClickListener;
import br.com.progvisual2.sdkgooglemaps.helper.UsuarioFirebase;
import br.com.progvisual2.sdkgooglemaps.model.Ocorrencia_Cad;

public class ListaOcorrencia extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<Ocorrencia_Cad> ocorrencias = new ArrayList<>();
    List<Ocorrencia_Cad> tiposOc;
    private DatabaseReference ocorrenciasRef;
    private String filtroTipo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ocorrencia);
        Ocorrencia_Cad oc = new Ocorrencia_Cad();
        ocorrenciasRef = FirebaseDatabase.getInstance().getReference().child("Minhas_Ocorrencias");
        ocorrenciasRef.keepSynced(true);
        recyclerView= findViewById(R.id.recyclerOcorrencias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //METODO DE CLICK NO CARDVIEW
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if(ocorrencias != null){

                            Ocorrencia_Cad ocorrenciaSelecionada = ocorrencias.get(position);

                            //PASSANDO OS DADOS DO CARDVIEW PARA TELA ABRE CARDVIEW
                            Intent intent = new Intent(ListaOcorrencia.this, AbreCardView.class);
                            intent.putExtra("ocorrenciaSelecionada", ocorrenciaSelecionada);
                            startActivity(intent);

                        }else{

                            Toast.makeText(ListaOcorrencia.this, "Não foi possivel abrir a ocorrencia", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    }
                })
        );


    }


    public void Filtrar(View view){

        final AlertDialog.Builder dialogFiltro = new AlertDialog.Builder(this);
        dialogFiltro.setTitle("Pesquise pelo nome da ocorrencia");

        View viewSpinner = getLayoutInflater().inflate(R.layout.diolog_spinner, null);
        final EditText edtTipo = viewSpinner.findViewById(R.id.edFiltroTipo);

        dialogFiltro.setView(viewSpinner);
        dialogFiltro.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            filtroTipo = edtTipo.getText().toString();
            recuperarOcorrencias();

            }
        });

        dialogFiltro.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = dialogFiltro.create();
        dialog.show();
    }

    public void recuperarOcorrencias(){

        ocorrenciasRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("Minhas_Ocorrencias")
                .child(filtroTipo);

        ocorrenciasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ocorrencias.clear();
                for (DataSnapshot testes: dataSnapshot.getChildren()){
                    for(DataSnapshot tipos: testes.getChildren()){

                        Ocorrencia_Cad ocorrencia = testes.getValue(Ocorrencia_Cad.class);
                        ocorrencias.add( ocorrencia);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Collections.reverse(ocorrencias);


    }


    //METODO PARA CARREGAR O RECYCLERVIEW COM AS OCORRENCIAS CADASTRADAS
    @Override
    protected void onStart() {
        super.onStart();
    FirebaseRecyclerAdapter<Ocorrencia_Cad,ListViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Ocorrencia_Cad, ListViewHolder>
            (Ocorrencia_Cad.class,R.layout.adapter_ocorrencia,ListViewHolder.class,ocorrenciasRef) {
        @Override
        protected void populateViewHolder(ListViewHolder listViewHolder, Ocorrencia_Cad ocorrencia_cad, int i) {

            listViewHolder.setTipo(ocorrencia_cad.getTipo());
            listViewHolder.setRua(ocorrencia_cad.getRua());
            listViewHolder.setCidade(ocorrencia_cad.getCidade());
            listViewHolder.setEstado(ocorrencia_cad.getEstado());
            listViewHolder.setDesc(ocorrencia_cad.getDataHora());
            listViewHolder.setAtendente(ocorrencia_cad.getNomeCadastrador());
            listViewHolder.setImage(getApplicationContext(),ocorrencia_cad.getCaminhoImagem());

            ocorrenciasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot obtect: dataSnapshot.getChildren()){
                        Ocorrencia_Cad cad = obtect.getValue(Ocorrencia_Cad.class);
                        ocorrencias.add(cad);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    };

      recyclerView.setAdapter(firebaseRecyclerAdapter);

}

    //VIEWHOLDER QUE PEGA OS OBJETOS ATRAVES DO CONSTRUTOR CRIADO NA CLASSE
    public static class ListViewHolder extends RecyclerView.ViewHolder{

        View view;
        public ListViewHolder(View item){
            super(item);

            view=item;
        }

        public void setTipo(String txtipo){

            TextView post_tipo=(TextView)view.findViewById(R.id.txTipo);
            post_tipo.setText(txtipo);

        }

        public void setRua(String txrua){

            TextView post_tipo=(TextView)view.findViewById(R.id.txRua);
            post_tipo.setText(txrua);

        }

        public void setCidade(String txCidade){

            TextView post_cidade=(TextView)view.findViewById(R.id.txCidade);
            post_cidade.setText(txCidade);

        }

        public void setEstado(String txEstado){

            TextView post_estado=(TextView)view.findViewById(R.id.txEstado);
            post_estado.setText(txEstado);

        }

        public void setDesc(String txDesc){

            TextView post_desc=(TextView)view.findViewById(R.id.txDesc);
            post_desc.setText(txDesc);

        }

        public void setAtendente(String txAtendente){

            TextView post_atend=(TextView)view.findViewById(R.id.txAtendente);
            post_atend.setText(txAtendente);

        }


        public void setImage(Context ctx, String image){

            ImageView post_Image= (ImageView)view.findViewById(R.id.imageOcorrencia);
            Picasso.with(ctx).load(image).into(post_Image);

        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ListaOcorrencia.this, Menu.class);
        startActivity(i);
    }
}
