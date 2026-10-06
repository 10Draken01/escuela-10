package com.draken.escuela.services.curso;

import com.draken.escuela.dto.curso.CursoRequest;
import com.draken.escuela.dto.curso.CursoResponse;
import com.draken.escuela.entities.Curso;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.exceptions.RecursoNoEncontradoException;
import com.draken.escuela.mapper.CursoMapper;
import com.draken.escuela.repositories.CursoRepository;
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
class CursoServiceImplTest {

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoMapper cursoMapper;

    @InjectMocks
    private CursoServiceImpl cursoService;

    private Curso curso;
    private CursoResponse cursoResponse;

    @BeforeEach
    void setUp() {
        curso = Curso.builder()
                .id(1L)
                .nombre("Matemáticas I")
                .descripcion("Fundamentos matemáticos")
                .creditos(6)
                .build();

        cursoResponse = new CursoResponse(1L, "Matemáticas I", "Fundamentos matemáticos", 6);
    }

    // ---------- listar() ----------

    @Test
    void listar_debeRetornarListaDeCursos_cuandoExistenRegistros() {
        when(cursoRepository.findAll()).thenReturn(List.of(curso));
        when(cursoMapper.entidadAResponse(curso)).thenReturn(cursoResponse);

        List<CursoResponse> resultado = cursoService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nombre()).isEqualTo("Matemáticas I");
        verify(cursoRepository).findAll();
    }

    @Test
    void listar_debeRetornarListaVacia_cuandoNoHayCursosRegistrados() {
        when(cursoRepository.findAll()).thenReturn(List.of());

        List<CursoResponse> resultado = cursoService.listar();

        assertThat(resultado).isEmpty();
    }

    // ---------- obtenerPorId() ----------

    @Test
    void obtenerPorId_debeRetornarCurso_cuandoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoMapper.entidadAResponse(curso)).thenReturn(cursoResponse);

        CursoResponse resultado = cursoService.obtenerPorId(1L);

        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.creditos()).isEqualTo(6);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Curso no encontrado con id: 99");
    }

    // ---------- registrar() ----------

    @Test
    void registrar_debeGuardarYRetornarCurso_cuandoElNombreNoExistePreviamente() {
        CursoRequest request = new CursoRequest("Matemáticas I", "Fundamentos matemáticos", 6);

        when(cursoRepository.existsByNombre("Matemáticas I")).thenReturn(false);
        when(cursoMapper.entidadAResponse(any(Curso.class))).thenReturn(cursoResponse);

        CursoResponse resultado = cursoService.registrar(request);

        assertThat(resultado).isEqualTo(cursoResponse);
        verify(cursoRepository).save(any(Curso.class));
    }

    @Test
    void registrar_debeLanzarExcepcion_cuandoYaExisteUnCursoConEseNombre() {
        CursoRequest request = new CursoRequest("Matemáticas I", "Fundamentos matemáticos", 6);
        when(cursoRepository.existsByNombre("Matemáticas I")).thenReturn(true);

        assertThatThrownBy(() -> cursoService.registrar(request))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Nombre del curso ya existente");

        verify(cursoRepository, never()).save(any());
    }

    // ---------- actualizar() ----------

    @Test
    void actualizar_debeActualizarDatos_cuandoCursoExisteYNombreEstaLibre() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNombreAndIdNot("Historia I", 1L)).thenReturn(false);

        CursoRequest request = new CursoRequest("Historia I", "Historia universal", 8);
        CursoResponse respuestaEsperada = new CursoResponse(1L, "Historia I", "Historia universal", 8);
        when(cursoMapper.entidadAResponse(curso)).thenReturn(respuestaEsperada);

        CursoResponse resultado = cursoService.actualizar(request, 1L);

        assertThat(resultado.nombre()).isEqualTo("Historia I");
        assertThat(curso.getCreditos()).isEqualTo(8);
        verify(cursoRepository).save(curso);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElCursoNoExiste() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        CursoRequest request = new CursoRequest("Historia I", "Historia universal", 8);

        assertThatThrownBy(() -> cursoService.actualizar(request, 99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Curso no encontrado con id: 99");

        verify(cursoRepository, never()).save(any());
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNuevoNombreYaLoUsaOtroCurso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNombreAndIdNot("Historia I", 1L)).thenReturn(true);

        CursoRequest request = new CursoRequest("Historia I", "Historia universal", 8);

        assertThatThrownBy(() -> cursoService.actualizar(request, 1L))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Ya existe un curso con el nombre: Historia I");

        verify(cursoRepository, never()).save(any());
    }

    // ---------- eliminar() ----------

    @Test
    void eliminar_debeEliminarCurso_cuandoExiste() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        cursoService.eliminar(1L);

        verify(cursoRepository).delete(curso);
        verify(cursoRepository).flush();
    }

    @Test
    void eliminar_debeLanzarExcepcion_cuandoNoExiste() {
        when(cursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cursoService.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);

        verify(cursoRepository, never()).delete(any());
    }
}
