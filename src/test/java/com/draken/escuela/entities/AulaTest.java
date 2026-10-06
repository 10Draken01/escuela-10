package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AulaTest {

    @Test
    void crear_debeCrearAula_cuandoDatosSonValidos() {
        // Act
        Aula aula = Aula.crear("  Aula 101  ", 30);

        // Assert
        assertThat(aula.getNombre()).isEqualTo("Aula 101");
        assertThat(aula.getCapacidad()).isEqualTo(30);
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElNombreEsMuyCorto() {
        // Act + Assert: "Ab" tiene menos de 5 caracteres
        assertThatThrownBy(() -> Aula.crear("Ab", 30))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("entre 5 y 30 caracteres");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaCapacidadEsNegativa() {
        assertThatThrownBy(() -> Aula.crear("Aula 101", -5))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La capacidad es requerida y debe ser positiva");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoLaCapacidadEsNula() {
        assertThatThrownBy(() -> Aula.crear("Aula 101", null))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("La capacidad es requerida y debe ser positiva");
    }

    @Test
    void actualizar_debeModificarDatos_cuandoTodoEsValido() {
        // Arrange
        Aula aula = Aula.builder()
                .nombre("Aula vieja")
                .capacidad(10)
                .build();

        // Act
        aula.actualizar("Aula Nueva", 45);

        // Assert
        assertThat(aula.getNombre()).isEqualTo("Aula Nueva");
        assertThat(aula.getCapacidad()).isEqualTo(45);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNombreEsMuyCorto() {
        // Arrange
        Aula aula = Aula.builder()
                .nombre("Aula válida")
                .capacidad(10)
                .build();

        // Act + Assert
        assertThatThrownBy(() -> aula.actualizar("Ab", 20))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("entre 5 y 30 caracteres");
    }

    @Test
    void asignarGrupo_debeLanzarExcepcion_cuandoElGrupoEsNulo() {
        // Arrange
        Aula aula = Aula.crear("Aula 101", 30);

        // Act + Assert
        assertThatThrownBy(() -> aula.asignarGrupo(null))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El grupo es requerido");
    }
}
