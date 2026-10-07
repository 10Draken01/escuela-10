package com.draken.escuela.controller;

import com.draken.escuela.dto.horario.HorarioRequest;
import com.draken.escuela.dto.horario.HorarioResponse;
import com.draken.escuela.services.horario.HorarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horarios", description = "Gestion de horarios de la escuela")
public class HorarioController extends CrudController<HorarioRequest, HorarioResponse, HorarioService>{
    public HorarioController(HorarioService service) {
        super(service);
    }
}
