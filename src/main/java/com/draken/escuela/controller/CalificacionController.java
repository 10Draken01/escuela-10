package com.draken.escuela.controller;

import com.draken.escuela.dto.calificacion.CalificacionRequest;
import com.draken.escuela.dto.calificacion.CalificacionResponse;
import com.draken.escuela.services.calificacion.CalificacionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calificaciones")
@Tag(name = "Calificaciones", description = "Gestion de calificaciones de la escuela")
public class CalificacionController extends CrudController<CalificacionRequest, CalificacionResponse, CalificacionService>{
    public CalificacionController(CalificacionService service) {
        super(service);
    }
}
