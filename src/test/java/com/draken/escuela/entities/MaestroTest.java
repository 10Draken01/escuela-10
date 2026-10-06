package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaestroTest {

    @Test
    void crear_debeCrearMaestro_cuandoDatosSonValidos() {
        // Act
        Maestro maestro = Maestro.crear(
                "Laura", "Martínez", "López", "  LAURA@ESCUELA.COM  ", "5551010789");

        // Assert: el email se normaliza a minúsculas y sin espacios
        assertThat(maestro.getNombre()).isEqualTo("Laura");
        assertThat(maestro.getApellidoPaterno()).isEqualTo("Martínez");
        assertThat(maestro.getEmail()).isEqualTo("laura@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("5551010789");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElNombreEsMuyCorto() {
        assertThatThrownBy(() -> Maestro.crear(
                "Lau", "Martínez", "López", "laura@escuela.com", "5551010789"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("El nombre es requerido");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElTelefonoNoTiene10Digitos() {
        assertThatThrownBy(() -> Maestro.crear(
                "Laura", "Martínez", "López", "laura@escuela.com", "555101"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("exactamente 10 caracteres");
    }

    @Test
    void crear_debeLanzarExcepcion_cuandoElTelefonoTieneLetras() {
        assertThatThrownBy(() -> Maestro.crear(
                "Laura", "Martínez", "López", "laura@escuela.com", "555101078A"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El telefono es invalido");
    }

    @Test
    void actualizar_debeModificarDatos_cuandoTodoEsValido() {
        // Arrange
        Maestro maestro = Maestro.builder()
                .nombre("Nombre viejo")
                .apellidoPaterno("Paterno viejo")
                .apellidoMaterno("Materno viejo")
                .email("viejo@escuela.com")
                .telefono("5550000000")
                .build();

        // Act
        maestro.actualizar("Karla", "Gómez", "Pérez", "KARLA@ESCUELA.COM", "5559999999");

        // Assert
        assertThat(maestro.getNombre()).isEqualTo("Karla");
        assertThat(maestro.getEmail()).isEqualTo("karla@escuela.com");
        assertThat(maestro.getTelefono()).isEqualTo("5559999999");
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElApellidoEsMuyCorto() {
        // Arrange
        Maestro maestro = Maestro.builder()
                .nombre("Nombre válido")
                .apellidoPaterno("Paterno válido")
                .apellidoMaterno("Materno válido")
                .email("valido@escuela.com")
                .telefono("5550000000")
                .build();

        // Act + Assert
        assertThatThrownBy(() -> maestro.actualizar(
                "Karla", "Go", "Pérez", "karla@escuela.com", "5559999999"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("El apellido paterno es requerido");
    }

    @Test
    void asignarGrupo_debeLanzarExcepcion_cuandoElGrupoEsNulo() {
        // Arrange
        Maestro maestro = Maestro.crear(
                "Laura", "Martínez", "López", "laura@escuela.com", "5551010789");

        // Act + Assert
        assertThatThrownBy(() -> maestro.asignarGrupo(null))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El grupo es requerido");
    }
}
