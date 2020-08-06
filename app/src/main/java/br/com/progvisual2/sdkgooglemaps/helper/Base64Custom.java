package br.com.progvisual2.sdkgooglemaps.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String texto){

      return   Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)" +
                "", "");


    }

    public static String decodificarBase64(String textoCodificado){

       return new String( Base64.encodeToString(textoCodificado.getBytes(), Base64.DEFAULT));
    }

}
