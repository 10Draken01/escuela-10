package com.draken.escuela.entities;

import com.draken.escuela.enums.Dia;
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
    private Dia dia;

    @Column(name = "HORA_INICIO", length = 5, nullable = false)
    private String horaInicio;

    @Column(name = "HORA_FIN", length = 5, nullable = false)
    private String horaFin;

    private static void validarDatos(
        String horaInicio,
        String horaFin
    ){
        StringCustomUtils.validarTamanio(
                horaInicio, 5, 5,
                "El hora de inicio es requerido y debe tener este formato: 00:00"
        );

        StringCustomUtils.validarTamanio(
                horaFin, 5, 5,
                "El hora de fin es requerido y debe tener este formato: 00:00"
        );
    }

    public static Horario crear(
            Dia dia,
            String horaInicio,
            String horaFin
    ){
        validarDatos(horaInicio, horaFin);

      return Horario.builder()
              .dia(dia)
              .horaInicio(horaInicio)
              .horaFin(horaFin)
              .build();
    }
}
