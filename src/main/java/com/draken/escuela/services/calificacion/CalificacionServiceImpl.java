package com.draken.escuela.services.calificacion;

import com.draken.escuela.dto.calificacion.CalificacionRequest;
import com.draken.escuela.dto.calificacion.CalificacionResponse;
import com.draken.escuela.entities.Calificacion;
import com.draken.escuela.entities.Inscripcion;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.mapper.CalificacionMapper;
import com.draken.escuela.mapper.InscripcionMapper;
import com.draken.escuela.repositories.CalificacionRepository;
import com.draken.escuela.repositories.InscripcionRepository;
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
public class CalificacionServiceImpl implements CalificacionService{
    private final CalificacionRepository calificacionRepository;
    private final CalificacionMapper calificacionMapper;

    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listar() {
        log.info("Listando calificaciones");
        return calificacionRepository.findAll().stream()
                .map(calificacionMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CalificacionResponse obtenerPorId(Long id) {
        return calificacionMapper.entidadAResponse(obtenerCalificacion(id));
    }

    @Override
    public CalificacionResponse registrar(CalificacionRequest request) {

        Inscripcion inscripcion = obtenerInscripcion(request.idInscripcion());

        if(calificacionRepository.existsByInscripcionId(request.idInscripcion()))
            throw new ConflictoException("La inscripción ya tiene una calificación registrada");

        Calificacion calificacion = calificacionMapper.requestAEntidad(request, inscripcion);

        calificacionRepository.saveAndFlush(calificacion);
        log.info("Calificación registrada con éxito: {}", calificacion.getId());
        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public CalificacionResponse actualizar(CalificacionRequest request, Long id) {

        Calificacion calificacion = obtenerCalificacion(id);

        calificacion.actualizar(
                request.calificacion()
        );

        calificacionRepository.saveAndFlush(calificacion);
        log.info("Calificación actualizada con éxito: {}", calificacion.getId());
        return calificacionMapper.entidadAResponse(calificacion);
    }

    @Override
    public void eliminar(Long id) {

        Calificacion calificacion = obtenerCalificacion(id);

        calificacionRepository.delete(calificacion);
        calificacionRepository.flush();
        log.info("Calificación eliminada con éxito: {}", calificacion.getId());

    }

    private Calificacion obtenerCalificacion(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                calificacionRepository,
                id,
                Calificacion.class
        );
    }

    private Inscripcion obtenerInscripcion(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                inscripcionRepository,
                id,
                Inscripcion.class
        );
    }
}
