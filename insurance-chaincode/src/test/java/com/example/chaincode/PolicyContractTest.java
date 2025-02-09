package com.example.chaincode;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.Before;
import org.junit.Test;

public class PolicyContractTest {

    private PolicyContract contract;
    private Context ctx;
    private ChaincodeStub stub;

    @Before
    public void setUp() {
        contract = new PolicyContract();
        ctx = mock(Context.class);
        stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
    }

    @Test
    public void testCreatePolicy() {
        String policyId = "P001";
        Policy policy = new Policy(policyId, "John Doe", 500.0, "Active");

        when(stub.getStringState(policyId)).thenReturn(null);
        contract.createPolicy(ctx, policyId, policy.getHolderName(), policy.getPremium(), policy.getStatus());

        verify(stub).putStringState(eq(policyId), anyString());
    }

    @Test
    public void testQueryPolicy() {
        String policyId = "P001";
        Policy policy = new Policy(policyId, "John Doe", 500.0, "Active");

        when(stub.getStringState(policyId)).thenReturn(policy.toJson());
        Policy result = contract.queryPolicy(ctx, policyId);

        assertEquals(policyId, result.getPolicyId());
        assertEquals("John Doe", result.getHolderName());
        assertEquals(500.0, result.getPremium(), 0);
        assertEquals("Active", result.getStatus());
    }

    @Test
    public void testUpdatePolicyStatus() {
        String policyId = "P001";
        Policy policy = new Policy(policyId, "John Doe", 500.0, "Active");

        when(stub.getStringState(policyId)).thenReturn(policy.toJson());
        Policy updatedPolicy = contract.updatePolicyStatus(ctx, policyId, "Expired");

        assertEquals("Expired", updatedPolicy.getStatus());
        verify(stub).putStringState(eq(policyId), anyString());
    }

    @Test(expected = RuntimeException.class)
    public void testCreateDuplicatePolicy() {
        String policyId = "P001";
        Policy policy = new Policy(policyId, "John Doe", 500.0, "Active");

        when(stub.getStringState(policyId)).thenReturn(policy.toJson());
        contract.createPolicy(ctx, policyId, policy.getHolderName(), policy.getPremium(), policy.getStatus());
    }

    @Test(expected = RuntimeException.class)
    public void testQueryNonExistentPolicy() {
        String policyId = "P002";

        when(stub.getStringState(policyId)).thenReturn(null);
        contract.queryPolicy(ctx, policyId);
    }

    @Test
    public void testCreatePolicyWithValidInput() {
        String policyId = "P001";
        Policy policy = new Policy(policyId, "John Doe", 500.0, "Active");

        when(stub.getStringState(policyId)).thenReturn(null);
        contract.createPolicy(ctx, policyId, policy.getHolderName(), policy.getPremium(), policy.getStatus());

        verify(stub).putStringState(eq(policyId), eq(policy.toJson()));
    }

}
