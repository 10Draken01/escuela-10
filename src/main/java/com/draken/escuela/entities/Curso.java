package com.draken.escuela.entities;

import com.draken.escuela.utils.StringCustomUtils;
import com.draken.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "CURSOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CURSO")
    private Long id;

    @Column(name = "NOMBRE", length = 100, nullable = false)
    private String nombre;

    @Column(name = "DESCRIPCION", length = 200, nullable = false)
    private String descripcion;

    @Column(name = "CREDITOS", nullable = false)
    private Integer creditos;

    private static void validarDatos(
            String nombre,
            String descripcion,
            Integer creditos
    ){
        StringCustomUtils.validarTamanio(
                nombre, 5, 100,
                "El nombre es requerido y debe tener entre 5 y 100 caracteres"
        );

        StringCustomUtils.validarTamanio(
                descripcion, 5, 200,
                "La descripcion es requerida y debe tener entre 5 y 200 caracteres"
        );

        ValoresNumericosUtils.validarEnteroPositvo(
                creditos,
                "El credito es requerido y debe ser positivo"
        );
    }

    public static Curso crear(
        String nombre,
        String descripcion,
        Integer creditos
    ){
        return Curso.builder()
                .nombre(nombre.trim())
                .descripcion(descripcion.trim())
                .creditos(creditos)
                .build();
    }
}
