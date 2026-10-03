package com.draken.escuela.services.maestro;

import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.entities.Maestro;
import com.draken.escuela.repositories.maestro.MaestroRepository;
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
public class MaestroServiceImpl implements MaestroService{

    private final MaestroRepository maestroRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MaestroResponse> listar(MaestroRequest request) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public MaestroResponse obtenerPorId(Long id) {
        return null;
    }

    @Override
    public MaestroResponse actualizar(MaestroRequest request, Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {
        Maestro maestro = obterMaestro(id);

        maestroRepository.delete(maestro);
        maestroRepository.flush();

        log.info("Maestro eliminado con id: {}", id);
    }

    private Maestro obterMaestro(Long id){
        return ServiceUtils.obtenerEntidadOException(
                maestroRepository,
                id,
                Maestro.class
        );
    }
}
