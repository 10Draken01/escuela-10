package com.draken.escuela.entities;

import com.draken.escuela.utils.StringCustomUtils;
import com.draken.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "AULAS",
        uniqueConstraints = {
                @UniqueConstraint(name = "AULA_UK", columnNames = "NOMBRE")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @Column(name = "NOMBRE", length = 30, nullable = false)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    public static void validarDatos(
        String nombre,
        Integer capacidad
    ){
        StringCustomUtils.validarTamanio(nombre, 5, 30,
        "El nombre es requerido y debe tener entre 5 y 30 caracteres"
        );

        ValoresNumericosUtils.validarEnteroPositvo(capacidad,
        "La capacidad es requerida y debe ser positiva"
        );
    }

    public static Aula crear(
            String nombre,
            Integer capacidad
    ){
        validarDatos(nombre, capacidad);

        return Aula.builder()
                .nombre(nombre.trim())
                .capacidad(capacidad)
                .build();
    }
}
