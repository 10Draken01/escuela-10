package com.draken.escuela.controller;


import com.draken.escuela.dto.curso.CursoRequest;
import com.draken.escuela.dto.curso.CursoResponse;
import com.draken.escuela.services.curso.CursoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "Cursos", description = "Gestion de cursos de la escuela")
public class CursoController extends CrudController<CursoRequest, CursoResponse, CursoService>{

    public CursoController(CursoService service) {
        super(service);
    }
}
