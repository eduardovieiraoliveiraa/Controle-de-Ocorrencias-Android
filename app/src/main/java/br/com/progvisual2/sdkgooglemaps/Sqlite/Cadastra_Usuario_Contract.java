package br.com.progvisual2.sdkgooglemaps.Sqlite;

import android.provider.BaseColumns;

public class Cadastra_Usuario_Contract implements BaseColumns {

    public static final String TABLE_NAME = "Usuario";
    public static final String COLUMN_NAME_NOME = "nome";
    public static final String COLUMN_NAME_MATRICULA = "matricula";
    public static final String COLUMN_NAME_LOGIN = "login";
    public static final String COLUMN_NAME_SENHA = "senha";

    //METODO DE CRIAÇÃO DE REGISTROS
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + "( " +
                    _ID + " INTEGER PRIMARY KEY, " +
                    COLUMN_NAME_NOME + " TEXT" +
                    COLUMN_NAME_MATRICULA + " TEXT)" +
                    COLUMN_NAME_LOGIN + " TEXT)" +
                    COLUMN_NAME_SENHA + " TEXT)";

    //METODO PARA DELETAR REGISTROS
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE ID EXISTS " + TABLE_NAME;
}
