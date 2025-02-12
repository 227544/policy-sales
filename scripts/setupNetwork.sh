#!/bin/bash

SRC_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
FABRIC_VERSION="2.2.3"
CA_VERSION="1.5.2"

function networkUp() {
	networkDown
  build_chaincode
  cd $SRC_DIR/..
  curl -sSL https://bit.ly/2ysbOFE | bash -s -- ${FABRIC_VERSION} ${CA_VERSION}
	cd fabric-samples
	if [ "${FABRIC_VERSION}" == "2.2.3" ]; then
		git checkout release-2.2
	fi
	cd test-network
	./network.sh up createChannel -ca -c mychannel -s couchdb
	./network.sh deployCC -ccn insurance-chaincode -ccv 1 -ccl java -ccp "$SRC_DIR/../insurance-chaincode"
  build_and_run_api
}

function networkDown() {
	if [[ -d $SRC_DIR/../fabric-samples/test-network ]]; then
    cd "$SRC_DIR/../fabric-samples/test-network"
		./network.sh down
    rm -fr "$SRC_DIR/../fabric-samples"
    
    cd "$SRC_DIR/../insurance-sales-api"
    docker rm -f dev-insurance-sales-api || true
    docker rmi -f insurance-sales-api || true
	fi
}

function printHelp() {
 echo "./startNetwork up"
 echo "./startNetwork down"
}

function prepare_directories() {
  if [[ -d $SRC_DIR/../insurance-sales-api/src/main/resources ]]; then
    cd "$SRC_DIR/../insurance-sales-api/src/main/resources"
    rm -rf tls/*
    rm -rf msp/*
    rm -rf credentials/*
    mkdir -p "$SRC_DIR/../insurance-sales-api/src/main/resources/tls"
    mkdir -p "$SRC_DIR/../insurance-sales-api/src/main/resources/msp"
    mkdir -p "$SRC_DIR/../insurance-sales-api/src/main/resources/credentials"
  fi
  chmod -R 777 "$SRC_DIR/../insurance-sales-api/src/main/resources"
}

function copy_files() {
  prepare_directories
  if [[ -d "$SRC_DIR/../fabric-samples/test-network/organizations" ]]; then
    cd "$SRC_DIR/../fabric-samples/test-network/organizations"
    cp "peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt" "$SRC_DIR/../insurance-sales-api/src/main/resources/tls/ca.crt"
    cp -r "ordererOrganizations/example.com/msp/"* "$SRC_DIR/../insurance-sales-api/src/main/resources/msp"
    cp "peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/"* "$SRC_DIR/../insurance-sales-api/src/main/resources/credentials/admin-key.pem"
    cp "peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/"* "$SRC_DIR/../insurance-sales-api/src/main/resources/credentials/admin-cert.pem"
  fi
}

function build_chaincode() {
  cd "$SRC_DIR/../insurance-chaincode"
  ./gradlew build installDist
}

function build_and_run_api() {
  copy_files
  cd "$SRC_DIR/../insurance-sales-api"
  mvn clean package
  docker build -t insurance-sales-api .
  docker run -d --name dev-insurance-sales-api -p 8080:8080 --network fabric_test insurance-sales-api
}

if [[ $# -lt 1 ]] ; then
  printHelp
  exit 0
else
  MODE=$1
  shift
fi

while [[ $# -ge 1 ]] ; do
  key="$1"
  case $key in
  -h )
    printHelp $MODE
    exit 0
    ;;
  -v )
    FABRIC_VERSION="$2"
	if [ ! -z "$3" ];then
	    CA_VERSION="$3"
	fi
    shift
    ;;
  esac
  shift
done

if [ "${MODE}" == "up" ]; then
  networkUp
elif [ "${MODE}" == "down" ]; then
  networkDown
else
  printHelp
  exit 1
fi

