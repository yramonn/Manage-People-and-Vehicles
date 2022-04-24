package com.sovos.dataregister.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor

public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long pessoaId;

    @Column(name ="name" )
    private String nome;

    @Column
    private String endereco;

    @Column
    private String cpf;


}
