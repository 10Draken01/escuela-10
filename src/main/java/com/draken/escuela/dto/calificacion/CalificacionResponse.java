package com.draken.escuela.dto.calificacion;

import com.draken.escuela.dto.datos.DatosInscripcion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos de la calificacion de un alumno")
public record CalificacionResponse(
        @Schema(description = "Identificador de la calificacion", example = "1")
        Long id,

        @Schema(description = "Datos de la inscripcion del curso")
        DatosInscripcion inscripcion,

        @Schema(description = "Calificacion del curso", example = "8")
        BigDecimal calificacion,

        @Schema(description = "Fecha de registro del curso", example = "11/02/2026")
        String fechaRegistro
) { }