package com.draken.escuela.services.horario;

import com.draken.escuela.dto.horario.HorarioRequest;
import com.draken.escuela.dto.horario.HorarioResponse;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Horario;
import com.draken.escuela.enums.DiaSemana;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.exceptions.EntidadRelacionadaException;
import com.draken.escuela.mapper.HorarioMapper;
import com.draken.escuela.repositories.GrupoRepository;
import com.draken.escuela.repositories.HorarioRepository;
import com.draken.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HorarioServiceImpl implements HorarioService{
    private final HorarioRepository horarioRepository;
    private final HorarioMapper horarioMapper;

    private final GrupoRepository grupoRepository;

    @Override
    public List<HorarioResponse> listar() {
        log.info("Listando horarios");
        return horarioRepository.findAll().stream()
                .map(horarioMapper::entidadAResponse).toList();
    }

    @Override
    public HorarioResponse obtenerPorId(Long id) {
        return horarioMapper.entidadAResponse(obtenerHorario(id));
    }

    @Override
    public HorarioResponse registrar(HorarioRequest request) {
        Grupo grupo = obtenerGrupo(request.idGrupo());
        DiaSemana dia = DiaSemana.obtenerDiaPorDescriptcion(request.dia());

        validarHorario(grupo, dia, request.horaInicio(), request.horaFin(), -1L);

        Horario horario = horarioMapper.requestAEntidad(request, grupo);
        horarioRepository.saveAndFlush(horario);
        log.info("Horario registrado con id: {}", horario.getId());
        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public HorarioResponse actualizar(HorarioRequest request, Long id) {
        Horario horario = obtenerHorario(id);
        Grupo grupo = obtenerGrupo(request.idGrupo());
        DiaSemana dia = DiaSemana.obtenerDiaPorDescriptcion(request.dia());

        if (horario.cambioEnDatos(request.dia(), request.horaInicio(), request.horaFin(), grupo)) {
            validarHorario(grupo, dia, request.horaInicio(), request.horaFin(), id);
            horario.actualizar(request.dia(), request.horaInicio(), request.horaFin(), grupo);
            horarioRepository.saveAndFlush(horario);
            log.info("Horario actualizado con id: {}", id);
        }
        return horarioMapper.entidadAResponse(horario);
    }

    @Override
    public void eliminar(Long id) {
        Horario horario = obtenerHorario(id);
        horarioRepository.delete(horario);
        horarioRepository.flush();
        log.info("Horario eliminado con id: {}", horario.getId());
    }

    private Horario obtenerHorario(Long id){
        return ServiceUtils.obtenerEntidadOException(
                horarioRepository,
                id,
                Horario.class
        );
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                grupoRepository,
                id,
                Grupo.class
        );
    }

    private void validarHorario(
            Grupo grupo,
            DiaSemana dia,
            String horaInicio,
            String horaFin,
            Long idExcluir
    ) {
        if (horarioRepository.existeTraslape(dia, horaInicio, horaFin,
                grupo.getPeriodo(), grupo.getId(), grupo.getAula().getId(), idExcluir))
            throw new EntidadRelacionadaException("El horario se traslapa con otro del mismo grupo o aula");
    }
}
