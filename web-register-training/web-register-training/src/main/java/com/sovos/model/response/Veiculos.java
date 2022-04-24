package com.sovos.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class Veiculos {
    private Integer veiculoId;
    private String placa;
    private String cor;
    private String modelo;
    private String cpfProprietario;

}
