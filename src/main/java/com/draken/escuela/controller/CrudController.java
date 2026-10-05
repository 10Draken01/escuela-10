package com.draken.escuela.controller;

import com.draken.escuela.docs.ProblemaDoc;
import com.draken.escuela.services.CrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@ApiResponse(
        responseCode = "400",
        description = "Solicitud incorrecta",
        content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(
                        implementation = ProblemaDoc.class
                )
        )
)
@ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(
                mediaType = "application/problem+json",
                schema = @Schema(
                        implementation = ProblemaDoc.class
                )
        )
)
public class CrudController<RQ, RS, S extends CrudService<RQ, RS>>{
    protected final S service;

    @GetMapping
    @Operation(summary = "Listar todos los registros")
    @ApiResponse(
            responseCode = "200",
            description = "Lista de registros"
    )
    public ResponseEntity<List<RS>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un registro por su identificador")
    @ApiResponse(
            responseCode = "200",
            description = "Registro encontrado"
    )
    public ResponseEntity<RS> obtenerPorId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo registro")
    @ApiResponse(
            responseCode = "201",
            description = "Registro creado"
    )
    public ResponseEntity<RS> registrar(
            @Valid RQ request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registrar(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro por su identificador")
    @ApiResponse(
            responseCode = "200",
            description = "Registro actualizado"
    )
    public ResponseEntity<RS> actualizar(
            @PathVariable Long id,
            @Valid RQ request
    ) {
        return ResponseEntity.ok(service.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro por su identificador")
    @ApiResponse(
            responseCode = "204",
            description = "Registro eliminado"
    )
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id
    ) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
