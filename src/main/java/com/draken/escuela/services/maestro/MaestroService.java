package com.draken.escuela.services.maestro;

import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.services.CrudService;

import java.util.List;

public interface MaestroService extends CrudService<MaestroRequest, MaestroResponse> {
    List<DatosCurso> obtenerCursosDeUnMaestroConId(Long id);
}
