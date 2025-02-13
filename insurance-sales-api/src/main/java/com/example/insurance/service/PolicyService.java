package com.example.insurance.service;

import com.example.insurance.dto.CreatePolicyDTO;
import com.example.insurance.dto.PolicyDTO;
import com.example.insurance.dto.RefundDTO;
import com.example.insurance.dto.UpdatePolicyStatusDTO;
import com.example.insurance.dto.CancelPolicyDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    public Map<String, String> createPolicy(CreatePolicyDTO createPolicyDTO) {
        try {
            logger.info("Request received: createPolicy");

            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("insurance-chaincode");
            byte[] result = contract.submitTransaction("createPolicy", createPolicyDTO.toJson());

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

    public List<String> queryAllPolicies() throws Exception {
        logger.info("Querying all policy IDs");
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.evaluateTransaction("queryAllPolicies");
        String response = new String(result);
        logger.info("Queried all policy IDs, response: {}", response);

        // Verifique se a resposta não é nula ou vazia
        if (response == null || response.isEmpty()) {
            logger.warn("No policy IDs found");
            return new ArrayList<>();
        }

        return new ObjectMapper().readValue(response, new TypeReference<List<String>>() {
        });
    }

    public List<PolicyDTO> queryByHolderdocumentId(String documentId) throws Exception {
        logger.info("Querying policies by holder documentId: {}", documentId);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.evaluateTransaction("queryByHolderdocumentId", documentId);
        String response = new String(result);
        logger.info("Queried policies by holder documentId: {}, response: {}", documentId, response);
        return PolicyDTO.fromJsonList(response);
    }

    public PolicyDTO updatePolicyStatus(UpdatePolicyStatusDTO updatePolicyStatusDTO) throws Exception {
        logger.info("Updating policy status with ID: {} to {}", updatePolicyStatusDTO.getPolicyId(),
                updatePolicyStatusDTO.getNewStatus());
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result;
        try {
            result = contract.submitTransaction("updatePolicyStatus", updatePolicyStatusDTO.getPolicyId(),
                    updatePolicyStatusDTO.getNewStatus());
        } catch (ContractException | TimeoutException | InterruptedException e) {
            logger.error("Error updating policy status: {}", e.getMessage());
            throw new RuntimeException("Error updating policy status", e);
        }
        String response = new String(result);
        logger.info("Updated policy status with ID: {}, response: {}", updatePolicyStatusDTO.getPolicyId(), response);
        return PolicyDTO.fromJson(response);
    }

    public RefundDTO cancelPolicy(CancelPolicyDTO cancelPolicyDTO) throws Exception {
        logger.info("Cancelling policy with ID: {}", cancelPolicyDTO.getPolicyId());
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result;
        try {
            result = contract.submitTransaction("cancelPolicy", cancelPolicyDTO.getPolicyId());
        } catch (ContractException | TimeoutException | InterruptedException e) {
            logger.error("Error cancelling policy: {}", e.getMessage());
            throw new RuntimeException("Error cancelling policy", e);
        }
        String response = new String(result);
        logger.info("Cancelled policy with ID: {}, response: {}", cancelPolicyDTO.getPolicyId(), response);
        return RefundDTO.fromJson(response);
    }
}