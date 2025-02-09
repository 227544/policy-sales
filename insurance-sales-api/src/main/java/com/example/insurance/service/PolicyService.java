package com.example.insurance.service;

import org.hyperledger.fabric.gateway.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        builder.identity(wallet, "admin").networkConfig(Paths.get(connectionProfilePath))
                .discovery(true);
        this.gateway = builder.connect();
        logger.info("Connected to gateway");
    }

    public String createPolicy(String policyId, String holderName, double premium, String status) throws Exception {
        logger.info("Creating policy with ID: {}, holderName: {}, premium: {}, status: {}", policyId, holderName,
                premium, status);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.submitTransaction("createPolicy", policyId, holderName, String.valueOf(premium),
                status);
        String response = new String(result);
        logger.info("Created policy with ID: {}, response: {}", policyId, response);
        return response;
    }

    public String queryPolicy(String policyId) throws Exception {
        logger.info("Querying policy with ID: {}", policyId);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.evaluateTransaction("queryPolicy", policyId);
        String response = new String(result);
        logger.info("Queried policy with ID: {}, response: {}", policyId, response);
        return response;
    }

    public String updatePolicyStatus(String policyId, String newStatus) throws Exception {
        logger.info("Updating policy status with ID: {} to {}", policyId, newStatus);
        Network network = gateway.getNetwork("mychannel");
        Contract contract = network.getContract("insurance-chaincode");

        byte[] result = contract.submitTransaction("updatePolicyStatus", policyId, newStatus);
        String response = new String(result);
        logger.info("Updated policy status with ID: {}, response: {}", policyId, response);
        return response;
    }
}