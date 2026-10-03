package com.draken.escuela.mapper;

import com.draken.escuela.dto.curso.CursoRequest;
import com.draken.escuela.dto.curso.CursoResponse;
import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.entities.Curso;
import org.springframework.stereotype.Component;

@Component
public class CursoMapper implements CommonMapper<CursoRequest, CursoResponse, Curso>{
    @Override
    public Curso requestAEntidad(CursoRequest request) {
        return request == null ? null
                : Curso.crear(
                "",
                "",
                1
        );
    }

    @Override
    public CursoResponse entidadAResponse(Curso entidad) {
        return null;
    }

    public DatosCurso entidadADatosCurso(Curso curso){
        return curso == null ? null
                : new DatosCurso(
                    curso.getNombre(),
                    curso.getDescripcion(),
                    curso.getCreditos()
        );
    }
}
