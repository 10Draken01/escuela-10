package com.draken.escuela.controller;

import com.draken.escuela.dto.inscripcion.InscripcionRequest;
import com.draken.escuela.dto.inscripcion.InscripcionResponse;
import com.draken.escuela.services.inscripcion.InscripcionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripcion", description = "Gestion de inscripciones de la escuela")
public class InscripcionController extends CrudController<InscripcionRequest, InscripcionResponse, InscripcionService>{
    public InscripcionController(InscripcionService service) {
        super(service);
    }
}
