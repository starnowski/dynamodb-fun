# Serverless - AWS Node.js Typescript

This project has been generated using the `aws-nodejs-typescript` template from the [Serverless framework](https://www.serverless.com/).

For detailed instructions, please refer to the [documentation](https://www.serverless.com/framework/docs/providers/aws/).

## Installation/deployment instructions

Depending on your preferred package manager, follow the instructions below to deploy your project.

> **Requirements**: NodeJS `lts/fermium (v.14.15.0)`. If you're using [nvm](https://github.com/nvm-sh/nvm), run `nvm use` to ensure you're using the same Node version in local and in your lambda's runtime.

### Using NPM

- Run `npm i` to install the project dependencies
- Run `npx sls deploy` to deploy this stack to AWS

### Using Yarn

- Run `yarn` to install the project dependencies
- Run `yarn sls deploy` to deploy this stack to AWS

## Test your service

This template contains a single lambda function triggered by an HTTP request made on the provisioned API Gateway REST API `/hello` route with `POST` method. The request body must be provided as `application/json`. The body structure is tested by API Gateway against `src/functions/hello/schema.ts` JSON-Schema definition: it must contain the `name` property.

- requesting any other path than `/hello` with any other method than `POST` will result in API Gateway returning a `403` HTTP error code
- sending a `POST` request to `/hello` with a payload **not** containing a string property named `name` will result in API Gateway returning a `400` HTTP error code
- sending a `POST` request to `/hello` with a payload containing a string property named `name` will result in API Gateway returning a `200` HTTP status code with a message saluting the provided name and the detailed event processed by the lambda

> :warning: As is, this template, once deployed, opens a **public** endpoint within your AWS account resources. Anybody with the URL can actively execute the API Gateway endpoint and the corresponding lambda. You should protect this endpoint with the authentication method of your choice.

### Locally

In order to test the hello function locally, run the following command:

- `npx sls invoke local -f hello --path src/functions/hello/mock.json` if you're using NPM
- `yarn sls invoke local -f hello --path src/functions/hello/mock.json` if you're using Yarn

Check the [sls invoke local command documentation](https://www.serverless.com/framework/docs/providers/aws/cli-reference/invoke-local/) for more information.

### Remotely

Copy and replace your `url` - found in Serverless `deploy` command output - and `name` parameter in the following `curl` command in your terminal or in Postman to test your newly deployed application.

```
curl --location --request POST 'https://myApiEndpoint/dev/hello' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Frederic"
}'
```

## Template features

### Project structure

The project code base is mainly located within the `src` folder. This folder is divided in:

- `functions` - containing code base and configuration for your lambda functions
- `libs` - containing shared code base between your lambdas

```
.
├── src
│   ├── functions               # Lambda configuration and source code folder
│   │   ├── hello
│   │   │   ├── handler.ts      # `Hello` lambda source code
│   │   │   ├── index.ts        # `Hello` lambda Serverless configuration
│   │   │   ├── mock.json       # `Hello` lambda input parameter, if any, for local invocation
│   │   │   └── schema.ts       # `Hello` lambda input event JSON-Schema
│   │   │
│   │   └── index.ts            # Import/export of all lambda configurations
│   │
│   └── libs                    # Lambda shared code
│       └── apiGateway.ts       # API Gateway specific helpers
│       └── handlerResolver.ts  # Sharable library for resolving lambda handlers
│       └── lambda.ts           # Lambda middleware
│
├── package.json
├── serverless.ts               # Serverless service file
├── tsconfig.json               # Typescript compiler configuration
├── tsconfig.paths.json         # Typescript paths
└── webpack.config.js           # Webpack configuration
```

### 3rd party libraries

- [json-schema-to-ts](https://github.com/ThomasAribart/json-schema-to-ts) - uses JSON-Schema definitions used by API Gateway for HTTP request validation to statically generate TypeScript types in your lambda's handler code base
- [middy](https://github.com/middyjs/middy) - middleware engine for Node.Js lambda. This template uses [http-json-body-parser](https://github.com/middyjs/middy/tree/master/packages/http-json-body-parser) to convert API Gateway `event.body` property, originally passed as a stringified JSON, to its corresponding parsed object
- [@serverless/typescript](https://github.com/serverless/typescript) - provides up-to-date TypeScript definitions for your `serverless.ts` service file

### Advanced usage

Any tsconfig.json can be used, but if you do, set the environment variable `TS_NODE_CONFIG` for building the application, eg `TS_NODE_CONFIG=./tsconfig.app.json npx serverless webpack`


### Links
https://levelup.gitconnected.com/creating-a-simple-serverless-application-using-typescript-and-aws-part-1-be2188f5ff93

#### testing framework sinon
https://sinonjs.org/


#### IoC for Typescript
https://levelup.gitconnected.com/dependency-injection-in-typescript-2f66912d143c
https://github.com/mertturkmenoglu/typescript-dependency-injection


curl --location --request POST 'http://localhost:3000/dev/leads' \
--header 'Content-Type: application/json' \
--verbose \
--data-raw '{
    "name": "Frederic",
    "url": "www.dog.com"
}'

curl --location --request POST 'http://localhost:3000/dev/user_stats' \
--header 'Content-Type: application/json' \
--verbose \
--data-raw '{
    "user_id": "1", "timestamp": "2012", "weight": 83, "blood_pressure": 123
}'



{"message":"Hello Frederic, welcome to the exciting Serverless world!","event":{"body":{"name":"Frederic","url":"www.dog.com"},"headers":{"Host":"localhost:3000","User-Agent":"curl/7.68.0","Accept":"*/*","Content-Type":"application/json","Content-Length":"52"},"httpMethod":"POST","isBase64Encoded":false,"multiValueHeaders":{"Host":["localhost:3000"],"User-Agent":["curl/7.68.0"],"Accept":["*/*"],"Content-Type":["application/json"],"Content-Length":["52"]},"multiValueQueryStringParameters":null,"path":"/leads","pathParameters":null,"queryStringParameters":null,"requestContext":{"accountId":"offlineContext_accountId","apiId":"offlineContext_apiId","authorizer":{"principalId":"offlineContext_authorizer_principalId"},"domainName":"offlineContext_domainName","domainPrefix":"offlineContext_domainPrefix","extendedRequestId":"ckyw6fiz60000y6xje5qddckr","httpMethod":"POST","identity":{"accessKey":null,"accountId":"offlineContext_accountId","apiKey":"offlineContext_apiKey","apiKeyId":"offlineContext_apiKeyId","caller":"offlineContext_caller","cognitoAuthenticationProvider":"offlineContext_cognitoAuthenticationProvider","cognitoAuthenticationType":"offlineContext_cognitoAuthenticationType","cognitoIdentityId":"offlineContext_cognitoIdentityId","cognitoIdentityPoolId":"offlineContext_cognitoIdentityPoolId","principalOrgId":null,"sourceIp":"127.0.0.1","user":"offlineContext_user","userAgent":"curl/7.68.0","userArn":"offlineContext_userArn"},"path":"/leads","protocol":"HTTP/1.1","requestId":"ckyw6fiz70001y6xj4q6i3joo","requestTime":"27/Jan/2022:00:26:41 +0100","requestTimeEpoch":1643239601099,"resourceId":"offlineContext_resourceId","resourcePath":"/dev/leads","stage":"dev"},"resource":"/leads","rawBody":"{\n    \"name\": \"Frederic\",\n    \"url\": \"www.dog.com\"\n}"}}


### Great tutorial about labmda with DI dependency injection
https://towardsaws.com/my-first-lambda-a-story-of-clean-architecture-caching-di-and-serverless-9d9dd400e5cb

### Mocking DI dependency injection
https://tech.gc.com/dependency-injection/