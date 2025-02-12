package com.example.insurance.service;

import com.example.insurance.dto.PolicyDTO;
import com.example.insurance.dto.TravelInfoDTO;
import com.example.insurance.dto.InsuranceProductDTO;
import com.example.insurance.dto.PassengerDTO;
import org.hyperledger.fabric.gateway.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class PolicyService {

    private static final Logger logger = LoggerFactory.getLogger(PolicyService.class);

    private Gateway gateway;

    @Value("${wallet.path}")
    private String walletPath;

    @Value("${connection.profile.path}")
    private String connectionProfilePath;

    @Value("${credentials.path}")
    private String credentialsPath;

    private final WalletService walletService;

    public PolicyService(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostConstruct
    public void init() throws Exception {
        logger.info("Initializing PolicyService with walletPath: {}, connectionProfilePath: {}, credentialsPath: {}",
                walletPath, connectionProfilePath, credentialsPath);

        if (walletPath == null || connectionProfilePath == null || credentialsPath == null) {
            logger.error(
                    "One or more @Value fields are null. walletPath: {}, connectionProfilePath: {}, credentialsPath: {}",
                    walletPath, connectionProfilePath, credentialsPath);
            throw new IllegalArgumentException("One or more @Value fields are null.");
        }

        Path walletDir = Paths.get(walletPath);
        Wallet wallet = Wallets.newFileSystemWallet(walletDir);

        // Adicione a identidade admin à carteira
        walletService.addAdminIdentity(wallet);

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(Paths.get(connectionProfilePath)).discovery(true);
        this.gateway = builder.connect();
        logger.info("Connected to gateway");
    }

    public Map<String, String> createPolicy(PolicyDTO policyDTO) {
        try {
            logger.info("Request received: createPolicy");

            // Criar TravelInfo
            TravelInfoDTO travelInfo = new TravelInfoDTO(policyDTO.getTravelInfo().getDestination(),
                    policyDTO.getTravelInfo().getStartDate(), policyDTO.getTravelInfo().getEndDate(),
                    policyDTO.getTravelInfo().getNumberOfPassengers());

            // Criar lista de passageiros
            List<PassengerDTO> passengers = new ArrayList<>();
            if (policyDTO.getPassengers() != null) {
                for (PassengerDTO passengerDTO : policyDTO.getPassengers()) {
                    PassengerDTO passenger = new PassengerDTO(passengerDTO.getName(), passengerDTO.getDocument(),
                            passengerDTO.getBirthDate(), passengerDTO.getEmail(), passengerDTO.getPhone(),
                            passengerDTO.getAddress());
                    passengers.add(passenger);
                }
            }

            // Criar InsuranceProduct
            InsuranceProductDTO insuranceProduct = new InsuranceProductDTO(
                    policyDTO.getInsuranceProduct().getCoverageName(),
                    policyDTO.getInsuranceProduct().getInsuredAmount());

            // Criar e armazenar a apólice
            PolicyDTO policy = new PolicyDTO(null, policyDTO.getPolicyHolder(), policyDTO.getPremium(), travelInfo,
                    passengers, insuranceProduct);

            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("insurance-chaincode");
            byte[] result = contract.submitTransaction("createPolicy", policy.toJson());

            String policyId = new String(result);
            logger.info("Policy created with ID: " + policyId);

            Map<String, String> response = new HashMap<>();
            response.put("policyId", policyId);
            return response;

        } catch (Exception e) {
            logger.error("Error in createPolicy: " + e.getMessage());
            throw new RuntimeException("Unexpected error: " + e.getMessage(), e);
        }
    }

    public PolicyDTO queryPolicy(String policyId) throws Exception {
        logger.info("Querying policy with ID: {}", policyId);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.evaluateTransaction("queryPolicy", policyId);
        String response = new String(result);
        logger.info("Queried policy with ID: {}, response: {}", policyId, response);
        return PolicyDTO.fromJson(response);
    }

    public PolicyDTO updatePolicyStatus(String policyId, String newStatus) throws Exception {
        logger.info("Updating policy status with ID: {} to {}", policyId, newStatus);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result;
        try {
            result = contract.submitTransaction("updatePolicyStatus", policyId, newStatus);
        } catch (ContractException | TimeoutException | InterruptedException e) {
            logger.error("Error updating policy status: {}", e.getMessage());
            throw new RuntimeException("Error updating policy status", e);
        }
        String response = new String(result);
        logger.info("Updated policy status with ID: {}, response: {}", policyId, response);
        return PolicyDTO.fromJson(response);
    }
}