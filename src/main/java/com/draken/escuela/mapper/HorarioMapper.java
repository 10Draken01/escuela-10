package com.draken.escuela.mapper;

import com.draken.escuela.dto.horario.HorarioRequest;
import com.draken.escuela.dto.horario.HorarioResponse;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Horario;
import com.draken.escuela.enums.DiaSemana;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HorarioMapper implements CommonMapper<HorarioRequest, HorarioResponse, Horario> {
    private final GrupoMapper grupoMapper;
    @Override
    public Horario requestAEntidad(HorarioRequest request) {
        return request == null ? null
                : Horario.crear(
                        request.horaInicio(),
                        request.horaFin()
        );
    }

    public Horario requestAEntidad(HorarioRequest request, DiaSemana diaSemana, Grupo grupo) {
        Horario horario = requestAEntidad(request);
        horario.asignarGrupo(grupo);
        horario.asignarDiaSemana(diaSemana);
        return horario;
    }

    @Override
    public HorarioResponse entidadAResponse(Horario horario) {
        return horario == null ? null
                : new HorarioResponse(
                        horario.getId(),
                        grupoMapper.entidadADatosGrupo(horario.getGrupo()),
                        horario.obtenerHorarioCompleto()
        );
    }
}
