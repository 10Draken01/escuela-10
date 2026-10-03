package com.draken.escuela.utils;

import com.draken.escuela.exceptions.DatoInvalidoException;

public class StringCustomUtils {
    public static void validarTamanio(String texto, Integer min, Integer max, String mensaje){
        validarNoVacioNoNull(texto, mensaje);
        if ( texto.length() < min || texto.length() > max)
            throw new DatoInvalidoException(mensaje);
    }
    public static void validarNoVacioNoNull(String texto, String mensaje){
        if (texto == null || texto.isBlank())
            throw new DatoInvalidoException(mensaje);
    }
    public static String normalizarTexto(String texto){
        return texto.toLowerCase()

                .replace("á", "a")
                .replace("à", "a")
                .replace("ä", "a")
                .replace("â", "a")
                .replace("ã", "a")
                .replace("å", "a")

                .replace("é", "e")
                .replace("è", "e")
                .replace("ë", "e")
                .replace("ê", "e")

                .replace("í", "i")
                .replace("ì", "i")
                .replace("ï", "i")
                .replace("î", "i")

                .replace("ó", "o")
                .replace("ò", "o")
                .replace("ö", "o")
                .replace("ô", "o")
                .replace("õ", "o")
                .replace("ø", "o")

                .replace("ú", "u")
                .replace("ù", "u")
                .replace("ü", "u")
                .replace("û", "u")

                .replace("ñ", "n")
                .replace("ç", "c")
                .replace("ý", "y")
                .replace("ÿ", "y")

                .trim();
    }
}
