package com.draken.escuela.controller;

import com.draken.escuela.dto.aula.AulaRequest;
import com.draken.escuela.dto.aula.AulaResponse;
import com.draken.escuela.exceptions.RecursoNoEncontradoException;
import com.draken.escuela.services.aula.AulaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AulaController.class)
class AulaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @MockitoBean reemplaza al antiguo @MockBean en Spring Boot 4.
    @MockitoBean
    private AulaService aulaService;

    @Test
    void registrar_debeRetornar201_cuandoDatosSonValidos() throws Exception {
        // Arrange
        AulaRequest request = new AulaRequest("Aula 101", 30);
        AulaResponse response = new AulaResponse(1L, "Aula 101", 30);

        when(aulaService.registrar(any(AulaRequest.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/api/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Aula 101"));
    }

    @Test
    void registrar_debeRetornar400_cuandoElNombreEsMuyCorto() throws Exception {
        // Arrange: "Ab" viola @Size(min = 5) del AulaRequest
        AulaRequest request = new AulaRequest("Ab", 30);

        // Act + Assert
        mockMvc.perform(post("/api/aulas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // La validación @Valid falla ANTES de llegar al service
        verify(aulaService, never()).registrar(any());
    }

    @Test
    void obtenerPorId_debeRetornar404_cuandoElAulaNoExiste() throws Exception {
        // Arrange
        when(aulaService.obtenerPorId(99L))
                .thenThrow(new RecursoNoEncontradoException("Aula no encontrado con id: 99"));

        // Act + Assert: GlobalExceptionHandler la convierte en 404 con ProblemDetail
        mockMvc.perform(get("/api/aulas/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Aula no encontrado con id: 99"));
    }

    @Test
    void obtenerPorId_debeRetornar400_cuandoElIdEsNegativo() throws Exception {
        // El @Positive del controller rechaza esto antes de llegar al service
        mockMvc.perform(get("/api/aulas/{id}", -1L))
                .andExpect(status().isBadRequest());

        verify(aulaService, never()).obtenerPorId(any());
    }
}
