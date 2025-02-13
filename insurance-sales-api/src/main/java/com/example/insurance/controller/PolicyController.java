package com.example.insurance.controller;

import com.example.insurance.dto.CreatePolicyDTO;
import com.example.insurance.dto.PolicyDTO;
import com.example.insurance.dto.RefundDTO;
import com.example.insurance.dto.UpdatePolicyStatusDTO;
import com.example.insurance.dto.CancelPolicyDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
                        @ApiResponse(responseCode = "200", description = "Successfully created policy", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyHolder\": \"John Doe\", \"documentId\": \"123456789\", \"premium\": 100.0, \"travelInfo\": { \"destination\": \"Paris\", \"startDate\": \"2025-01-01\", \"endDate\": \"2025-01-10\", \"numberOfPassengers\": 2 }, \"insuranceProduct\": { \"coverageName\": \"Travel Insurance\", \"insuredAmount\": 50000.0 }, \"passengers\": [ { \"name\": \"Jane Doe\", \"document\": \"123456789\", \"birthDate\": \"1990-01-01\", \"email\": \"jane.doe@example.com\", \"phone\": \"1234567890\", \"address\": \"123 Main St\" } ] }"))),
                        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Invalid input data\" }"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @PostMapping("/create")
        public ResponseEntity<Map<String, String>> createPolicy(@RequestBody CreatePolicyDTO createPolicyDTO)
                        throws Exception {
                logger.info("Received request to create policy for holder: {}", createPolicyDTO.getPolicyHolder());
                Map<String, String> response = policyService.createPolicy(createPolicyDTO);
                logger.info("Response for create policy for holder {}: {}", createPolicyDTO.getPolicyHolder(),
                                response);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Query a policy by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully queried policy", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyId\": \"12345\", \"policyHolder\": \"John Doe\", \"premium\": 100.0, \"travelInfo\": { \"destination\": \"Paris\", \"startDate\": \"2025-01-01\", \"endDate\": \"2025-01-10\", \"numberOfPassengers\": 2 }, \"insuranceProduct\": { \"coverageName\": \"Travel Insurance\", \"insuredAmount\": 50000.0 }, \"passengers\": [ { \"name\": \"Jane Doe\", \"document\": \"123456789\", \"birthDate\": \"1990-01-01\", \"email\": \"jane.doe@example.com\", \"phone\": \"1234567890\", \"address\": \"123 Main St\" } ] }"))),
                        @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Policy not found\" }"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @GetMapping("/{policyId}")
        public ResponseEntity<PolicyDTO> queryPolicy(
                        @Parameter(description = "ID of the policy to query", required = true) @PathVariable String policyId)
                        throws Exception {
                logger.info("Received request to query policy with ID: {}", policyId);
                PolicyDTO response = policyService.queryPolicy(policyId);
                logger.info("Response for query policy with ID {}: {}", policyId, response);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Query all policies")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully queried all policies", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[\"12345\", \"67890\"]"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @GetMapping("/findAll")
        public ResponseEntity<List<String>> queryAllPolicies() throws Exception {
                logger.info("Received request to query all policies");
                List<String> response = policyService.queryAllPolicies();
                logger.info("Response for query all policies: {}", response);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Query policies by policy holder document Id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully queried policies by policy holder document Id", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[{ \"policyId\": \"12345\", \"policyHolder\": \"John Doe\", \"premium\": 100.0, \"travelInfo\": { \"destination\": \"Paris\", \"startDate\": \"2025-01-01\", \"endDate\": \"2025-01-10\", \"numberOfPassengers\": 2 }, \"insuranceProduct\": { \"coverageName\": \"Travel Insurance\", \"insuredAmount\": 50000.0 }, \"passengers\": [ { \"name\": \"Jane Doe\", \"document\": \"123456789\", \"birthDate\": \"1990-01-01\", \"email\": \"jane.doe@example.com\", \"phone\": \"1234567890\", \"address\": \"123 Main St\" } ] }]"))),
                        @ApiResponse(responseCode = "404", description = "Policy holder document Id not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Policy holder not found\" }"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @GetMapping("/holder/{documentId}")
        public ResponseEntity<List<PolicyDTO>> queryByHolderdocumentId(
                        @Parameter(description = "Policy holder document Id to query", required = true) @PathVariable String documentId)
                        throws Exception {
                logger.info("Received request to query policies by policy holder: {}", documentId);
                List<PolicyDTO> response = policyService.queryByHolderdocumentId(documentId);
                logger.info("Response for query policies by policy holder {}: {}", documentId, response);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Update the status of a policy")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully updated policy status", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"policyId\": \"12345\", \"newStatus\": \"Active\" }"))),
                        @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Policy not found\" }"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @PutMapping("/{policyId}/status")
        public ResponseEntity<PolicyDTO> updatePolicyStatus(
                        @Parameter(description = "ID of the policy to update", required = true) @PathVariable String policyId,
                        @Parameter(description = "New status of the policy", required = true) @RequestParam String newStatus)
                        throws Exception {
                logger.info("Received request to update policy status with ID: {} to {}", policyId, newStatus);
                UpdatePolicyStatusDTO updatePolicyStatusDTO = new UpdatePolicyStatusDTO(policyId, newStatus);
                PolicyDTO response = policyService.updatePolicyStatus(updatePolicyStatusDTO);
                logger.info("Response for update policy status with ID {}: {}", policyId, response);
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Cancel a policy and process refund")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully cancelled policy and processed refund", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"refundId\": \"refund_12345\", \"policyId\": \"12345\", \"policyHolder\": \"John Doe\", \"refundAmount\": 80.0 }"))),
                        @ApiResponse(responseCode = "404", description = "Policy not found", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Policy not found\" }"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{ \"error\": \"Internal server error\" }"))) })
        @PostMapping("/cancel/{policyId}")
        public ResponseEntity<RefundDTO> cancelPolicy(
                        @Parameter(description = "ID of the policy to cancel", required = true) @PathVariable String policyId)
                        throws Exception {
                logger.info("Received request to cancel policy with ID: {}", policyId);
                CancelPolicyDTO cancelPolicyDTO = new CancelPolicyDTO(policyId);
                RefundDTO response = policyService.cancelPolicy(cancelPolicyDTO);
                logger.info("Response for cancel policy with ID {}: {}", policyId, response);
                return ResponseEntity.ok(response);
        }
}