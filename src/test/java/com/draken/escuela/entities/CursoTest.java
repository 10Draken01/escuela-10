package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CursoTest {

    @Test
    void crear_debeCrearCurso_cuandoDatosSonValidos() {
        // Act
        Curso curso = Curso.crear("  Matemáticas I  ", "  Fundamentos  ", 6);

        // Assert
        assertThat(curso.getNombre()).isEqualTo("Matemáticas I");
        assertThat(curso.getDescripcion()).isEqualTo("Fundamentos");
        assertThat(curso.getCreditos()).isEqualTo(6);
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElNombreEsMuyCorto() {
        assertThatThrownBy(() -> Curso.crear("Ab", "Descripción", 6))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("entre 5 y 100 caracteres");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLosCreditosSonCero() {
        assertThatThrownBy(() -> Curso.crear("Matemáticas I", "Descripción", 0))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El credito es requerido y debe ser positivo");
    }

    @Test
    void actualizar_debeModificarDatos_cuandoTodoEsValido() {
        // Arrange
        Curso curso = Curso.builder()
                .nombre("Nombre viejo")
                .descripcion("Descripción vieja")
                .creditos(4)
                .build();

        // Act
        curso.actualizar("Historia I", "Nueva descripción", 8);

        // Assert
        assertThat(curso.getNombre()).isEqualTo("Historia I");
        assertThat(curso.getDescripcion()).isEqualTo("Nueva descripción");
        assertThat(curso.getCreditos()).isEqualTo(8);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNombreEsMuyCorto() {
        // Arrange
        Curso curso = Curso.builder()
                .nombre("Nombre válido")
                .descripcion("Descripción")
                .creditos(4)
                .build();

        // Act + Assert
        assertThatThrownBy(() -> curso.actualizar("Ab", "Descripción", 6))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("entre 5 y 100 caracteres");
    }
}
