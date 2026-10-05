package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALUMNOS")
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

    @Column(name = "EMAIL", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "MATRICULA", length = 10, nullable = false, unique = true)
    private String matricula;

    @Column(name = "FECHA_INGRESO", nullable = false)
    private LocalDate fechaIngreso;

    @Builder.Default
    @OneToMany(mappedBy = "alumno", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

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

    public void agregarInscripcion(Inscripcion inscripcion){
        if (inscripcion == null)
            throw new DatoInvalidoException("La inscripcion es requerida");
        this.inscripciones.add(inscripcion);
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
