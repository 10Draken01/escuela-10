package com.draken.escuela.entities;

import com.draken.escuela.enums.DiaSemana;
import com.draken.escuela.utils.HoraUtils;
import com.draken.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "HORARIOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HORARIO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;

    @Enumerated(EnumType.STRING)
    @Column(name = "DIA", length = 15, nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "HORA_INICIO", length = 5, nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", length = 5, nullable = false)
    private String horaFin;

    private static void validarDatos(
        String horaInicio,
        String horaFin
    ){
        StringCustomUtils.validarNoVacioNoNull(horaInicio, "La hora de inicio es requerida");
        HoraUtils.validarHora(horaInicio, "El formato de hora inicio debe ser HH:mm");
        StringCustomUtils.validarNoVacioNoNull(horaFin, "La hora de fin es requerida");
        HoraUtils.validarHora(horaFin, "El formato de hora fin debe ser HH:mm");
    }

    public static Horario crear(
            DiaSemana diaSemana,
            String horaInicio,
            String horaFin
    ){
        validarDatos(horaInicio, horaFin);

      return Horario.builder()
              .diaSemana(diaSemana)
              .horaInicio(horaInicio)
              .horaFin(horaFin)
              .build();
    }
}
