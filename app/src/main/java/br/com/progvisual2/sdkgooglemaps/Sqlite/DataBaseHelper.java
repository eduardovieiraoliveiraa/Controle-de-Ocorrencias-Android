package br.com.progvisual2.sdkgooglemaps.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.progvisual2.sdkgooglemaps.model.Cadastra_Usuario;

public class DataBaseHelper extends SQLiteOpenHelper {

    //VERSAO DO BANCO
    // O NUMERO DA VERSAO É PRA QUANDO VOCE ADCIONA MAIS ELEMENTOS NO BANCO E DESEJA ATUALIZAR
    //BASTA MUDAR O NUMERO DA VERSAO QUE ELE IRA ADCIONAR TD QUE VC CRIOU
    public static final int DATABASE_VERSION = 1;

    //DEFINE O NOME DO BANCO
    public static final String DATABASE_NAME = "ControleOcorrencias.db";


    //create, read, update, delete

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //CRIA A TABELA NO BANCO
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Cadastra_Usuario_Contract.SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion1) {
        //   db.execSQL(FeedContract.SQL_DELETE_ENTRIES);
        db.execSQL(Cadastra_Usuario_Contract.SQL_CREATE_ENTRIES);
    }

    public void create(Cadastra_Usuario cad) {
        // Captura a base de dados em modo escrita
        SQLiteDatabase db = getWritableDatabase();

        // Cria um novo mapa de valores, onde os nomes das colunas são as chaves
        ContentValues values = new ContentValues();
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_NOME, cad.getNome());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_MATRICULA, cad.getMatricula());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_LOGIN, cad.getLogin());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_SENHA, cad.getSenha());

        // Insere uma nova linha na base de dados, retornando a chave primária do registro
        long newRowId = db.insert(Cadastra_Usuario_Contract.TABLE_NAME, null, values);
        db.close();
    }



    public List<Cadastra_Usuario> read() {
        SQLiteDatabase db = getReadableDatabase();

// Define uma projeção com as colunas que deseja-se retornar da base de dados
        String[] projection = {
                Cadastra_Usuario_Contract._ID,
                Cadastra_Usuario_Contract.COLUMN_NAME_NOME,
                Cadastra_Usuario_Contract.COLUMN_NAME_MATRICULA,
                Cadastra_Usuario_Contract.COLUMN_NAME_LOGIN,
                Cadastra_Usuario_Contract.COLUMN_NAME_SENHA
        };


        // Ordenação do resultado
        String sortOrder =
                Cadastra_Usuario_Contract.COLUMN_NAME_NOME + " DESC";

        Cursor cursor = db.query(
                Cadastra_Usuario_Contract.TABLE_NAME,   // Tabela selecionada
                projection,             // O Array de colunas para retornar, passe null para selecionar todas
                null,              // Cláusula where
                null,          // argumentos da cláusula where
                null,
                null,
                sortOrder               // Ordenação
        );

        List<Cadastra_Usuario> feeds = new ArrayList<>();
        while (cursor.moveToNext()) {
            Cadastra_Usuario cad = new Cadastra_Usuario();
            cad.setNome(String.valueOf(cursor.getInt(cursor.getColumnIndex(Cadastra_Usuario_Contract.COLUMN_NAME_NOME))));
            cad.setMatricula(String.valueOf(cursor.getInt(cursor.getColumnIndex(Cadastra_Usuario_Contract.COLUMN_NAME_MATRICULA))));

            feeds.add(cad);
        }
        cursor.close();
        db.close();
        return feeds;
    }

    public int update(Cadastra_Usuario cad) {
        SQLiteDatabase db = getWritableDatabase();

        // Novo valor para a coluna
        ContentValues values = new ContentValues();
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_NOME, cad.getNome());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_MATRICULA, cad.getMatricula());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_LOGIN, cad.getLogin());
        values.put(Cadastra_Usuario_Contract.COLUMN_NAME_SENHA, cad.getSenha());


        // Atualização baseada no _ID
        String selection = Cadastra_Usuario_Contract._ID + " = ?";
        String[] selectionArgs = {"" + cad.get_id()};

        int count = db.update(
                Cadastra_Usuario_Contract.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        db.close();
        return count;
    }

    public void delete(int id) {
        SQLiteDatabase db = getWritableDatabase();

        // Deletar através do _ID
        String selection = Cadastra_Usuario_Contract._ID + " = ?";

        String[] selectionArgs = {"" + id};

        int deletedRows = db.delete(
                Cadastra_Usuario_Contract.TABLE_NAME,//nome da tabela
                selection,//where
                selectionArgs);//argumentos do where
        db.close();
    }

}
