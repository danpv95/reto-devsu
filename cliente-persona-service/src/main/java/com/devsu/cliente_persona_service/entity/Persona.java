package com.devsu.cliente_persona_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String genero;
    @Column(nullable = false)
    private Integer edad;
    @Column(nullable = false, unique = true)
    private String identificacion;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private String telefono;
}
