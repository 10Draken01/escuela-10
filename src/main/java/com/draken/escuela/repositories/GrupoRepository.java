package com.draken.escuela.repositories;

import com.draken.escuela.entities.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    boolean existsByMaestroId(Long idMaestro);
}
