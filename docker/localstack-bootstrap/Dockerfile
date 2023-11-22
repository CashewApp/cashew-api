FROM alpine

RUN apk update && apk add aws-cli
RUN apk add jq

ENV AWS_DEFAULT_OUTPUT=json
ENV AWS_DEFAULT_REGION=sa-east-1
ENV AWS_ENDPOINT_URL=http://localstack:4566
ENV AWS_ACCESS_KEY_ID=123
ENV AWS_SECRET_ACCESS_KEY=123

COPY script.sh .
RUN chmod +x script.sh

ENTRYPOINT ["./script.sh"]