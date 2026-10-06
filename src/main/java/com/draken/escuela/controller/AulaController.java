package com.draken.escuela.controller;

import com.draken.escuela.dto.aula.AulaRequest;
import com.draken.escuela.dto.aula.AulaResponse;
import com.draken.escuela.services.aula.AulaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/aulas")
@Tag(name = "Aulas", description = "Gestion de aulas de la escuela")
public class AulaController extends CrudController<AulaRequest, AulaResponse, AulaService>{
    public AulaController(AulaService service) {
        super(service);
    }
}
