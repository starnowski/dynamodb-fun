


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

#
export PYTHONPATH=${PYTHONPATH}:./src && serverless invoke local --function hello -d '{"name": "warehouse", "type": "CLOTHES RENTAL", "url": "http://warehouse.com/nosuch_address"}'


export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID='DUMMYIDEXAMPLE'
export AWS_SECRET_ACCESS_KEY='DUMMYEXAMPLEKEY'
export DYNAMODB_PORT=9000
export DYNAMODB_HOST=localhost
serverless invoke local --function hello --path events/leads.json
export PYTHONPATH=${PYTHONPATH}:./src && serverless invoke local --function hello --path events/leads.json


#### Simulate apigateway
serverless simulate apigateway


### Simulate apigateway - authorizator
https://medium.com/free-code-camp/how-you-can-speed-up-serverless-development-by-simulating-aws-lambda-locally-41c61a60fbae

https://aws.amazon.com/blogs/compute/using-serverless-to-load-test-amazon-api-gateway-with-authorization/


### Build Dockerfile image
cd local_tests
docker build -t lambci/lambda:python3.8 .


### Saml
sam build
./prepare_sam_tmp_env_file.sh tmp_vars
sam local start-api --env-vars tmp_vars &



### Local dynamodb
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html
aws dynamodb list-tables --endpoint-url http://localhost:9000

aws dynamodb query \
    --table-name MusicCollection \
    --projection-expression "SongTitle" \
    --key-condition-expression "Artist = :v1" \
    --expression-attribute-values file://expression-attributes.json \
    --return-consumed-capacity TOTAL

### TODO Run subprocess in tests
https://stackoverflow.com/questions/89228/how-do-i-execute-a-program-or-call-a-system-command

https://www.learnaws.org/2020/12/01/test-aws-code/
https://github.com/getmoto/moto


### Python packaging
https://packaging.python.org/en/latest/guides/packaging-namespace-packages/


### Src layout
https://packaging.python.org/en/latest/discussions/src-layout-vs-flat-layout/


### Find Out Which Process Listening on a Particular Port
https://www.tecmint.com/find-out-which-process-listening-on-a-particular-port/
lsof -i :3000


### Writing unit tests for Python AWS Lambda with Serverless framework
https://emshea.com/post/writing-python-unit-tests-lambda-functions

### Approach for writing static methods
https://softwareengineering.stackexchange.com/questions/112137/is-staticmethod-proliferation-a-code-smell

