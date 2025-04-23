package com.devsu.cliente_persona_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder                      // hereda el builder de Persona
@Table(name = "cliente")
public class Cliente extends Persona {

    @Column(nullable = false)
    private String contrasena;
    @Column(nullable = false)
    private Boolean estado;

    public Long getId() {
        return super.getId();
    }

}
