# Microservices - Sistema de Cartão de Crédito

## Descrição
Este projeto implementa um sistema de cartão de crédito baseado em arquitetura de microserviços, utilizando tecnologias como Spring Boot, Docker, Keycloak para autenticação e RabbitMQ para mensageria.

## Tecnologias Utilizadas
- **Spring Boot** - Desenvolvimento dos microserviços
- **Spring Cloud Gateway** - API Gateway
- **Spring Cloud Eureka** - Service Discovery
- **Keycloak** - Gerenciamento de autenticação
- **RabbitMQ** - Mensageria
- **Docker** - Containerização dos microserviços
- **Insomnia** - Testes dos endpoints

## Arquitetura dos Microserviços
O sistema é composto pelos seguintes microserviços:

1. **ms-gateway**: Responsável pelo roteamento das requisições.
2. **ms-eureka**: Responsável pelo service discovery.
3. **ms-clientes**: Gerencia os dados dos clientes.
4. **ms-cartoes**: Gerencia a emissão e consulta de cartões.
5. **ms-avaliadorcredito**: Avalia a situação de crédito do cliente.
6. **ms-keycloak**: Gerencia a autenticação e autorização.
7. **RabbitMQ**: Implementa a comunicação assíncrona.

## Endpoints Principais
Os endpoints principais são os seguintes:

- **Autenticação**
  - `POST /key-token` - Geração do token JWT

- **Avaliador de Crédito**
  - `GET /situacao-cliente` - Consulta a situação de crédito do cliente
  - `POST /avaliacao-cliente` - Avaliação de crédito
  - `POST /solicitacao-emissao-cartao` - Solicitação de cartão

- **Cartões**
  - `POST /cartoes/salvar` - Cadastro de cartão
  - `GET /cartoes/renda` - Busca cartões por renda
  - `GET /cartoes/cliente` - Busca cartão por cliente

- **Clientes**
  - `POST /clientes/salvar` - Cadastro de cliente
  - `GET /clientes/dados` - Consulta de dados do cliente
  - `GET /clientes/status` - Status do cliente

## Endpoints no Insomnia
![Endpoints](https://github.com/luixsouza/Microservice_API_Gateway/blob/main/assets/endpoints.png)

## Containers no Docker
![Containers](https://github.com/luixsouza/Microservice_API_Gateway/blob/main/assets/containers.png)

## Imagens no Docker
![System Architecture](https://github.com/luixsouza/Microservice_API_Gateway/blob/main/assets/imagens.png)

## Como Executar o Projeto
1. Clone o repositório:
   ```sh
   git clone https://github.com/luixsouza/microservices-credit-card.git
   ```
2. Suba os containers com Docker Compose:
   ```sh
   docker-compose up -d
   ```
3. Acesse a documentação do Swagger:
   ```
   http://localhost:8080/swagger-ui.html
   ```