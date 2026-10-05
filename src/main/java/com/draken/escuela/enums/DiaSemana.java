package com.draken.escuela.enums;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiaSemana {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miercoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sabado");
    private final String description;
    
    public static DiaSemana obtenerDiaPorDescriptcion(String description){
        StringCustomUtils.validarNoVacioNoNull(description, "El horario es requerido");
        String descripcionNormalizada = StringCustomUtils.normalizarTexto(description);

        for (DiaSemana diaSemana : values()){
            if(
                    StringCustomUtils
                            .normalizarTexto(
                                    diaSemana.getDescription()
                            )
                            .equals(descripcionNormalizada)
            )
                return diaSemana;
        }

        throw new DatoInvalidoException("No existe un dia con descripcion: " + description);
    }
}
