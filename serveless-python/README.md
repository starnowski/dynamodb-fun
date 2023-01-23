


https://medium.com/@sebastian.phelps/serverless-and-python-cb3711f6d307

nvm install 12.13.0
npm install serverless-plugin-simulate --save-dev
npm install request --save
serverless simulate apigateway


### Setting project
https://www.serverless.com/blog/serverless-python-packaging/

### Passing parameters to local invoked lambda
https://stackoverflow.com/questions/52251075/how-to-pass-parameters-to-serverless-invoke-local

### Plugin
https://github.com/serverless/serverless-python-requirements

### How to add environment variables
https://www.serverless.com/plugins/serverless-plugin-simulate

curl --location --request POST 'http://localhost:3000/leads' --header 'Content-Type: application/json' \
--verbose \
--data-raw '{"name": "warehouse", "type": "CLOTHES RENTAL", "url": "http://warehouse.com/nosuch_address"}'


### Local invoke
serverless invoke local --function hello -d '{"name": "warehouse", "type": "CLOTHES RENTAL", "url": "http://warehouse.com/nosuch_address"}'


export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost
serverless invoke local --function hello --path events/leads.json


#### Simulate apigateway
serverless simulate apigateway


### Simulate apigateway - authorizator
https://medium.com/free-code-camp/how-you-can-speed-up-serverless-development-by-simulating-aws-lambda-locally-41c61a60fbae

https://aws.amazon.com/blogs/compute/using-serverless-to-load-test-amazon-api-gateway-with-authorization/


### Build Dockerfile image
cd local_tests
docker build -t lambci/lambda:python3.8 .


### Saml
./prepare_sam_tmp_env_file.sh tmp_vars
sam local start-api --env-vars tmp_vars &
