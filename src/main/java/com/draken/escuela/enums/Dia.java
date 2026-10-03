package com.draken.escuela.enums;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Dia {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado");
    private final String description;
    
    public static Dia obtenerDiaPorDescriptcion(String description){
        StringCustomUtils.validarNoVacioNoNull(description, "El horario es requerido");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(description);

        for (Dia dia : values()){
            if(
                    StringCustomUtils
                            .normalizarTexto(
                                    dia.getDescription()
                            )
                            .equals(descripcionNormalizada)
            )
                return dia;
        }

        throw new DatoInvalidoException("No existe un dia con descripcion: " + description);
    }
}
