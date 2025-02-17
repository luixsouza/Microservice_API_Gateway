package com.compass.msavaliadorcredito.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.compass.msavaliadorcredito.clients.CartoesClient;
import com.compass.msavaliadorcredito.clients.ClienteClient;
import com.compass.msavaliadorcredito.ex.DadosClienteNotFoundException;
import com.compass.msavaliadorcredito.ex.ErroComunicacaoMicroservicesException;
import com.compass.msavaliadorcredito.ex.ErroSolicitacaoCartaoException;
import com.compass.msavaliadorcredito.infra.mqueue.SolicitarEmissaoCartaoPublisher;
import com.compass.msavaliadorcredito.model.Cartao;
import com.compass.msavaliadorcredito.model.CartaoAprovado;
import com.compass.msavaliadorcredito.model.CartaoCliente;
import com.compass.msavaliadorcredito.model.DadosCliente;
import com.compass.msavaliadorcredito.model.DadosSolicitacaoEmissaoCartao;
import com.compass.msavaliadorcredito.model.ProtocoloSolicitacaoCartao;
import com.compass.msavaliadorcredito.model.RetornoAvaliacao;
import com.compass.msavaliadorcredito.model.SituacaoCliente;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteClient clienteClient;
    private final CartoesClient cartoesClient;
    private final SolicitarEmissaoCartaoPublisher emissaoCartaoPublisher;

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

    public RetornoAvaliacao realizarAvaliacao(String cpf, Long renda)
            throws DadosClienteNotFoundException, ErroComunicacaoMicroservicesException{
        try{
            ResponseEntity<DadosCliente> dadosClienteResponse = clienteClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAteh(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {

                DadosCliente dadosCliente = dadosClienteResponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;

            }).collect(Collectors.toList());

            return new RetornoAvaliacao(listaCartoesAprovados);

        }catch (FeignException.FeignClientException e) {
            int status = e.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicroservicesException(e.getMessage(), e.status());
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao (DadosSolicitacaoEmissaoCartao dados) {
        try {
            emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception e) {
            throw new ErroSolicitacaoCartaoException(e.getMessage());
        }
    }
}
