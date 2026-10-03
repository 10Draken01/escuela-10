package com.draken.escuela.services;

import java.util.List;

public interface CrudService<RQ, RS>{

    List<RS> listar(RQ request);

    RS obtenerPorId(Long id);

    RS actualizar(RQ request, Long id);

    void eliminar(Long id);
}
