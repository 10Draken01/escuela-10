package com.draken.escuela.services.maestro;

import com.draken.escuela.dto.datos.DatosCurso;
import com.draken.escuela.dto.maestro.MaestroRequest;
import com.draken.escuela.dto.maestro.MaestroResponse;
import com.draken.escuela.entities.Curso;
import com.draken.escuela.entities.Maestro;
import com.draken.escuela.exceptions.ConflictoException;
import com.draken.escuela.exceptions.EntidadRelacionadaException;
import com.draken.escuela.exceptions.RecursoNoEncontradoException;
import com.draken.escuela.mapper.CursoMapper;
import com.draken.escuela.mapper.MaestroMapper;
import com.draken.escuela.repositories.CursoRepository;
import com.draken.escuela.repositories.GrupoRepository;
import com.draken.escuela.repositories.MaestroRepository;
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
class MaestroServiceImplTest {

    @Mock
    private MaestroRepository maestroRepository;

    @Mock
    private MaestroMapper maestroMapper;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private CursoMapper cursoMapper;

    @Mock
    private GrupoRepository grupoRepository;

    @InjectMocks
    private MaestroServiceImpl maestroService;

    private Maestro maestro;
    private MaestroResponse maestroResponse;

    @BeforeEach
    void setUp() {
        maestro = Maestro.builder()
                .id(1L)
                .nombre("Laura")
                .apellidoPaterno("Martínez")
                .apellidoMaterno("López")
                .email("laura@escuela.com")
                .telefono("5551010789")
                .build();

        maestroResponse = new MaestroResponse(
                1L, "Laura Martínez López", "laura@escuela.com", "5551010789", List.of());
    }

    // ---------- listar() ----------

    @Test
    void listar_debeRetornarListaDeMaestros_cuandoExistenRegistros() {
        when(maestroRepository.findAll()).thenReturn(List.of(maestro));
        when(maestroMapper.entidadAResponse(maestro)).thenReturn(maestroResponse);

        List<MaestroResponse> resultado = maestroService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).email()).isEqualTo("laura@escuela.com");
        verify(maestroRepository).findAll();
    }

    @Test
    void listar_debeRetornarListaVacia_cuandoNoHayMaestrosRegistrados() {
        when(maestroRepository.findAll()).thenReturn(List.of());

        List<MaestroResponse> resultado = maestroService.listar();

        assertThat(resultado).isEmpty();
    }

    // ---------- obtenerPorId() ----------

    @Test
    void obtenerPorId_debeRetornarMaestro_cuandoExiste() {
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro));
        when(maestroMapper.entidadAResponse(maestro)).thenReturn(maestroResponse);

        MaestroResponse resultado = maestroService.obtenerPorId(1L);

        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    void obtenerPorId_debeLanzarExcepcion_cuandoNoExiste() {
        when(maestroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maestroService.obtenerPorId(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Maestro no encontrado con id: 99");
    }

    // ---------- registrar() ----------

    @Test
    void registrar_debeGuardarYRetornarMaestro_cuandoEmailYTelefonoEstanLibres() {
        MaestroRequest request = new MaestroRequest(
                "Laura", "Martínez", "López", "laura@escuela.com", "5551010789");

        // El email se guarda en minúsculas (Maestro.crear lo normaliza)
        when(maestroRepository.existsByEmail("laura@escuela.com")).thenReturn(false);
        when(maestroRepository.existsByTelefono("5551010789")).thenReturn(false);
        when(maestroMapper.entidadAResponse(any(Maestro.class))).thenReturn(maestroResponse);

        MaestroResponse resultado = maestroService.registrar(request);

        assertThat(resultado).isEqualTo(maestroResponse);
        verify(maestroRepository).save(any(Maestro.class));
    }

    @Test
    void registrar_debeLanzarExcepcion_cuandoElEmailYaExiste() {
        MaestroRequest request = new MaestroRequest(
                "Laura", "Martínez", "López", "laura@escuela.com", "5551010789");
        when(maestroRepository.existsByEmail("laura@escuela.com")).thenReturn(true);

        assertThatThrownBy(() -> maestroService.registrar(request))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Email ya existente");

        verify(maestroRepository, never()).save(any());
    }

    @Test
    void registrar_debeLanzarExcepcion_cuandoElTelefonoYaExiste() {
        MaestroRequest request = new MaestroRequest(
                "Laura", "Martínez", "López", "laura@escuela.com", "5551010789");
        when(maestroRepository.existsByEmail("laura@escuela.com")).thenReturn(false);
        when(maestroRepository.existsByTelefono("5551010789")).thenReturn(true);

        assertThatThrownBy(() -> maestroService.registrar(request))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Telefono ya existente");

        verify(maestroRepository, never()).save(any());
    }

    // ---------- actualizar() ----------

    @Test
    void actualizar_debeActualizarDatos_cuandoMaestroExisteYDatosUnicosEstanLibres() {
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro));
        // El service valida con los valores crudos del request
        when(maestroRepository.existsByEmailAndIdNot("karla@escuela.com", 1L)).thenReturn(false);
        when(maestroRepository.existsByTelefonoAndIdNot("5559999999", 1L)).thenReturn(false);

        MaestroRequest request = new MaestroRequest(
                "Karla", "Gómez", "Pérez", "karla@escuela.com", "5559999999");
        MaestroResponse respuestaEsperada = new MaestroResponse(
                1L, "Karla Gómez Pérez", "karla@escuela.com", "5559999999", List.of());
        when(maestroMapper.entidadAResponse(maestro)).thenReturn(respuestaEsperada);

        MaestroResponse resultado = maestroService.actualizar(request, 1L);

        assertThat(resultado.nombre()).isEqualTo("Karla Gómez Pérez");
        assertThat(maestro.getTelefono()).isEqualTo("5559999999");
        verify(maestroRepository).save(maestro);
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElMaestroNoExiste() {
        when(maestroRepository.findById(99L)).thenReturn(Optional.empty());

        MaestroRequest request = new MaestroRequest(
                "Karla", "Gómez", "Pérez", "karla@escuela.com", "5559999999");

        assertThatThrownBy(() -> maestroService.actualizar(request, 99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("Maestro no encontrado con id: 99");

        verify(maestroRepository, never()).save(any());
    }

    @Test
    void actualizar_debeLanzarExcepcion_cuandoElNuevoEmailYaLoUsaOtroMaestro() {
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro));
        when(maestroRepository.existsByEmailAndIdNot("otro@escuela.com", 1L)).thenReturn(true);

        MaestroRequest request = new MaestroRequest(
                "Karla", "Gómez", "Pérez", "otro@escuela.com", "5559999999");

        assertThatThrownBy(() -> maestroService.actualizar(request, 1L))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("Otro maestro ya tiene este email");

        verify(maestroRepository, never()).save(any());
    }

    // ---------- eliminar() ----------

    @Test
    void eliminar_debeEliminarMaestro_cuandoExisteYNoTieneGruposAsignados() {
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro));
        when(grupoRepository.existsByMaestroId(1L)).thenReturn(false);

        maestroService.eliminar(1L);

        verify(maestroRepository).delete(maestro);
        verify(maestroRepository).flush();
    }

    @Test
    void eliminar_debeLanzarExcepcion_cuandoElMaestroTieneGruposAsignados() {
        when(maestroRepository.findById(1L)).thenReturn(Optional.of(maestro));
        when(grupoRepository.existsByMaestroId(1L)).thenReturn(true);

        assertThatThrownBy(() -> maestroService.eliminar(1L))
                .isInstanceOf(EntidadRelacionadaException.class)
                .hasMessageContaining("No se puede eliminar si tiene grupos asignado");

        verify(maestroRepository, never()).delete(any());
    }

    @Test
    void eliminar_debeLanzarExcepcion_cuandoNoExiste() {
        when(maestroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> maestroService.eliminar(99L))
                .isInstanceOf(RecursoNoEncontradoException.class);

        verify(maestroRepository, never()).delete(any());
    }

    // ---------- obtenerCursosDeUnMaestroConId() ----------

    @Test
    void obtenerCursosDeUnMaestroConId_debeRetornarCursos_cuandoElMaestroExiste() {
        // Arrange
        Curso curso = Curso.builder()
                .id(1L).nombre("Matemáticas I").descripcion("Fundamentos").creditos(6)
                .build();
        DatosCurso datosCurso = new DatosCurso("Matemáticas I", "Fundamentos", 6);

        when(maestroRepository.existsById(1L)).thenReturn(true);
        when(cursoRepository.obtenerCursosPorIdMaestro(1L)).thenReturn(List.of(curso));
        when(cursoMapper.entidadADatosCurso(curso)).thenReturn(datosCurso);

        // Act
        List<DatosCurso> resultado = maestroService.obtenerCursosDeUnMaestroConId(1L);

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).nombre()).isEqualTo("Matemáticas I");
    }

    @Test
    void obtenerCursosDeUnMaestroConId_debeLanzarExcepcion_cuandoElMaestroNoExiste() {
        when(maestroRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> maestroService.obtenerCursosDeUnMaestroConId(99L))
                .isInstanceOf(ConflictoException.class)
                .hasMessageContaining("El maestro no existe con id: 99");
    }
}
