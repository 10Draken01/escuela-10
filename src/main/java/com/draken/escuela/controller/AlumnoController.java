package com.draken.escuela.controller;

import com.draken.escuela.dto.alumno.AlumnoRequest;
import com.draken.escuela.dto.alumno.AlumnoResponse;
import com.draken.escuela.services.alumno.AlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/alumnos")
@Tag(name = "Alumnos", description = "Gestion de alumnos de la escuela")
public class AlumnoController extends CrudController<AlumnoRequest, AlumnoResponse, AlumnoService>{
    public AlumnoController(AlumnoService service) {
        super(service);
    }
}
