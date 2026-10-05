package com.draken.escuela.entities;


import com.draken.escuela.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(
        name = "INSCRIPCIONES",
        uniqueConstraints = {
                @UniqueConstraint(name = "INSCRIPCION_ALU_GRU_UK", columnNames = {
                        "ID_ALUMNO", "ID_GRUPO"
                })
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INSCRIPCION")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALUMNO", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GRUPO", nullable = false)
    private Grupo grupo;
    @Column(name = "FECHA_INSCRIPCION", nullable = false)
    private LocalDate fechaInscripcion;

    public void asignarAlumno(Alumno alumno){
        if(alumno == null)
            throw new DatoInvalidoException("El alumno es requerido");

        alumno.agregarInscripcion(this);
        this.alumno = alumno;
    }


    public void asignarGrupo(Grupo grupo){
        if(grupo == null)
            throw new DatoInvalidoException("El grupo es requerido");

        this.grupo = grupo;
    }

    public static Inscripcion crear(){
        return Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .build();
    }
}
