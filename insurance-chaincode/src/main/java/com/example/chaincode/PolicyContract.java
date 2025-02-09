package com.example.chaincode;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.logging.Logger;

@Contract(name = "PolicyContract", info = @Info(title = "Policy Management", description = "Smart Contract for managing insurance policies", version = "1.0.0"))
public class PolicyContract implements ContractInterface {

    private static final Logger logger = Logger.getLogger(PolicyContract.class.getName());

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void init(Context ctx) {
        logger.info("Chaincode initialized successfully.");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Policy createPolicy(Context ctx, String policyId, String holderName, double premium, String status) {
        ChaincodeStub stub = ctx.getStub();
        String txId = stub.getTxId();

        try {
            logger.info("Transaction ID: " + txId + " | Request received: createPolicy with policyId=" + policyId);

            // Validações
            validateInputs(policyId, holderName, premium, status);

            // Verificar se já existe
            String existingPolicy = stub.getStringState(policyId);
            if (existingPolicy != null && !existingPolicy.isEmpty()) {
                throw new ChaincodeException("Policy with ID " + policyId + " already exists.", "POLICY_EXISTS");
            }

            // Criar e armazenar a apólice
            Policy policy = new Policy(policyId, holderName, premium, status);
            stub.putStringState(policyId, policy.toJson());
            logger.info("Transaction ID: " + txId + " | Policy created: " + policy.toJson());

            return policy;

        } catch (Exception e) {
            logger.severe("Transaction ID: " + txId + " | Error in createPolicy: " + e.getMessage());
            throw new ChaincodeException("Unexpected error: " + e.getMessage(), "UNKNOWN_ERROR", e);
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public Policy queryPolicy(Context ctx, String policyId) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Querying policy with ID: " + policyId);

            String policyJson = stub.getStringState(policyId);
            if (policyJson == null || policyJson.isEmpty()) {
                throw new ChaincodeException("Policy with ID " + policyId + " does not exist.", "POLICY_NOT_FOUND");
            }

            Policy policy = Policy.fromJson(policyJson);
            logger.info("Policy found: " + policy.toJson());
            return policy;

        } catch (Exception e) {
            logger.severe("Error in queryPolicy: " + e.getMessage());
            throw new ChaincodeException("Failed to query policy: " + e.getMessage(), "QUERY_ERROR", e);
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public Policy updatePolicyStatus(Context ctx, String policyId, String newStatus) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Updating policy status: policyId=" + policyId + ", newStatus=" + newStatus);

            String policyJson = stub.getStringState(policyId);
            if (policyJson == null || policyJson.isEmpty()) {
                throw new ChaincodeException("Policy with ID " + policyId + " does not exist.", "POLICY_NOT_FOUND");
            }

            Policy policy = Policy.fromJson(policyJson);
            policy.setStatus(newStatus);

            stub.putStringState(policyId, policy.toJson());
            logger.info("Policy updated: " + policy.toJson());

            return policy;

        } catch (Exception e) {
            logger.severe("Error in updatePolicyStatus: " + e.getMessage());
            throw new ChaincodeException("Failed to update policy: " + e.getMessage(), "UPDATE_ERROR", e);
        }
    }

    private void validateInputs(String policyId, String holderName, double premium, String status) {
        if (policyId == null || policyId.isEmpty()) {
            throw new ChaincodeException("Policy ID cannot be null or empty", "INVALID_POLICY_ID");
        }
        if (holderName == null || holderName.isEmpty()) {
            throw new ChaincodeException("Holder Name cannot be null or empty", "INVALID_HOLDER_NAME");
        }
        if (premium <= 0) {
            throw new ChaincodeException("Premium must be greater than 0", "INVALID_PREMIUM");
        }
        if (status == null || status.isEmpty()) {
            throw new ChaincodeException("Status cannot be null or empty", "INVALID_STATUS");
        }
    }
}
