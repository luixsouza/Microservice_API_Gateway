package com.compass.msavaliadorcredito.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compass.msavaliadorcredito.clients.ClienteClient;
import com.compass.msavaliadorcredito.model.DadosCliente;
import com.compass.msavaliadorcredito.model.SituacaoCliente;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteClient clienteClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {
        ResponseEntity<DadosCliente> dadosClienteResponse = clienteClient.dadosCliente(cpf);
        return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .build();
    }
}
