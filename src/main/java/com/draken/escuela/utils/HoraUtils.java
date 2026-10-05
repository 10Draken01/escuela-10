package com.draken.escuela.utils;

import com.draken.escuela.exceptions.DatoInvalidoException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HoraUtils {
    public static void validarHora(String hora, String mensaje) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm");

        try {
            LocalDateTime.parse(hora, formatter);
        } catch (DateTimeParseException e) {
            throw new DatoInvalidoException(mensaje);
        }
    }
}
