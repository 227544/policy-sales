package com.example.chaincode;

import org.hyperledger.fabric.contract.ContractRouter;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;

@Default
@Contract(name = "InsuranceChaincode")
public class InsuranceChaincode extends PolicyContract {
    public static void main(String[] args) {
        ContractRouter.main(args);
    }

}