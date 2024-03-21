#!/bin/sh

aws kms create-key --key-usage SIGN_VERIFY --key-spec RSA_2048 --description "Chave criada no localstack" | jq -r ".KeyMetadata.KeyId" > key.json && \
    aws ssm put-parameter --name "/dev/cashew/kms.keyid" --type "SecureString" --value $(cat key.json) && \
    aws ssm put-parameter --name "/dev/cashew/kms.publickey" --type "SecureString" --value $(aws kms get-public-key --key-id $(cat key.json) | jq -r ".PublicKey") && \
    rm key.json
