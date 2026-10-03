package com.draken.escuela.dto.maestro;

import com.draken.escuela.dto.datos.DatosCurso;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Datos para registrar un maestro")
public record MaestroResponse(
    @Schema(description = "Identificador del maestro", example = "1")
    Long id,

    @Schema(description = "Nombre del maestro", example = "Laura")
    String nombre,

    @Schema(description = "Email del maestro", example = "laura.martinez@escuela.com")
    String email,

    @Schema(description = "Numero telefonico del maestro", example = "5551010789")
    String telefono,

    @Schema(description = "Datos de los cursos del maestro")
    List<DatosCurso> cursos
) { }