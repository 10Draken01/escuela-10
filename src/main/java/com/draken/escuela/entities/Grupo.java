package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "GRUPOS",
        uniqueConstraints = {
                @UniqueConstraint(name = "GRUPO_CU_MA_AU_PE_UK", columnNames = {
                        "ID_CURSO", "ID_MAESTRO", "ID_AULA", "PERIODO"
                })
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_GRUPO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CURSO", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MAESTRO", nullable = false)
    private Maestro maestro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AULA", nullable = false)
    private Aula aula;

    @Column(name = "PERIODO", length = 20, nullable = false)
    private String periodo;

    @Builder.Default
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    private static void validarDatos(String periodo){
        StringCustomUtils.validarTamanio(periodo, 10, 20,
        "El periodo es requerido y debe tener entre 10 y 20 caracteres"
        );
    }

    public void asignarMaestro(Maestro maestro){
        if (maestro == null)
            throw new DatoInvalidoException("El maestro es requerido");

        this.maestro = maestro;
    }

    public void asignarAula(Aula aula){
        if (aula == null)
            throw new DatoInvalidoException("El aula es requerida");

        this.aula = aula;
    }

    public void cambiarAula(Aula aula){
        if (aula == null)
            throw new DatoInvalidoException("El aula es requerida");
        this.aula.desasignarGrupo(this);
        this.aula = aula;
    }

    public void inscribirAlumno(Alumno alumno){
        if (alumno == null)
            throw new DatoInvalidoException("El alumno es requerido");

        Inscripcion inscripcion = Inscripcion.crear();

        inscripcion.asignarAlumno(alumno);
        inscripcion.asignarGrupo(this);

        this.inscripciones.add(inscripcion);
    }

    public static Grupo crear(
        String periodo
    ){
        validarDatos(periodo);
        return Grupo.builder()
                .periodo(periodo)
                .build();
    }
}
