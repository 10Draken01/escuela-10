package com.draken.escuela.enums;

import com.draken.escuela.exceptions.DatoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiaSemanaTest {

    @Test
    void obtenerDiaPorDescriptcion_debeEncontrarDia_ignorandoMayusculas() {
        // Act
        DiaSemana dia = DiaSemana.obtenerDiaPorDescriptcion("lunes");

        // Assert
        assertThat(dia).isEqualTo(DiaSemana.LUNES);
    }

    @Test
    void obtenerDiaPorDescriptcion_debeEncontrarDia_conMayusculasYAcento() {
        // "MIÉRCOLES" normalizado es "miercoles", igual que la descripción del enum
        DiaSemana dia = DiaSemana.obtenerDiaPorDescriptcion("MIÉRCOLES");

        assertThat(dia).isEqualTo(DiaSemana.MIERCOLES);
    }

    @Test
    void obtenerDiaPorDescriptcion_debeLanzarExcepcion_cuandoNoExisteElDia() {
        assertThatThrownBy(() -> DiaSemana.obtenerDiaPorDescriptcion("Domingo"))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessageContaining("No existe un dia con descripcion: Domingo");
    }

    @Test
    void obtenerDiaPorDescriptcion_debeLanzarExcepcion_cuandoLaDescripcionEsVacia() {
        assertThatThrownBy(() -> DiaSemana.obtenerDiaPorDescriptcion("   "))
                .isInstanceOf(DatoInvalidoException.class)
                .hasMessage("El horario es requerido");
    }
}
