package com.draken.escuela.mapper;

import com.draken.escuela.dto.datos.DatosGrupo;
import com.draken.escuela.dto.grupo.GrupoRequest;
import com.draken.escuela.dto.grupo.GrupoResponse;
import com.draken.escuela.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrupoMapper implements CommonMapper<GrupoRequest, GrupoResponse, Grupo>{
    private final CursoMapper cursoMapper;
    private final MaestroMapper maestroMapper;
    private final AulaMapper aulaMapper;

    @Override
    public Grupo requestAEntidad(GrupoRequest request) {
        return request == null ? null
                : Grupo.crear(
                request.periodo()
        );
    }

    public Grupo requestAEntidad(GrupoRequest request, Curso curso, Maestro maestro, Aula aula) {
        Grupo grupo = requestAEntidad(request);

        grupo.asignarCurso(curso);
        grupo.asignarMaestro(maestro);
        grupo.asignarAula(aula);

        return grupo;
    }

    @Override
    public GrupoResponse entidadAResponse(Grupo grupo) {
        return grupo == null ? null
                : new GrupoResponse(
                grupo.getId(),
                cursoMapper.entidadADatosCurso(grupo.getCurso()),
                maestroMapper.entidadADatosMaestro(grupo.getMaestro()),
                aulaMapper.entidadADatosAula(grupo.getAula()),
                grupo.getHorarios().stream()
                        .map(Horario::obtenerHorarioCompleto)
                        .toList(),
                grupo.getPeriodo()
        );
    }

    public DatosGrupo entidadADatosGrupo(Grupo grupo){
        return grupo == null ? null
                : new DatosGrupo(
                grupo.getCurso().getNombre(),
                grupo.getMaestro().obtenerNombreCompleto(),
                grupo.getAula().getNombre(),
                grupo.getPeriodo()
        );
    }
}
