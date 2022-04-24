package com.sovos.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VeiculosDTO {

    private Integer id;
    private String placa;
    private String cor;
    private String modelo;
    private String cpfProprietario;
    private String command;

}
