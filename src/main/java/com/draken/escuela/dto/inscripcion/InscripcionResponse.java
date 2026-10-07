package com.draken.escuela.dto.inscripcion;

import com.draken.escuela.dto.datos.DatosAlumno;
import com.draken.escuela.dto.datos.DatosGrupo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos de una inscripcion de un curso")
public record InscripcionResponse(
        @Schema(description = "Identificador de la inscripcion", example = "1")
        Long id,

        @Schema(description = "Datos del alumno")
        DatosAlumno alumno,

        @Schema(description = "Datos del grupo")
        DatosGrupo grupo,

        @Schema(description = "Calificacion del curso", example = "8")
        BigDecimal calificacion,

        @Schema(description = "Fecha de inscripcion del curso", example = "11/02/2026")
        String fechaInscripcion
){ }