package com.draken.escuela.controller;

import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.services.maestro.MaestroService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maestros")
@Tag(name = "Maestros", description = "Gestion de maestros de la escuela")
public class MaestroController extends CrudController<MaestroRequest, MaestroResponse, MaestroService> {

    public MaestroController(MaestroService service) {
        super(service);
    }

    @GetMapping("/cursos/{id}")
    public ResponseEntity<List<DatosCurso>> obtenerCursosDeUnMaestroConId(
            @Parameter(description = "Identificador del maestro", example = "1")
            @PathVariable @Positive(message = "El identificador debe ser positivo") Long id
    ){
        return ResponseEntity.ok(service.obtenerCursosDeUnMaestroConId(id));
    }
}
