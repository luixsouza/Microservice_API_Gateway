package com.compass.mscartoes.dto;

import java.math.BigDecimal;

import com.compass.mscartoes.domain.BandeiraCartao;
import com.compass.mscartoes.domain.Cartao;

import lombok.Data;

@Data
public class CartaoSaveRequest {

    private String nome;
    private BandeiraCartao bandeira;
    private BigDecimal renda;
    private BigDecimal limite;

    public Cartao toModel(){
        return new Cartao(nome, bandeira, renda, limite);
    }
}
