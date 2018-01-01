# ACES Listener Ethereum

This service runs along side a ethereum node and scans the blockchain
for new transactions to send to subscribers using the ACES Listener API.


## How it Works

This listener implementation connects to a locally running ethereum instance
using the JSON-RPC client. 

Subscriptions specify a `minConfirmations` required for new transaction
notifications. Because ethereum doesn't have a method for querying 
transactions across the blockchain using confirmations, this application
needs scan blocks recursively until the `maxScanBlockDepth` is reached.
Any new transactions that are found confirmed in each block will be 
posted to the corresponding listener subscriber.

1) get best block (longest chain)
2) iterate over transactions in best block (confirmations = 1)
3) if block depth < `maxScanBlockDepth`
   1) repeat from step 1 with previous block in chain


## Setup

Use `vagrant up` to create a VM for running ethereum testnet node locally.

To use PostgreSQL for the listener database, use the following config:

```yml
spring:
  datasource:
    url: jdbc:postgresql://localhost/aces_listener_db
    username: aces_listener_db_user
    password: aces_listener_db_user_password
    driver-class-name: org.postgresql.Driver
```


## RPC Calls

This app makes use of geth RPC calls to fetch blockchain transactions:

```
curl -X POST http://localhost:8545 \
-H 'Content-type: application/json' \
-d '{
  "jsonrpc":"2.0",
  "method":"eth_getBlockByNumber",
  "params":["latest", true],
  "id":1
}'
```

```
curl -X POST http://localhost:8545 \
-H 'Content-type: application/json' \
-d '{
  "jsonrpc":"2.0",
  "method":"eth_getBlockByHash",
  "params":["0x0000000000000000000000000000000000000000000000000000000000000000", true],
  "id":1
}'
```

Example response:

```
{"jsonrpc":"2.0","id":1,"result":{"difficulty":"0x1","extraData":"0x00000000000000000000000000000000000000000000000000000000000000005ec2aaa235b3ee2799599d5d934fc4f850fa2f380000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000","gasLimit":"0x5fdfb1","gasUsed":"0x0","hash":"0x55b102d1d6ee402dcb2685d2244d6b60bdc89ba9127771facfa8911dbb6be9cf","logsBloom":"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000","miner":"0x0000000000000000000000000000000000000000","mixHash":"0x0000000000000000000000000000000000000000000000000000000000000000","nonce":"0x0000000000000000","number":"0x0","parentHash":"0x0000000000000000000000000000000000000000000000000000000000000000","receiptsRoot":"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421","sha3Uncles":"0x1dcc4de8dec75d7aab85b567b6ccd41ad312451b948a7413f0a142fd40d49347","size":"0x26e","stateRoot":"0xf8d3c8a78bc399d9fdca2cd131537b47987aedd7941cc664808fcb95f3ce5868","timestamp":"0x0","totalDifficulty":"0x1","transactions":[],"transactionsRoot":"0x56e81f171bcc55a6ff8345e692c0f86e5b48e01b996cadc001622fb5e363b421","uncles":[]}}
```

## Example Usage

Consumers register their Http callback endpoint by posting to the `subscriptions`
endpoint. Immediately following successful a subscription the
listener will send all new Ethereum transactions to the registered callback
URL.

Example request:

```bash
curl -X POST 'localhost:9090/subscriptions' \
-H 'Content-type: application/json' \
-d '{
  "callbackUrl": "http://localhost:9090/public/eventLogger",
  "minConfirmations": 5
}'
```

Example response: 

```json
{
  "identifier" : "TwpEVgS64WKG4WalMgBk",
  "callbackUrl" : "http://localhost:9090/public/eventLogger",
  "createdAt" : "2017-10-24T04:15:17.091Z"
}
```

The `callbackUrl` will now receive POSTs for Ethereum transactions 
added to the blockchain:

```

```