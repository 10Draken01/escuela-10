package com.draken.escuela.mapper;

import com.draken.escuela.dto.alumno.AlumnoRequest;
import com.draken.escuela.dto.alumno.AlumnoResponse;
import com.draken.escuela.dto.datos.DatosAlumno;
import com.draken.escuela.dto.datos.DatosCalificacion;
import com.draken.escuela.dto.datos.DatosGrupo;
import com.draken.escuela.entities.Alumno;
import com.draken.escuela.entities.Calificacion;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Inscripcion;
import com.draken.escuela.utils.StringCustomUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class AlumnoMapper implements CommonMapper<AlumnoRequest, AlumnoResponse, Alumno> {

    @Override
    public Alumno requestAEntidad(AlumnoRequest request) {
        return request == null ? null
                : Alumno.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno()
        );
    }

    public Alumno requestAEntidad(AlumnoRequest request, String email, String matricula) {
        if(request == null) return null;

        Alumno alumno = requestAEntidad(request);

        alumno.asignarDatosAcademicos(email, matricula);

        return alumno;
    }

    @Override
    public AlumnoResponse entidadAResponse(Alumno alumno) {
        if (alumno == null) return null;

        return new AlumnoResponse(
                alumno.getId(),
                String.join(" ",
                        alumno.getNombre(),
                        alumno.getApellidoPaterno(),
                        alumno.getApellidoMaterno()
                ),
                alumno.getEmail(),
                alumno.getMatricula(),
                StringCustomUtils.localDateAString(alumno.getFechaIngreso()),
                entidadADatosCalificacion(alumno),
                alumno.calcularPromedio()
        );
    }

    public List<DatosCalificacion> entidadADatosCalificacion(Alumno alumno) {
        if (alumno == null) return List.of();

        return alumno.getInscripciones().stream()
                .map(inscripcion -> new DatosCalificacion(
                        inscripcion.getGrupo().getCurso().getNombre(),
                        inscripcion.getGrupo().getPeriodo(),
                        inscripcion.getCalificacion() != null ?
                                inscripcion.getCalificacion().getCalificacion()
                                : null
                ))
                .toList();
    }

    public DatosAlumno entidadADatosAlumno(Alumno alumno){
        return alumno == null ? null
                : new DatosAlumno(
                        alumno.obtenerNombreCompleto(),
                        alumno.getMatricula(),
                        alumno.getEmail(),
                        StringCustomUtils.localDateAString(alumno.getFechaIngreso())
        );
    }
}