package com.example.chaincode;

import com.example.models.Policy;
import org.hyperledger.fabric.shim.ChaincodeException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class EligibilityContract {

    private static final Logger logger = Logger.getLogger(EligibilityContract.class.getName());

    public void validatePolicyCreation(Policy policy) {
        String startDateStr = policy.getTravelInfo().getStartDate();
        String endDateStr = policy.getTravelInfo().getEndDate();
        String destination = policy.getTravelInfo().getDestination();
        int numberOfPassengers = policy.getTravelInfo().getNumberOfPassengers();

        logger.info("Validating policy creation...");

        if (startDateStr == null || endDateStr == null) {
            logger.severe("Start date and end date are required.");
            throw new ChaincodeException("Start date and end date are required.", "INVALID_DATES");
        }

        LocalDate startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today) || startDate.isEqual(today)) {
            logger.severe("Start date must be greater than today.");
            throw new ChaincodeException("Start date must be greater than today.", "INVALID_START_DATE");
        }

        if (endDate.isBefore(startDate) || endDate.isEqual(startDate)) {
            logger.severe("End date must be greater than start date.");
            throw new ChaincodeException("End date must be greater than start date.", "INVALID_END_DATE");
        }

        if (endDate.isBefore(startDate.plusDays(1))) {
            logger.severe("The duration between start date and end date must be greater than 1 day.");
            throw new ChaincodeException("The duration between start date and end date must be greater than 1 day.",
                    "INVALID_DURATION");
        }

        if (destination == null || destination.isEmpty()) {
            logger.severe("Destination is required.");
            throw new ChaincodeException("Destination is required.", "INVALID_DESTINATION");
        }

        if (numberOfPassengers < 1) {
            logger.severe("Number of passengers must be at least 1.");
            throw new ChaincodeException("Number of passengers must be at least 1.", "INVALID_PASSENGERS");
        }

        logger.info("Policy creation validated successfully.");
    }

    public void validatePolicyUpdate(Policy policy, String newStatus) {
        logger.info("Validating policy update...");

        if (newStatus == null || newStatus.isEmpty()) {
            logger.severe("New status cannot be null or empty.");
            throw new ChaincodeException("New status cannot be null or empty.", "INVALID_STATUS");
        }

        if (policy.getPolicyStatus().equals(Policy.PolicyStatus.CLOSED.name())
                || policy.getPolicyStatus().equals(Policy.PolicyStatus.CANCELLED.name())) {
            logger.severe("Cannot update policy with status CLOSED or CANCELLED.");
            throw new ChaincodeException("Cannot update policy with status CLOSED or CANCELLED.",
                    "INVALID_POLICY_STATUS");
        }

        // Additional business rules for policy update can be added here

        logger.info("Policy update validated successfully.");
    }

    public void validatePolicyCancellation(Policy policy) {
        logger.info("Validating policy cancellation...");

        if (!policy.getPolicyStatus().equals(Policy.PolicyStatus.ACTIVE.name())) {
            logger.severe("Only active policies can be cancelled.");
            throw new ChaincodeException("Only active policies can be cancelled.", "INVALID_POLICY_STATUS");
        }

        // Additional business rules for policy cancellation can be added here

        logger.info("Policy cancellation validated successfully.");
    }
}