package br.com.progvisual2.sdkgooglemaps.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){

            List<String> ListaPermissoes = new ArrayList<>();

            //validar permissoes
            for(String permissao : permissoes){

                Boolean tempermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!tempermissao) ListaPermissoes.add(permissao);
                }
            if ( ListaPermissoes.isEmpty()) return true;
            String [] novaspermissoes = new String[ListaPermissoes.size()];
            ListaPermissoes.toArray(novaspermissoes);

            //Solicita permissao
            ActivityCompat.requestPermissions(activity, novaspermissoes, requestCode);
            }


        return true;
    }
}
