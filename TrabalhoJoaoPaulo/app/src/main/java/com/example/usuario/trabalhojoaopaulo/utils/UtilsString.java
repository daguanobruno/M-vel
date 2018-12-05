package com.example.usuario.trabalhojoaopaulo.utils;

public class UtilsString {

    public static boolean stringVazia(String texto){

        if (texto == null || texto.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }

    public static boolean camposIguais(String s1, String s2){

        if (s1 == s2){
            return true;
        }
        else {
            return false;
        }
    }
}
