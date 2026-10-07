package com.draken.escuela.dto.alumno;

import com.draken.escuela.dto.datos.DatosCalificacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Datos de un alumno")
public record AlumnoResponse(
        @Schema(description = "Identificador del alumno", example = "1")
        Long id,

        @Schema(description = "Nombre del alumno", example = "Juan Pérez López")
        String nombre,

        @Schema(description = "Email del alumno", example = "juan.perez@alumnos.com")
        String email,

        @Schema(description = "Matricula del alumno", example = "A2025001")
        String matricula,

        @Schema(description = "Fecha de ingreso del alumno", example = "10/01/2025")
        String fechaIngreso,

        @Schema(description = "Calificaciones del alumno")
        List<DatosCalificacion> calificaciones,

        @Schema(description = "Promedio del curso", example = "8.0")
        BigDecimal promedio
) { }