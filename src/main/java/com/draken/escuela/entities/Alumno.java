package com.draken.escuela.entities;

import com.draken.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "ALUMNOS",
        uniqueConstraints = {
                @UniqueConstraint(name = "ALUMNO_EMAIL_UK", columnNames = "EMAIL"),
                @UniqueConstraint(name = "ALUMNO_MATRICULA_UK", columnNames = "MATRICULA")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALUMNO")
    private Long id;

    @Column(name = "NOMBRE", length = 50, nullable = false)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", length = 50, nullable = false)
    private String apellidoMaterno;

    @Column(name = "EMAIL", length = 100, nullable = false)
    private String email;

    @Column(name = "MATRICULA", length = 10, nullable = false)
    private String matricula;

    @Column(name = "FECHA_INGRESO", nullable = false)
    private LocalDate fechaIngreso;

    private static void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno
    ){
        StringCustomUtils.validarTamanio(
                nombre, 5, 50,
                "El nombre es requerido y debe tener entre 5 y 50 caracteres"
        );

        StringCustomUtils.validarTamanio(
                apellidoPaterno, 5, 50,
                "El apellido paterno es requerido y debe tener entre 5 y 50 caracteres"
        );

        StringCustomUtils.validarTamanio(
                apellidoMaterno, 5, 50,
                "El apellido materno es requerido y debe tener entre 5 y 50 caracteres"
        );
    }

    public static Alumno crear(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno
            // Matricula se genera en la base de datos
    ){
        validarDatos(nombre, apellidoPaterno, apellidoMaterno);

        return Alumno.builder()
                .nombre(nombre.trim())
                .apellidoPaterno(apellidoPaterno.trim())
                .apellidoMaterno(apellidoMaterno.trim())
                .build();
    }
}
