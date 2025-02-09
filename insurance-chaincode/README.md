Para invocar um chaincode no Hyperledger Fabric, você precisa seguir alguns passos. Vou fornecer os comandos necessários para invocar um chaincode, assumindo que você já tenha configurado o ambiente corretamente.

1. Defina as variáveis de ambiente para a organização que está invocando o chaincode. Vou usar a Org1 como exemplo:


```
export CORE_PEER_TLS_ENABLED=true
export CORE_PEER_LOCALMSPID="Org1MSP"
export CORE_PEER_TLS_ROOTCERT_FILE=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
export CORE_PEER_MSPCONFIGPATH=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
export CORE_PEER_ADDRESS=localhost:7051
```

2. Defina as variáveis de ambiente para o orderer:

```
export ORDERER_CA=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
export ORDERER_ADDRESS=localhost:7050
```

3. Envie a transação para invocar o chaincode:

```
peer chaincode invoke \
  -o $ORDERER_ADDRESS \
  --ordererTLSHostnameOverride orderer.example.com \
  --tls --cafile $ORDERER_CA \
  -C mychannel -n insurance-chaincode \
  --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE \
  --peerAddresses localhost:9051 --tlsRootCertFiles /Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt \
  -c '{"function":"createPolicy","Args":["POLICY123", "John Doe", "100000", "ACTIVE"]}'
```


4. Envie a transação para consultar a apólice:
```
peer chaincode query \
  -C mychannel -n insurance-chaincode \
  --peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE \
  -c '{"function":"queryPolicy","Args":["POLICY123"]}'
```

5. Atualizar Status de Apólice:
   Para atualizar o status de uma apólice, você pode usar a função updatePolicyStatus. Supondo que você queira atualizar o status da apólice com o ID POLICY123 para Active, o comando seria:

   1. Defina as variáveis de ambiente para a organização que está invocando o chaincode. Vou usar a Org1 como exemplo:

	```
	export CORE_PEER_TLS_ENABLED=true
	export CORE_PEER_LOCALMSPID="Org1MSP"
	export CORE_PEER_TLS_ROOTCERT_FILE=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt
	export CORE_PEER_MSPCONFIGPATH=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp
	export CORE_PEER_ADDRESS=localhost:7051
	```

	2. Defina as variáveis de ambiente para o orderer:

	```
	export ORDERER_CA=/Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/ordererOrganizations/example.com/orderers/orderer.example.com/msp/tlscacerts/tlsca.example.com-cert.pem
	export ORDERER_ADDRESS=localhost:7050
	```

	3. Envie a transação para atualizar o status da apólice:

  	```
	peer chaincode invoke \
	-o $ORDERER_ADDRESS \
	--ordererTLSHostnameOverride orderer.example.com \
	--tls --cafile $ORDERER_CA \
	-C mychannel -n insurance-chaincode \
	--peerAddresses localhost:7051 --tlsRootCertFiles $CORE_PEER_TLS_ROOTCERT_FILE \
	--peerAddresses localhost:9051 --tlsRootCertFiles /Users/alex.prado/fabric/fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org2.example.com/peers/peer0.org2.example.com/tls/ca.crt \
	-c '{"function":"updatePolicyStatus","Args":["POLICY123", "Closed"]}'
  	```

Certifique-se de ajustar os argumentos da função createCar conforme necessário para sua aplicação específica.

Verifique o resultado: Após enviar a transação, você deve ver uma saída indicando que a transação foi enviada com sucesso. Você pode verificar o ledger para confirmar que a transação foi aplicada corretamente.
Se você precisar invocar uma função diferente ou usar outros argumentos, ajuste o comando -c conforme necessário.
