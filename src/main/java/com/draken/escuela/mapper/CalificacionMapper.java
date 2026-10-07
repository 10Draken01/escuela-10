package com.draken.escuela.mapper;

import com.draken.escuela.dto.calificacion.CalificacionRequest;
import com.draken.escuela.dto.calificacion.CalificacionResponse;
import com.draken.escuela.entities.Calificacion;
import com.draken.escuela.entities.Inscripcion;
import com.draken.escuela.utils.StringCustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CalificacionMapper implements CommonMapper<CalificacionRequest, CalificacionResponse, Calificacion>{
    private final InscripcionMapper inscripcionMapper;

    @Override
    public Calificacion requestAEntidad(CalificacionRequest request) {
        return request == null ? null
                : Calificacion.crear(
                request.calificacion()
        );
    }
    public Calificacion requestAEntidad(CalificacionRequest request, Inscripcion inscripcion) {
        Calificacion calificacion = requestAEntidad(request);
        calificacion.asignarInscripcion(inscripcion);
        return calificacion;
    }

    @Override
    public CalificacionResponse entidadAResponse(Calificacion calificacion) {
        return calificacion == null ? null
                : new CalificacionResponse(
                calificacion.getId(),
                inscripcionMapper.entidadADatosInscripcion(calificacion.getInscripcion()),
                calificacion.getCalificacion(),
                StringCustomUtils.localDateAString(
                        calificacion.getInscripcion().getFechaInscripcion()
                )
        );
    }
}
