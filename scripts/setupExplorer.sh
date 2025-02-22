#!/bin/bash

SRC_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

function start_explorer() {
  cd "$SRC_DIR/../explorer"
  docker-compose down || true
  docker rm -f explorer.mynetwork.com || true
  docker rm -f explorerdb.mynetwork.com || true
  docker-compose up -d
  echo "Hyperledger Explorer iniciado com sucesso."
}

function stop_explorer() {
  cd "$SRC_DIR/../explorer"
  docker-compose down || true
  docker rm -f explorer.mynetwork.com || true
  docker rm -f explorerdb.mynetwork.com || true
  docker rmi -f ghcr.io/hyperledger-labs/explorer:latest || true
  docker rmi -f ghcr.io/hyperledger-labs/explorer-db:latest || true
  rm -rf "$SRC_DIR/../explorer/organizations"
  echo "Hyperledger Explorer finalizado com sucesso."
}

function copy_and_rename_organizations() {
  local src_path="$SRC_DIR/../fabric-samples/test-network/organizations"
  local dest_path="$SRC_DIR/../explorer/organizations"

  # Copiar as pastas ordererOrganizations e peerOrganizations para o destino
  if [ -d "$dest_path" ]; then
    rm -rf "$dest_path"
  fi
  mkdir -p "$dest_path"
  cp -r "$src_path/ordererOrganizations" "$dest_path"
  cp -r "$src_path/peerOrganizations" "$dest_path"

  # Caminho do diretório keystore
  local keystore_dir="$dest_path/peerOrganizations/org1.example.com/users/User1@org1.example.com/msp/keystore"

  # Renomear o arquivo no diretório keystore para priv_sk
  for filename in "$keystore_dir"/*; do
    if [ "$(basename "$filename")" != "priv_sk" ]; then
      mv "$filename" "$keystore_dir/priv_sk"
      break
    fi
  done

  echo "Pastas ordererOrganizations e peerOrganizations copiadas e arquivo renomeado com sucesso."
}

function printHelp() {
  echo "Usage: ./setupExplorer.sh {up|down}"
}

if [[ $# -lt 1 ]] ; then
  printHelp
  exit 0
else
  MODE=$1
  shift
fi

case "${MODE}" in
  up)
    copy_and_rename_organizations
    start_explorer
    ;;
  down)
    stop_explorer
    ;;
  *)
    printHelp
    exit 1
    ;;
esac