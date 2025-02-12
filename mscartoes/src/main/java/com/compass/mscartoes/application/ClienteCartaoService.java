package com.compass.mscartoes.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.compass.mscartoes.domain.ClienteCartao;
import com.compass.mscartoes.infra.repository.ClienteCartaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {

    private final ClienteCartaoRepository clienteCartaoRepository;

    public List<ClienteCartao> listCartoesByCpf(String cpf) {
        return clienteCartaoRepository.findByCpf(cpf);
    }
}
