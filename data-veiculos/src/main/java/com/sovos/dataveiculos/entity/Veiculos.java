package com.sovos.dataveiculos.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor

public class Veiculos {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer veiculoId;

    @Column(nullable = false,length = 9)
    private String placa;

    @Column(nullable = true)
    private String cor;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false,length = 11)
    private String cpfProprietario;
}
