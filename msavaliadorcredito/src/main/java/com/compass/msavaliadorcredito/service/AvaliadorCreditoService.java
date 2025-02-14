package com.compass.msavaliadorcredito.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compass.msavaliadorcredito.clients.CartoesClient;
import com.compass.msavaliadorcredito.clients.ClienteClient;
import com.compass.msavaliadorcredito.ex.DadosClienteNotFoundException;
import com.compass.msavaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import com.compass.msavaliadorcredito.model.CartaoCliente;
import com.compass.msavaliadorcredito.model.DadosCliente;
import com.compass.msavaliadorcredito.model.SituacaoCliente;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteClient clienteClient;
    private final CartoesClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) 
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException {
        try{
        ResponseEntity<DadosCliente> dadosClienteResponse = clienteClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> cartoesResponse = cartoesClient.getCartoesByCliente(cpf);
        return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteResponse.getBody())
                    .cartoes(cartoesResponse.getBody())
                    .build();
                    
        }catch (FeignException.FeignClientException e) {
                int status = e.status();
                if(HttpStatus.NOT_FOUND.value() == status) {
                    throw new DadosClienteNotFoundException();
                }
                throw new ErroComunicacaoMicroservicesException(e.getMessage(), e.status());
        }
    }
}
