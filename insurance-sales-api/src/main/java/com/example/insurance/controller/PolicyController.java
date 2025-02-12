package com.example.insurance.controller;

import com.example.insurance.dto.PolicyDTO;
import com.example.insurance.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
@Validated
public class PolicyController {

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @Operation(summary = "Create a new policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created policy", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyHolder\": \"John Doe\", \"premium\": 100.0, \"travelInfo\": { \"destination\": \"Paris\", \"startDate\": \"2025-01-01\", \"endDate\": \"2025-01-10\", \"numberOfPassengers\": 2 }, \"insuranceProduct\": { \"coverageName\": \"Travel Insurance\", \"insuredAmount\": 50000.0 }, \"passengers\": [ { \"name\": \"Jane Doe\", \"document\": \"123456789\", \"birthDate\": \"1990-01-01\", \"email\": \"jane.doe@example.com\", \"phone\": \"1234567890\", \"address\": \"123 Main St\" } ] }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @PostMapping("/create")
    public Map<String, String> createPolicy(@RequestBody PolicyDTO policyDTO) throws Exception {
        logger.info("Received request to create policy for holder: {}", policyDTO.getPolicyHolder());
        Map<String, String> response = policyService.createPolicy(policyDTO);
        logger.info("Response for create policy for holder {}: {}", policyDTO.getPolicyHolder(), response);
        return response;
    }

    @Operation(summary = "Query a policy by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully queried policy", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyId\": \"12345\", \"policyHolder\": \"John Doe\", \"premium\": 100.0, \"travelInfo\": { \"destination\": \"Paris\", \"startDate\": \"2025-01-01\", \"endDate\": \"2025-01-10\", \"numberOfPassengers\": 2 }, \"insuranceProduct\": { \"coverageName\": \"Travel Insurance\", \"insuredAmount\": 50000.0 }, \"passengers\": [ { \"name\": \"Jane Doe\", \"document\": \"123456789\", \"birthDate\": \"1990-01-01\", \"email\": \"jane.doe@example.com\", \"phone\": \"1234567890\", \"address\": \"123 Main St\" } ] }"))),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @GetMapping("/query")
    public PolicyDTO queryPolicy(
            @Parameter(description = "ID of the policy to query", required = true) @RequestParam String policyId)
            throws Exception {
        logger.info("Received request to query policy with ID: {}", policyId);
        PolicyDTO response = policyService.queryPolicy(policyId);
        logger.info("Response for query policy with ID {}: {}", policyId, response);
        return response;
    }

    @Operation(summary = "Update the status of a policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated policy status", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyId\": \"12345\", \"newStatus\": \"Active\" }"))),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error") })
    @PostMapping("/updateStatus")
    public PolicyDTO updatePolicyStatus(
            @Parameter(description = "ID of the policy to update", required = true) @RequestParam String policyId,
            @Parameter(description = "New status of the policy", required = true) @RequestParam String newStatus)
            throws Exception {
        logger.info("Received request to update policy status with ID: {} to {}", policyId, newStatus);
        PolicyDTO response = policyService.updatePolicyStatus(policyId, newStatus);
        logger.info("Response for update policy status with ID {}: {}", policyId, response);
        return response;
    }
}