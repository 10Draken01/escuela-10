package com.draken.escuela.dto.datos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de una inscripcion de un curso")
public record DatosInscripcion(
        @Schema(description = "Datos del alumno", example = "Juan Pérez López")
        DatosAlumno alumno,

        @Schema(description = "Datos del grupo", example = "Matemáticas I")
        DatosGrupo grupo,

        @Schema(description = "Fecha de inscripcion del curso", example = "10/01/2025")
        String fechaInscripcion
) { }