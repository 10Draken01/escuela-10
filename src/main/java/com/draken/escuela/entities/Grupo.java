package com.draken.escuela.entities;

import com.draken.escuela.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private static void validarDatos(String periodo){
        StringCustomUtils.validarTamanio(periodo, 10, 20,
        "El periodo es requerido y debe tener entre 10 y 20 caracteres"
        );
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
