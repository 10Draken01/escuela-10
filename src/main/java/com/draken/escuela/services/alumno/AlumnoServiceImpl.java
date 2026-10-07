package com.draken.escuela.services.alumno;

import com.draken.escuela.dto.alumno.AlumnoRequest;
import com.draken.escuela.dto.alumno.AlumnoResponse;
import com.draken.escuela.entities.Alumno;
import com.draken.escuela.exceptions.EntidadRelacionadaException;
import com.draken.escuela.mapper.AlumnoMapper;
import com.draken.escuela.repositories.AlumnoRepository;
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
public class AlumnoServiceImpl implements AlumnoService{
    private final AlumnoRepository alumnoRepository;
    private final AlumnoMapper alumnoMapper;

    private final InscripcionRepository inscripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AlumnoResponse> listar() {
        log.info("Listando alumnos");
        return alumnoRepository.findAll().stream()
                .map(alumnoMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AlumnoResponse obtenerPorId(Long id) {
        return alumnoMapper.entidadAResponse(obtenerAlumno(id));
    }

    @Override
    public AlumnoResponse registrar(AlumnoRequest request) {

        Alumno alumno = alumnoMapper.requestAEntidad(
                request,
                obtenerEmail(request),
                obtenerMatricula(request)
        );

        alumnoRepository.saveAndFlush(alumno);
        log.info("Alumno registrado con id: {}", alumno.getId());
        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public AlumnoResponse actualizar(AlumnoRequest request, Long id) {
        Alumno alumno = obtenerAlumno(id);

        if(alumno.cambioEnDatos(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno()
        )) {
            alumno.actualizar(
                    request.nombre(),
                    request.apellidoPaterno(),
                    request.apellidoMaterno(),
                    obtenerEmail(request),
                    obtenerMatricula(request)
            );

            alumnoRepository.saveAndFlush(alumno);

            log.info("Alumno actualizado con id: {}", alumno.getId());
        }
        return alumnoMapper.entidadAResponse(alumno);
    }

    @Override
    public void eliminar(Long id) {
        Alumno alumno = obtenerAlumno(id);

        if (inscripcionRepository.existsByAlumnoId(id))
            throw new EntidadRelacionadaException(
                    "No se puede eliminar el alumno con id: " + id + " porque tiene inscripciones asociadas"
            );

        alumnoRepository.delete(alumno);
        alumnoRepository.flush();

        log.info("Alumno eliminado con id: {}", alumno.getId());
    }

    private Alumno obtenerAlumno(Long id){
        return ServiceUtils.obtenerEntidadOException(
                alumnoRepository,
                id,
                Alumno.class
        );
    }

    private String obtenerMatricula(AlumnoRequest request){

        log.info("Generando matricula para: {} {} {}", request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());

        String matricula = alumnoRepository.generarMatricula(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );

//        if (alumnoRepository.existsByMatricula(matricula))
//            throw new ConflictoException(
//                    "Ya existe un alumno con la matricula: " + matricula
//            );

        return matricula;
    }

    private String obtenerEmail(AlumnoRequest request){

        log.info("Generando email para: {} {} {}", request.nombre(), request.apellidoPaterno(), request.apellidoMaterno());

        String email = alumnoRepository.generarEmail(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim()
        );

//        if (alumnoRepository.existsByEmail(email))
//            throw new ConflictoException(
//                    "Ya existe un alumno con el email: " + email
//            );

        return email;
    }
}
