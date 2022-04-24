package com.sovos.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PessoaDTO {

    private String id;
    private String name;
    private String address;
    private String cpf;
    private String command;

}
