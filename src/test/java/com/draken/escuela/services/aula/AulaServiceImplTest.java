package com.draken.escuela.services.aula;

import com.draken.escuela.dto.aula.AulaRequest;
import com.draken.escuela.dto.aula.AulaResponse;
import com.draken.escuela.entities.Aula;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.exceptions.RecursoNoEncontradoException;
import com.draken.escuela.mapper.AulaMapper;
import com.draken.escuela.repositories.AulaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AulaServiceImplTest {

    @Mock
    private AulaRepository aulaRepository;

    @Mock
    private AulaMapper aulaMapper;

    @InjectMocks
    private AulaServiceImpl aulaService;

    private Aula aula;
    private AulaResponse aulaResponse;

    @BeforeEach
    void setUp() {
        aula = Aula.builder()
                .id(1L)
                .nombre("Aula 101")
                .capacidad(30)
                .build();

        aulaResponse = new AulaResponse(1L, "Aula 101", 30);
    }

    // ---------- listar() ----------

    @Test
    void listar_debeRetornarListaDeAulas_cuandoExistenRegistros() {
        // Arrange
        when(aulaRepository.findAll()).thenReturn(List.of(aula));
        when(aulaMapper.entidadAResponse(aula)).thenReturn(aulaResponse);

        // Act
        List<AulaResponse> resultado = aulaService.listar();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nombre()).isEqualTo("Aula 101");
        verify(aulaRepository).findAll();
    }

    @Test
    void listar_debeRetornarListaVacia_cuandoNoHayAulasRegistradas() {
        // Arrange
        when(aulaRepository.findAll()).thenReturn(List.of());

        // Act
        List<AulaResponse> resultado = aulaService.listar();

        // Assert
        assertThat(resultado).isEmpty();
    }

    // ---------- obtenerPorId() ----------

    @Test
    void obtenerPorId_debeRetornarAula_cuandoExiste() {
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
        when(aulaMapper.entidadAResponse(aula)).thenReturn(aulaResponse);

        AulaResponse resultado = aulaService.obtenerPorId(1L);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.capacidad()).isEqualTo(30);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(aulaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aulaService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Aula no encontrado con id: 99");
    }

    // ---------- registrar() ----------

    @Test
    void registrar_debeGuardarYRetornarAula_cuandoElNombreNoExistePreviamente() {
        // Arrange: Aula.crear(...) es código real; el nombre queda sin espacios ("Aula 101")
        AulaRequest request = new AulaRequest("Aula 101", 30);

        // El service pregunta por el nombre ya limpio (trimmed)
        when(aulaRepository.existsByNombre("Aula 101")).thenReturn(false);
        when(aulaMapper.entidadAResponse(any(Aula.class))).thenReturn(aulaResponse);

        // Act
        AulaResponse resultado = aulaService.registrar(request);

        // Assert
        assertThat(resultado).isEqualTo(aulaResponse);
        verify(aulaRepository).save(any(Aula.class));
    }

    @Test
    void registrar_debeLanzarExcepcion_cuandoYaExisteUnaAulaConEseNombre() {
        // Arrange
        AulaRequest request = new AulaRequest("Aula 101", 30);
        when(aulaRepository.existsByNombre("Aula 101")).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> aulaService.registrar(request))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("El nombre del aula ya existe");

        // Si algo falla antes, no se guarda nada a medias
        verify(aulaRepository, never()).save(any());
    }

    // ---------- actualizar() ----------

    @Test
    void actualizar_debeActualizarDatos_cuandoAulaExisteYNombreEstaLibre() {
        // Arrange
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
        when(aulaRepository.existsByNombreAndIdNot("Aula 202", 1L)).thenReturn(false);

        AulaRequest request = new AulaRequest("Aula 202", 40);
        AulaResponse respuestaEsperada = new AulaResponse(1L, "Aula 202", 40);
        when(aulaMapper.entidadAResponse(aula)).thenReturn(respuestaEsperada);

        // Act
        AulaResponse resultado = aulaService.actualizar(request, 1L);

        // Assert
        assertThat(resultado.nombre()).isEqualTo("Aula 202");
        assertThat(aula.getCapacidad()).isEqualTo(40);
        verify(aulaRepository).save(aula);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElAulaNoExiste() {
        when(aulaRepository.findById(99L)).thenReturn(Optional.empty());

        AulaRequest request = new AulaRequest("Aula 202", 40);

        assertThatThrownBy(() -> aulaService.actualizar(request, 99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Aula no encontrado con id: 99");

        verify(aulaRepository, never()).save(any());
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNuevoNombreYaLoUsaOtraAula() {
        // Arrange: el aula id=1 existe, pero el nombre nuevo ya lo tiene OTRA aula
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
        when(aulaRepository.existsByNombreAndIdNot("Aula 202", 1L)).thenReturn(true);

        AulaRequest request = new AulaRequest("Aula 202", 40);

        // Act + Assert
        assertThatThrownBy(() -> aulaService.actualizar(request, 1L))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Una aula con el mismo nombre ya existe");

        verify(aulaRepository, never()).save(any());
    }

    // ---------- eliminar() ----------

    @Test
    void eliminar_debeEliminarAula_cuandoExiste() {
        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));

        aulaService.eliminar(1L);

        verify(aulaRepository).delete(aula);
        // eliminar() también llama a flush() explícitamente
        verify(aulaRepository).flush();
    }

    @Test
    void eliminar_debeLanzarExcepcion_cuandoNoExiste() {
        when(aulaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> aulaService.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);

        verify(aulaRepository, never()).delete(any());
    }
}
