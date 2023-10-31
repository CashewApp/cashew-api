# Cashew API

API em Java destinada para ser utilizada pelos clientes web e mobile do projeto Cashew
## Sumário
1. [O Que é o Cashew?](#o-que-é-o-cashew)
2. [Tecnologias](#tecnologias-utilizadas-para-desenvolvimento-da-api)
3. [Como Instalar e Executar a API](#como-instalar-e-executar-a-api)
4. [Como Usar a API](#como-usar-a-api)

## O que é o Cashew?

Este projeto consiste no desenvolvimento de uma plataforma de gestão, compra e retirada de pedidos realizados em unidades de lanchonetes escolares, 
visando aprimorar o relacionamento do cliente com o estabelecimento. 

<details>
    <summary>Casos de Uso</summary>

- Visualizar todos os cupons utilizáveis;
- Visualizar meios de pagamento cadastrados;
- Adicionar cartão de crédito;
- Adicionar cartão de débito;
- Pagar pedido;
- Pagar com pix;
- Pagar com cartão de débito;
- Pagar com cartão de crédito;
- Pagar com dinheiro;
- Visualizar avaliações da lanchonete;
- Visualizar histórico de pedidos;
- Visualizar produtos mais vendidos das lanchonetes no campus e universidade definidos;
- Visualizar as lanchonetes baseadas na universidade e campus definidos;
- Visualizar produtos de uma lanchonete;
- Visualizar perfil de uma lanchonete;
- Editar meio de pagamento;
- Aumentar ou diminuir a quantidade de itens de um produto no carrinho;
- Remover itens do carrinho;
- Visualizar itens do carrinho;
- Visualizar dados de um pedido;
- Remover cupom de um pedido;
- Cancelar pedido;
- Criar uma categoria de produtos;
- Adicionar produtos a uma categoria;
- Criar um cupom de desconto;
- Visualizar dados de um pedido em específico;
- Aprovar cancelamento de pedido;
- Processar pedido;
- Definir um prazo de validade de um cupom;
- Definir um número máximo de utilizações de um cupom;
- Definir status do pedido;
- Editar cupom de desconto;
- Adicionar quantidade em estoque de um produto;
- Visualizar avaliação do pedido;
- Apagar um cupom de desconto;
- Cadastrar saída de saldo;
- Editar um produto;
- Visualizar todos os cupons;
- Cadastrar entrada de saldo;
- Visualizar todos os pedidos feitos;
- Remover uma categoria de produtos;
- Editar uma categoria de produtos;
- Remover produtos de uma categoria;
- Visualizar todos os produtos cadastrados;
- Registrar funcionários da lanchonete;
- Calcular receita;
- Vincular uma faculdade e campus com a lanchonete;
- Cadastrar um novo produto;
- Apagar um produto;
- Editar os dados da lanchonete;
- Adicionar descrição.
</details>

## Tecnologias utilizadas para desenvolvimento da API

- ### Spring Boot
  - Spring Data JPA
  - Spring Security
  - Spring OAuth2ResourceServer
  - Spring Devtools
  - Spring Web
  - Lombok
  - Spring Boot Starter Test
  - Hibernate Validator
  - Spring Cloud AWS
- ### MariaDB
- ### Postman
- AWS SDK v2 Java

## Como Instalar e Executar a API

### Dependências

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [MariaDB Server](https://mariadb.org/download/?t=mariadb)
- [Maven](https://maven.apache.org/download.cgi)
- IDE (VSCode, IntelliJ, Eclipse)

### Download

- Baixe o projeto


    ```git clone https://github.com/CashewApp/cashew-backend.git```

### Executar

- No windows
    
    
    ```mvnw spring-boot:run```

- No linux


   ``` ./mvnw spring-boot:run```


## Como Usar a API

A API está configurada na porta 8080 com o prefixo /api/v1

Você pode ver todos os endpoints aqui: [docs](https://documenter.getpostman.com/view/26485477/2s9YXbAmP2)
