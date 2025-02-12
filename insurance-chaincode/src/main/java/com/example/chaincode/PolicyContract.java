package com.example.chaincode;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.example.chaincode.dto.InsuranceProductDTO;
import com.example.chaincode.dto.PassengerDTO;
import com.example.chaincode.dto.PolicyDTO;
import com.example.chaincode.dto.TravelInfoDTO;

@Contract(name = "PolicyContract", info = @Info(title = "Policy Management", description = "Smart Contract for managing insurance policies", version = "1.0.0"))
public class PolicyContract implements ContractInterface {

    private static final Logger logger = Logger.getLogger(PolicyContract.class.getName());

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void init(Context ctx) {
        logger.info("Chaincode initialized successfully.");
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createPolicy(Context ctx, String policyJson) {
        ChaincodeStub stub = ctx.getStub();
        String policyId = stub.getTxId();

        try {
            logger.info("Transaction ID: " + policyId + " | Request received: createPolicy");

            // Convert JSON string to PolicyDTO
            PolicyDTO policyDTO = PolicyDTO.fromJson(policyJson);

            // Criar TravelInfo
            TravelInfoDTO travelInfo = new TravelInfoDTO(policyDTO.getTravelInfo().getDestination(),
                    policyDTO.getTravelInfo().getStartDate(), policyDTO.getTravelInfo().getEndDate(),
                    policyDTO.getTravelInfo().getNumberOfPassengers());

            // Criar lista de passageiros
            List<PassengerDTO> passengers = new ArrayList<>();
            for (PassengerDTO passengerDTO : policyDTO.getPassengers()) {
                PassengerDTO passenger = new PassengerDTO(passengerDTO.getName(), passengerDTO.getDocument(),
                        passengerDTO.getBirthDate(), passengerDTO.getEmail(), passengerDTO.getPhone(),
                        passengerDTO.getAddress());
                passengers.add(passenger);
            }

            // Criar InsuranceProduct
            InsuranceProductDTO insuranceProduct = new InsuranceProductDTO(
                    policyDTO.getInsuranceProduct().getCoverageName(),
                    policyDTO.getInsuranceProduct().getInsuredAmount());

            // Criar e armazenar a apólice
            PolicyDTO policy = new PolicyDTO(policyId, policyDTO.getPolicyHolder(), policyDTO.getPremium(),
                    policyDTO.getPolicyStatus(), travelInfo, passengers, insuranceProduct);
            stub.putStringState(policyId, policy.toJson());
            logger.info("Transaction ID: " + policyId + " | Policy created: " + policy.toJson());

            // Retornar a chave composta como policyId
            return policyId;

        } catch (Exception e) {
            logger.severe("Transaction ID: " + policyId + " | Error in createPolicy: " + e.getMessage());
            throw new ChaincodeException("Unexpected error: " + e.getMessage(), "UNKNOWN_ERROR", e);
        }
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public PolicyDTO queryPolicy(Context ctx, String policyId) {
        ChaincodeStub stub = ctx.getStub();

        // Verificar se o policyId não é nulo ou vazio
        if (policyId == null || policyId.isEmpty()) {
            String errorMessage = "Policy ID cannot be null or empty.";
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "INVALID_POLICY_ID");
        }

        try {
            logger.info("Querying policy with ID: " + policyId);

            // Obter o estado da apólice do ledger
            String policyJson = stub.getStringState(policyId);

            // Verificar se a apólice existe
            if (policyJson == null || policyJson.isEmpty()) {
                String errorMessage = "Policy with ID " + policyId + " does not exist.";
                logger.severe(errorMessage);
                throw new ChaincodeException(errorMessage, "POLICY_NOT_FOUND");
            }

            // Converter JSON para PolicyDTO
            PolicyDTO policy = PolicyDTO.fromJson(policyJson);
            policy.setPolicyId(policyId);
            logger.info("Policy found: " + policy.toJson());
            return policy;

        } catch (Exception e) {
            String errorMessage = "Failed to query policy: " + e.getMessage();
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "QUERY_ERROR", e);
        }
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public PolicyDTO updatePolicyStatus(Context ctx, String policyId, String newStatus) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Updating policy status: policyId=" + policyId + ", newStatus=" + newStatus);

            String policyJson = stub.getStringState(policyId);
            if (policyJson == null || policyJson.isEmpty()) {
                throw new ChaincodeException("Policy with ID " + policyId + " does not exist.", "POLICY_NOT_FOUND");
            }

            PolicyDTO policy = PolicyDTO.fromJson(policyJson);
            policy.setPolicyStatus(newStatus);

            stub.putStringState(policyId, policy.toJson());
            logger.info("Policy updated: " + policy.toJson());

            return policy;

        } catch (Exception e) {
            logger.severe("Error in updatePolicyStatus: " + e.getMessage());
            throw new ChaincodeException("Failed to update policy: " + e.getMessage(), "UPDATE_ERROR", e);
        }
    }
}
