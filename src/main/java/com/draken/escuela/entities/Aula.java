package com.draken.escuela.entities;

import com.draken.escuela.exceptions.DatoInvalidoException;
import com.draken.escuela.utils.StringCustomUtils;
import com.draken.escuela.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "ID_AULA")
    private Long id;

    @Column(name = "NOMBRE", length = 30, nullable = false, unique = true)
    private String nombre;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Builder.Default
    @OneToMany(mappedBy = "aula", fetch = FetchType.LAZY)
    private List<Grupo> grupos = new ArrayList<>();

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

    public void asignarGrupo(Grupo grupo){
        if(grupo == null)
            throw new DatoInvalidoException("El grupo es requerido");

        grupo.asignarAula(this);
        this.grupos.add(grupo);
    }

    public void actualizar(
            String nombre,
            Integer capacidad
    ){
        validarDatos(nombre, capacidad);

        this.nombre = nombre.trim();
        this.capacidad = capacidad;
    }

    public void desasignarGrupo(Grupo grupo){
        this.grupos.remove(grupo);
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
