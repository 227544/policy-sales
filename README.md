# Insurance Policy Sales

This document describes the project structure for the `policy-sales` repository. A blockchain-based solution for selling insurance policies.

## Main Project Structure

This repository is organized as follows:

- **insurance-chaincode/**: Contains the smart contract code for the insurance application.
  - **PolicyContract.java**: Main class of the smart contract that defines methods for managing insurance policies.
    - `createPolicy`: Creates a new insurance policy.
    - `queryPolicy`: Queries an existing insurance policy by its ID.
    - `queryAllPolicies`: Queries all existing insurance policy IDs.
    - `queryByHolderDocumentId`: Queries all policies of a primary insured by their ID.
    - `updatePolicyStatus`: Updates the status of an existing insurance policy.
    - `cancelPolicy`: Cancels a policy and calculates the refund amount.
- **insurance-sales-api/**: Contains the insurance sales API.
  - **src/**: Contains the main source code of the project.
    - **config/**: Contains configurations for using Swagger.
    - **controllers/**: Contains the controllers that handle HTTP requests.
    - **dto/**: Contains definitions for transferring data between client and server.
    - **services/**: Contains business logic and services.
- **scripts/**: Contains useful scripts for project automation and maintenance.

## Configuration Instructions

1. Clone the repository:

   ```bash
   git clone https://github.com/227544/policy-sales.git
   ```

2. Check the prerequisites as described in the file [`insurance-chaincode/README.md`](insurance-chaincode/README.md).

## Running the Project

To start the project, follow these steps:

1. Run the script [`scripts/setupNetwork.sh`](scripts/setupNetwork.sh) to set up the blockchain network:

   ```bash
   ./scripts/setupNetwork.sh up
   ```

   The script setupNetwork.sh sets up the necessary blockchain network for the application, including creating channels, installing, and instantiating the insurance-chaincode. The script also builds and deploys the insurance-sales-api in a Docker container, allowing execution via REST API.

   Access the insurance-sales-api environment through the following link: [http://localhost:8080/](http://localhost:8080/)

2. Run the script [`scripts/setuptExplorer.sh`](scripts/setupExplorer.sh) to set up the Hyperledger Explorer:

   ```bash
   ./scripts/setupExplorer.sh up
   ```

   The script setupExplorer.sh sets up the Hyperledger Explorer that is open-source utility to browse activity on the underlying blockchain network.

   Access the Hyperledger Explorer through the following link: [[http://localhost:8081/](http://localhost:8081/)

    use the following credentials:
    "user": "exploreradmin"
    "password": "exploreradminpw"

### Clean up

To stop services, run the following:

   ```bash
   ./scripts/setupNetwork.sh down
   ```

   ```bash
   ./scripts/setupExplorer.sh down
   ```

## License

This project is licensed under the MIT License. See the file [`LICENSE`](LICENSE) for more details.
