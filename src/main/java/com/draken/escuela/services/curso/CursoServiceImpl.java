package com.draken.escuela.services.curso;

import com.draken.escuela.dto.curso.CursoRequest;
import com.draken.escuela.dto.curso.CursoResponse;
import com.draken.escuela.entities.Curso;
import com.draken.escuela.entities.Maestro;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.mapper.CursoMapper;
import com.draken.escuela.repositories.CursoRepository;
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
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CursoResponse> listar() {
        log.info("Listando cursos");
        return cursoRepository.findAll().stream()
                .map(cursoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CursoResponse obtenerPorId(Long id) {
        log.info("Obteniendo curso con id: {}", id);
        return cursoMapper.entidadAResponse(obtenerCurso(id));
    }

    @Override
    public CursoResponse registrar(CursoRequest request) {

        Curso curso = Curso.crear(
                request.nombre(),
                request.descripcion(),
                request.creditos()
        );

        validarDatosUnicos(curso.getNombre());

        cursoRepository.save(curso);

        log.info("Curso registrado con id: {}", curso.getId());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public CursoResponse actualizar(CursoRequest request, Long id) {

        Curso curso = obtenerCurso(id);

        Curso cursoConCambios = Curso.crear(
                request.nombre(),
                request.descripcion(),
                request.creditos()
        );

        validarCambiosUnicos(cursoConCambios.getNombre(), id);

        curso.actualizar(
                cursoConCambios.getNombre(),
                cursoConCambios.getDescripcion(),
                cursoConCambios.getCreditos()
        );

        cursoRepository.save(curso);

        log.info("Curso actualizado con id: {}", curso.getId());

        return cursoMapper.entidadAResponse(curso);
    }

    @Override
    public void eliminar(Long id) {
        Curso curso = obtenerCurso(id);

        cursoRepository.delete(curso);

        cursoRepository.flush();

        log.info("Curso eliminado con id: {}", curso.getId());
    }

    private Curso obtenerCurso(Long id){
        return ServiceUtils.obtenerEntidadOException(
                cursoRepository,
                id,
                Curso.class
        );
    }

    private void validarDatosUnicos(String nombre){
        if(cursoRepository.existsByNombre(nombre))
            throw new ConflictoException("Nombre del curso ya existente");
    }

    private void validarCambiosUnicos(String nombre, Long id){
        if(cursoRepository.existsByNombreAndIdNot(nombre, id))
            throw new ConflictoException("Ya existe un curso con el nombre: " + nombre);
    }
}
