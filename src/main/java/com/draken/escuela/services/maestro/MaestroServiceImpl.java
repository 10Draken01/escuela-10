package com.draken.escuela.services.maestro;

import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.entities.Curso;
import com.draken.escuela.entities.Maestro;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.exceptions.EntidadRelacionadaException;
import com.draken.escuela.exceptions.RecursoNoEncontradoException;
import com.draken.escuela.mapper.CursoMapper;
import com.draken.escuela.mapper.MaestroMapper;
import com.draken.escuela.repositories.CursoRepository;
import com.draken.escuela.repositories.GrupoRepository;
import com.draken.escuela.repositories.MaestroRepository;
import com.draken.escuela.utils.ServiceUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MaestroServiceImpl implements MaestroService{

    private final MaestroRepository maestroRepository;
    private final MaestroMapper maestroMapper;

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;

    private final GrupoRepository grupoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listar() {
        log.info("Listando maestros");
        return maestroRepository.findAll().stream()
                .map(maestroMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return maestroMapper.entidadAResponse(obterMaestro(id));
    }

    @Override
    public MaestroResponse registrar(MaestroRequest request) {

        Maestro maestro = maestroMapper.requestAEntidad(request);

        validarDatosUnicos(maestro.getEmail(), maestro.getTelefono());

        maestroRepository.saveAndFlush(maestro);
        log.info("Maestro agregado con id: {}", maestro.getId());
        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        Maestro maestro = obterMaestro(id);

        if(maestro.cambioEnDatos(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono()
        )) {
            validarCambiosUnicos(request.email().trim().toLowerCase(Locale.ROOT), request.telefono().trim(), id);
            maestro.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    request.email(),
                    request.telefono()
            );

            maestroRepository.saveAndFlush(maestro);

            log.info("Maestro {} actualizado con id: {}", maestro.getNombre(), maestro.getId());
        }

        return maestroMapper.entidadAResponse(maestro);
    }

    @Override
    public void eliminar(Long id) {
        Maestro maestro = obterMaestro(id);

        // Validar si el maestro tiene grupos asignados antes de eliminarlo
        if(grupoRepository.existsByMaestroId(id))
            throw new EntidadRelacionadaException("No se puede eliminar si tiene grupos asignados");

        maestroRepository.delete(maestro);
        maestroRepository.flush();

        log.info("Maestro eliminado con id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<DatosCurso> obtenerCursosDeUnMaestroConId(Long id){
        if(!maestroRepository.existsById(id))
            throw new RecursoNoEncontradoException("El maestro no existe con id: " + id);

        log.info("Listando cursos del maestro con id: {}", id);

        return cursoRepository.obtenerCursosPorIdMaestro(id).stream()
                .map(cursoMapper::entidadADatosCurso).toList();
    }

    private Maestro obterMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(
                maestroRepository,
                id,
                Maestro.class
        );
    }

    private void validarDatosUnicos(String email, String telefono){
        if(maestroRepository.existsByEmail(email))
            throw new ConflictoException("Email ya existente");

        if(maestroRepository.existsByTelefono(telefono))
            throw new ConflictoException("Telefono ya existente");
    }

    private void validarCambiosUnicos(String email, String telefono, Long id){
        if(maestroRepository.existsByEmailAndIdNot(email, id))
            throw new ConflictoException("Otro maestro ya tiene este email");

        if(maestroRepository.existsByTelefonoAndIdNot(telefono, id))
            throw new ConflictoException("Otro maestro ya tiene este telefono");
    }
}
