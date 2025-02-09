package com.example.insurance.controller;

import com.example.insurance.service.PolicyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policies")
@Api(value = "Policy Management System", description = "Policies operations for API")
public class PolicyController {

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    private final PolicyService policyService;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @ApiOperation(value = "Create a new policy", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully created policy"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/create")
    public String createPolicy(
            @ApiParam(value = "ID of the policy", required = true) @RequestParam String policyId,
            @ApiParam(value = "Name of the policy holder", required = true) @RequestParam String holderName,
            @ApiParam(value = "Premium amount", required = true) @RequestParam double premium,
            @ApiParam(value = "Status of the policy", required = true) @RequestParam String status) throws Exception {
        logger.info("Received request to create policy with ID: {}", policyId);
        String response = policyService.createPolicy(policyId, holderName, premium, status);
        logger.info("Response for create policy with ID {}: {}", policyId, response);
        return response;
    }

    @ApiOperation(value = "Query a policy by ID", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully queried policy"),
            @ApiResponse(code = 404, message = "Policy not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/query")
    public String queryPolicy(
            @ApiParam(value = "ID of the policy to query", required = true) @RequestParam String policyId)
            throws Exception {
        logger.info("Received request to query policy with ID: {}", policyId);
        String response = policyService.queryPolicy(policyId);
        logger.info("Response for query policy with ID {}: {}", policyId, response);
        return response;
    }

    @ApiOperation(value = "Update the status of a policy", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated policy status"),
            @ApiResponse(code = 404, message = "Policy not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping("/updateStatus")
    public String updatePolicyStatus(
            @ApiParam(value = "ID of the policy to update", required = true) @RequestParam String policyId,
            @ApiParam(value = "New status of the policy", required = true) @RequestParam String newStatus)
            throws Exception {
        logger.info("Received request to update policy status with ID: {} to {}", policyId, newStatus);
        String response = policyService.updatePolicyStatus(policyId, newStatus);
        logger.info("Response for update policy status with ID {}: {}", policyId, response);
        return response;
    }
}