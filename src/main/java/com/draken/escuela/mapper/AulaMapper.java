package com.draken.escuela.mapper;

import com.draken.escuela.dto.aula.AulaRequest;
import com.draken.escuela.dto.aula.AulaResponse;
import com.draken.escuela.entities.Aula;
import org.springframework.stereotype.Component;

@Component
public class AulaMapper implements CommonMapper<AulaRequest, AulaResponse, Aula>{
    @Override
    public Aula requestAEntidad(AulaRequest request) {
        return request == null ? null
            : Aula.crear(
                request.nombre().trim(),
                request.capacidad()
        );
    }

    @Override
    public AulaResponse entidadAResponse(Aula entidad) {
        return entidad == null ? null
                : new AulaResponse(
                        entidad.getId(),
                        entidad.getNombre(),
                        entidad.getCapacidad()
        );
    }
}
