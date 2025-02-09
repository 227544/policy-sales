FROM openjdk:11-jre-slim
VOLUME /tmp

# Adicione as variáveis de ambiente necessárias
ENV CORE_PEER_ADDRESS=peer0.org1.example.com:7051
ENV CORE_PEER_LOCALMSPID=Org1MSP
ENV CORE_PEER_TLS_ROOTCERT_FILE=/etc/hyperledger/tls/ca.crt
ENV CORE_PEER_MSPCONFIGPATH=/etc/hyperledger/msp
ENV CORE_PEER_TLS_ENABLED=true

# Crie os diretórios necessários e ajuste as permissões
RUN mkdir -p /etc/hyperledger/tls /etc/hyperledger/msp /etc/hyperledger/credentials /etc/hyperledger/wallet && \
    chmod -R 777 /etc/hyperledger

# Copie os arquivos necessários para o container
COPY target/insurance-sales-api-1.0-SNAPSHOT.jar /insurance-sales-api.jar
COPY src/main/resources/connection-org1.json /etc/hyperledger/connection-org1.json
COPY src/main/resources/tls/ca.crt /etc/hyperledger/tls/ca.crt
COPY src/main/resources/msp/ /etc/hyperledger/msp/
COPY src/main/resources/credentials/ /etc/hyperledger/credentials/
COPY src/main/resources/wallet /etc/hyperledger/wallet

# Exponha a porta 8080
EXPOSE 8080

# Comando para iniciar a aplicação Spring Boot
ENTRYPOINT ["java","-jar","/insurance-sales-api.jar"]