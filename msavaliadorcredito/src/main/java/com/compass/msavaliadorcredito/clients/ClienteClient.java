package com.compass.msavaliadorcredito.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.compass.msavaliadorcredito.model.DadosCliente;

@FeignClient(value = "msclientes", path = "/clientes")
public interface ClienteClient {

    @GetMapping(params = "cpf")
    ResponseEntity<DadosCliente> dadosCliente(@RequestParam("cpf") String cpf);
}
