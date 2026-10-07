package com.draken.escuela.dto.grupo;

import com.draken.escuela.dto.datos.DatosAula;
import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.datos.DatosMaestro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Datos de un grupo")
public record GrupoResponse(
        @Schema(description = "Identificador del grupo", example = "1")
        Long id,

        @Schema(description = "Datos del curso")
        DatosCurso curso,

        @Schema(description = "Datos del maestro")
        DatosMaestro maestro,

        @Schema(description = "Datos del aula")
        DatosAula aula,

        @Schema(description = "Horarios del grupo", example = "[\"Lunes 10:00 - 12:00\", \"Miércoles 14:00 - 16:00\"]")
        List<String> horarios,

        @Schema(description = "Fecha de ingreso del alumno", example = "10/01/2025")
        String periodo
) { }