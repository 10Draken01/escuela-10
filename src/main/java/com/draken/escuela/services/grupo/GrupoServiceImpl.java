package com.draken.escuela.services.grupo;

import com.draken.escuela.dto.grupo.GrupoRequest;
import com.draken.escuela.dto.grupo.GrupoResponse;
import com.draken.escuela.entities.Aula;
import com.draken.escuela.entities.Curso;
import com.draken.escuela.entities.Grupo;
import com.draken.escuela.entities.Maestro;
import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.exceptions.EntidadRelacionadaException;
import com.draken.escuela.mapper.GrupoMapper;
import com.draken.escuela.repositories.*;
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
public class GrupoServiceImpl implements GrupoService{
    private final GrupoRepository grupoRepository;
    private final GrupoMapper grupoMapper;

    private final CursoRepository cursoRepository;

    private final MaestroRepository maestroRepository;

    private final AulaRepository aulaRepository;

    private final InscripcionRepository inscripcionRepository;
    private final HorarioRepository horarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GrupoResponse> listar() {
        log.info("Listando grupos");
        return grupoRepository.findAll().stream()
                .map(grupoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GrupoResponse obtenerPorId(Long id) {
        return grupoMapper.entidadAResponse(obtenerGrupo(id));
    }

    @Override
    public GrupoResponse registrar(GrupoRequest request) {
        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obterMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        validarDatosUnicos(request);

        Grupo grupo = grupoMapper.requestAEntidad(
                request,
                curso,
                maestro,
                aula
        );

        grupoRepository.saveAndFlush(grupo);
        log.info("Grupo registrado con id: {}", grupo.getId());

        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public GrupoResponse actualizar(GrupoRequest request, Long id) {
        Grupo grupo = obtenerGrupo(id);

        Curso curso = obtenerCurso(request.idCurso());
        Maestro maestro = obterMaestro(request.idMaestro());
        Aula aula = obtenerAula(request.idAula());

        if (grupo.cambioEnDatos(
                curso,
                maestro,
                aula,
                request.periodo()
        )) {
            validarCambiosUnicos(request, id);
            grupo.actualizarDatos(
                    curso,
                    maestro,
                    aula,
                    request.periodo()
            );

            grupoRepository.saveAndFlush(grupo);
            log.info("Grupo actualizado con id: {}", grupo.getId());
        }
        return grupoMapper.entidadAResponse(grupo);
    }

    @Override
    public void eliminar(Long id) {
        Grupo grupo = obtenerGrupo(id);

        if(inscripcionRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo con id: " + id + " porque tiene inscripciones asociadas"
            );

        if (horarioRepository.existsByGrupoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el grupo con id: " + id + " porque tiene horarios asociados"
            );

        grupoRepository.delete(grupo);
        grupoRepository.flush();
        log.info("Grupo eliminado con id: {}", grupo.getId());
    }

    private Grupo obtenerGrupo(Long id) {
        return ServiceUtils.obtenerEntidadOException(
                grupoRepository,
                id,
                Grupo.class
        );
    }

    private void validarDatosUnicos(GrupoRequest request) {
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodo(
                request.idCurso(),
                request.idMaestro(),
                request.idAula(),
                request.periodo()
        ))
            throw new DatoInvalidoException(
                    "Ya existe un grupo con el mismo curso, maestro, aula y periodo"
            );
    }

    private void validarCambiosUnicos(GrupoRequest request, Long id) {
        if (grupoRepository.existsByCursoIdAndMaestroIdAndAulaIdAndPeriodoAndIdNot(
                request.idCurso(),
                request.idMaestro(),
                request.idAula(),
                request.periodo(),
                id
        ))
            throw new DatoInvalidoException(
                    "Ya existe un grupo con el mismo curso, maestro, aula y periodo"
            );
    }

    private Curso obtenerCurso(Long id){
        return ServiceUtils.obtenerEntidadOException(
                cursoRepository,
                id,
                Curso.class
        );
    }

    private Maestro obterMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(
                maestroRepository,
                id,
                Maestro.class
        );
    }

    private Aula obtenerAula(Long id){
        return ServiceUtils.obtenerEntidadOException(
                aulaRepository,
                id,
                Aula.class
        );
    }
}
