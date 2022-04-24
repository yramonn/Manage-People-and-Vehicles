package com.sovos.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Pessoa {

    private long pessoaId;
    private String nome;
    private String endereco;
    private String cpf;

}
