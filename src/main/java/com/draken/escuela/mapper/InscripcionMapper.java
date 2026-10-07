package com.draken.escuela.mapper;

import com.draken.escuela.dto.datos.DatosInscripcion;
import com.draken.escuela.dto.datos.DatosMaestro;
import com.draken.escuela.dto.inscripcion.InscripcionRequest;
import com.draken.escuela.dto.inscripcion.InscripcionResponse;
import com.draken.escuela.entities.Alumno;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Inscripcion;
import com.draken.escuela.utils.StringCustomUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InscripcionMapper implements CommonMapper<InscripcionRequest, InscripcionResponse, Inscripcion>{
    private final AlumnoMapper alumnoMapper;
    private final GrupoMapper grupoMapper;

    @Override
    public Inscripcion requestAEntidad(InscripcionRequest request) {
        return request == null ? null
                : Inscripcion.crear();
    }
    public Inscripcion requestAEntidad(InscripcionRequest request, Alumno alumno, Grupo grupo) {
        Inscripcion inscripcion = requestAEntidad(request);
        inscripcion.asignarAlumno(alumno);
        inscripcion.asignarGrupo(grupo);
        return inscripcion;
    }

    @Override
    public InscripcionResponse entidadAResponse(Inscripcion inscripcion) {
        return inscripcion == null ? null
                : new InscripcionResponse(
                        inscripcion.getId(),
                        alumnoMapper.entidadADatosAlumno(inscripcion.getAlumno()),
                        grupoMapper.entidadADatosGrupo(inscripcion.getGrupo()),
                        inscripcion.getCalificacion() == null ? null : inscripcion.getCalificacion().getCalificacion(),
                        StringCustomUtils.localDateAString(inscripcion.getFechaInscripcion())
        );
    }

    public DatosInscripcion entidadADatosInscripcion(Inscripcion inscripcion){
        return inscripcion == null ? null
                : new DatosInscripcion(
                alumnoMapper.entidadADatosAlumno(inscripcion.getAlumno()),
                grupoMapper.entidadADatosGrupo(inscripcion.getGrupo()),
                StringCustomUtils.localDateAString(inscripcion.getFechaInscripcion())
        );
    }
}
