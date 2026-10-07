package com.draken.escuela.controller;

import com.draken.escuela.dto.grupo.GrupoRequest;
import com.draken.escuela.dto.grupo.GrupoResponse;
import com.draken.escuela.services.grupo.GrupoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grupos")
@Tag(name = "Grupos", description = "Gestion de grupos de la escuela")
public class GrupoController extends CrudController<GrupoRequest, GrupoResponse, GrupoService>{
    public GrupoController(GrupoService service) {
        super(service);
    }
}
