package com.draken.escuela.mapper;

import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Maestro;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MaestroMapper implements CommonMapper<MaestroRequest, MaestroResponse, Maestro>{

    private final CursoMapper cursoMapper;

    @Override
    public Maestro requestAEntidad(MaestroRequest request) {
        return request == null ? null
            : Maestro.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        );
    }

    @Override
    public MaestroResponse entidadAResponse(Maestro entidad) {
        return entidad == null ? null
                : new MaestroResponse(
                    entidad.getId(),
                    String.join(" ",
                            entidad.getNombre(),
                            entidad.getApellidoPaterno(),
                            entidad.getApellidoMaterno()
                    ),
                    entidad.getEmail(),
                    entidad.getTelefono(),
                    null
        );
    }

    private List<DatosCurso> entidadADatosCurso(Maestro entidad){
        return entidad == null ? List.of()
                : entidad.getGrupos().stream()
                .map(Grupo::getCurso)
                .map(cursoMapper::entidadADatosCurso).toList();
    }
}
