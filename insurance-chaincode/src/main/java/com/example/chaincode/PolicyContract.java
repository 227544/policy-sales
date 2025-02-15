package com.example.chaincode;

import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.example.models.InsuranceProduct;
import com.example.models.Passenger;
import com.example.models.Policy;
import com.example.models.Refund;
import com.example.models.TravelInfo;
import com.google.gson.Gson;

@Contract(name = "PolicyContract", info = @Info(title = "Policy Management", description = "Smart Contract for managing insurance policies", version = "1.0.0"))
public class PolicyContract implements ContractInterface {

    private static final Logger logger = Logger.getLogger(PolicyContract.class.getName());
    private final EligibilityContract eligibilityContract = new EligibilityContract();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void init(Context ctx) {
        logger.info("Chaincode initialized successfully.");
    }

    // Create a new policy
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String createPolicy(Context ctx, String policyJson) {
        ChaincodeStub stub = ctx.getStub();
        String policyId = stub.getTxId();

        try {
            logger.info("Transaction ID: " + policyId + " | Request received: createPolicy");

            // Convert JSON string to Policy
            Policy policy = Policy.fromJson(policyJson);

            // Validate policy creation
            eligibilityContract.validatePolicyCreation(policy);

            // Create TravelInfo
            TravelInfo travelInfo = new TravelInfo(policy.getTravelInfo().getDestination(),
                    policy.getTravelInfo().getStartDate(), policy.getTravelInfo().getEndDate(),
                    policy.getTravelInfo().getNumberOfPassengers());

            // Create passengers list
            List<Passenger> passengers = new ArrayList<>();
            for (Passenger passenger : policy.getPassengers()) {
                Passenger newPassenger = new Passenger(passenger.getName(), passenger.getDocument(),
                        passenger.getBirthDate(), passenger.getEmail(), passenger.getPhone(), passenger.getAddress());
                passengers.add(newPassenger);
            }

            // Create InsuranceProduct
            InsuranceProduct insuranceProduct = new InsuranceProduct(policy.getInsuranceProduct().getCoverageName(),
                    policy.getInsuranceProduct().getInsuredAmount());

            // Create and save a policy
            Policy newPolicy = new Policy(policyId, policy.getPolicyHolder(), policy.getDocumentId(),
                    policy.getPremium(), "ACTIVE", travelInfo, passengers, insuranceProduct);
            stub.putStringState(policyId, newPolicy.toJson());
            logger.info("Transaction ID: " + policyId + " | Policy created: " + newPolicy.toJson());

            // Return the TxId as policyId
            return policyId;

        } catch (Exception e) {
            logger.severe("Transaction ID: " + policyId + " | Error in createPolicy: " + e.getMessage());
            throw new ChaincodeException("Unexpected error: " + e.getMessage(), "UNKNOWN_ERROR", e);
        }
    }

    // Query policy by policyId
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryPolicy(Context ctx, String policyId) {
        ChaincodeStub stub = ctx.getStub();

        // Validation of policyId for null or empty
        if (policyId == null || policyId.isEmpty()) {
            String errorMessage = "Policy ID cannot be null or empty.";
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "INVALID_POLICY_ID");
        }

        try {
            logger.info("Querying policy with ID: " + policyId);

            // Get policy state from ledger
            String policyJson = stub.getStringState(policyId);

            // Verify if policy exists
            if (policyJson == null || policyJson.isEmpty()) {
                String errorMessage = "Policy with ID " + policyId + " does not exist.";
                logger.severe(errorMessage);
                throw new ChaincodeException(errorMessage, "POLICY_NOT_FOUND");
            }

            logger.info("Policy found: " + policyJson);
            return policyJson;

        } catch (Exception e) {
            String errorMessage = "Failed to query policy: " + e.getMessage();
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "QUERY_ERROR", e);
        }
    }

    // List all policy IDs
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryAllPolicies(Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Querying all policy IDs");

            List<String> policyIds = new ArrayList<>();
            QueryResultsIterator<KeyValue> results = stub.getStateByRange("", "");

            for (KeyValue result : results) {
                String policyJson = result.getStringValue();
                if (policyJson != null && !policyJson.isEmpty()) {
                    policyIds.add(result.getKey());
                }
            }

            logger.info("All policy IDs found: " + policyIds.size());
            return new Gson().toJson(policyIds);

        } catch (Exception e) {
            String errorMessage = "Failed to query all policy IDs: " + e.getMessage();
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "QUERY_ERROR", e);
        }
    }

    // Find all policies from policyHolder
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String queryByHolderdocumentId(Context ctx, String documentId) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Querying policies by holder document Id: " + documentId);

            List<String> policiesJson = new ArrayList<>();
            QueryResultsIterator<KeyValue> results = stub
                    .getQueryResult("{\"selector\":{\"documentId\":\"" + documentId + "\"}}");

            for (KeyValue result : results) {
                String policyJson = result.getStringValue();
                if (policyJson != null && !policyJson.isEmpty()) {
                    policiesJson.add(policyJson);
                }
            }

            logger.info("Policies found for policyHolder " + documentId + ": " + policiesJson.size());
            return policiesJson.toString();

        } catch (Exception e) {
            String errorMessage = "Failed to query policies by policyHolder: " + e.getMessage();
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "QUERY_ERROR", e);
        }
    }

    // Update policy status
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String updatePolicyStatus(Context ctx, String policyId, String newStatus) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Updating policy status: policyId=" + policyId + ", newStatus=" + newStatus);

            String policyJson = stub.getStringState(policyId);
            if (policyJson == null || policyJson.isEmpty()) {
                String errorMessage = "Policy with ID " + policyId + " does not exist.";
                logger.severe(errorMessage);
                throw new ChaincodeException(errorMessage, "POLICY_NOT_FOUND");
            }

            Policy policy = Policy.fromJson(policyJson);

            // Validate policy update
            eligibilityContract.validatePolicyUpdate(policy, newStatus);

            policy.setPolicyStatus(newStatus);

            stub.putStringState(policyId, policy.toJson());
            logger.info("Policy updated: " + policy.toJson());

            return policy.toJson();

        } catch (Exception e) {
            logger.severe("Error in updatePolicyStatus: " + e.getMessage());
            throw new ChaincodeException("Failed to update policy: " + e.getMessage(), "UPDATE_ERROR", e);
        }
    }

    // Cancel policy and process refund
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String cancelPolicy(Context ctx, String policyId) {
        ChaincodeStub stub = ctx.getStub();

        try {
            logger.info("Cancelling policy with ID: " + policyId);

            String policyJson = stub.getStringState(policyId);
            if (policyJson == null || policyJson.isEmpty()) {
                String errorMessage = "Policy not found: " + policyId;
                logger.severe(errorMessage);
                throw new ChaincodeException(errorMessage, "POLICY_NOT_FOUND");
            }

            Policy policy = Policy.fromJson(policyJson);

            // Validate policy cancellation
            eligibilityContract.validatePolicyCancellation(policy);

            policy.setPolicyStatus(Policy.PolicyStatus.CANCELLED.name());

            // Calculate refund amount
            double refundAmount = calculateRefund(policy);

            // Create Refund
            Refund refund = new Refund(policyId, policy.getPolicyHolder(), refundAmount, policy.getPolicyStatus());

            // Save the updated policy state
            stub.putStringState(policyId, policy.toJson());

            logger.info("Policy cancelled with ID: " + policyId + ", refund amount: " + refundAmount);
            return refund.toJson();

        } catch (Exception e) {
            String errorMessage = "Failed to cancel policy: " + e.getMessage();
            logger.severe(errorMessage);
            throw new ChaincodeException(errorMessage, "CANCEL_POLICY_ERROR", e);
        }
    }

    // Calculate refund amount
    private double calculateRefund(Policy policy) {
        double premium = policy.getPremium();
        String startDateStr = policy.getTravelInfo().getStartDate();
        long startDateMillis = Long.parseLong(startDateStr);
        Date startDate = new Date(startDateMillis);

        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate cancelDate = LocalDate.now();

        if (cancelDate.isBefore(startLocalDate.minusDays(7))) {
            return premium * 0.8;
        } else {
            return 0.0;
        }
    }
}
