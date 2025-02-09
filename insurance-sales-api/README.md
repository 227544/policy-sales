# Insurance API

Este projeto contém a API de seguro que se conecta a uma rede Hyperledger Fabric.

## Pré-requisitos

- Docker
- Maven
- Java 11

## Construção do Projeto

1. Compile o projeto usando Maven:

    ```sh
    mvn clean install
    ```

2. Construa a imagem Docker:

    ```sh
    docker build -t insurance-sales-api .
    ```

## Executando o Container

1. Execute o container Docker:

    ```sh
    docker run -e CORE_PEER_ADDRESS=peer0.org1.example.com:7051 \
               -e CORE_PEER_LOCALMSPID=Org1MSP \
               -e CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/tls/ca.crt \
               -e CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp \
               -p 8080:8080 insurance-sales-api
    ```

## Estrutura do Projeto

- [Dockerfile](http://_vscodecontentref_/0): Arquivo Docker para construir a imagem do container.
- `src/main/resources/connection-org1.json`: Arquivo de configuração para conexão com a rede Hyperledger Fabric.
- `target/insurance-sales-api-0.0.1-SNAPSHOT.jar`: Arquivo JAR gerado pelo Maven.

## Configurações do Dockerfile

O Dockerfile está configurado para copiar os arquivos necessários para o container e definir as variáveis de ambiente:

```dockerfile
FROM openjdk:11-jre-slim
VOLUME /tmp

# Adicione as variáveis de ambiente necessárias
ENV CORE_PEER_ADDRESS=peer0.org1.example.com:7051
ENV CORE_PEER_LOCALMSPID=Org1MSP
ENV CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/tls/ca.crt
ENV CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp

# Copie os arquivos necessários para o container
COPY target/insurance-sales-api-0.0.1-SNAPSHOT.jar /insurance-sales-api.jar
COPY src/main/resources/connection-org1.json /etc/hyperledger/connection-org1.json
COPY Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt /etc/hyperledger/tls/ca.crt
COPY /Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/ordererOrganizations/example.com/msp /etc/hyperledger/msp

ENTRYPOINT ["java","-jar","/insurance-sales-api.jar"]
