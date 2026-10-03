package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "CALIFICACIONES",
        uniqueConstraints = {
                @UniqueConstraint(name = "CALIFICACION_INSCRIPCION_UK", columnNames = "ID_INSCRIPCION")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Calificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CALIFICACION")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INSCRIPCION", nullable = false)
    private Inscripcion inscripcion;

    @Column(name = "CALIFICACION", nullable = false)
    private Integer calificacion;

    @Column(name = "FECHA_REGISTRO", nullable = false)
    private LocalDate fechaRegistro;

    private static void validarDatos(
        Integer calificacion
    ){
        ValoresNumericosUtils.validarEnteroPositvo(calificacion, "La calificacion debe ser positiva");
        if(calificacion > 10)
            throw new DatoInvalidoException("La calificacion no debe ser mayor a 10");
    }
    public static Calificacion crear(Integer calificacion){
        validarDatos(calificacion);
        return Calificacion.builder()
                .calificacion(calificacion)
                .fechaRegistro(LocalDate.now())
                .build();
    }
}
