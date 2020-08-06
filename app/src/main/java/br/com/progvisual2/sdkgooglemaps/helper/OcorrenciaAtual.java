package br.com.progvisual2.sdkgooglemaps.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.progvisual2.sdkgooglemaps.config.ConfiguracaoFirebase;

public class OcorrenciaAtual {

    public static FirebaseUser getOcoreenciaAtual(){
        FirebaseAuth usuario  = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

}
