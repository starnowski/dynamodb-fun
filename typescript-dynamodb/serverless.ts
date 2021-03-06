import type { AWS } from '@serverless/typescript';

import leads from '@functions/leads';
import { user_stats } from '@src/functions';

const serverlessConfiguration: AWS = {
  service: 'typescript-dynamodb',
  frameworkVersion: '2',
  plugins: ['serverless-esbuild',
            'serverless-offline'
  ],
  provider: {
    name: 'aws',
    runtime: 'nodejs14.x',
    apiGateway: {
      minimumCompressionSize: 1024,
      shouldStartNameWithService: true,
    },
    environment: {
      AWS_NODEJS_CONNECTION_REUSE_ENABLED: '1',
      NODE_OPTIONS: '--enable-source-maps --stack-trace-limit=1000',
    },
    lambdaHashingVersion: '20201221',
  },
  // import the function via paths
  functions: { leads, user_stats },
  package: { individually: true },
  custom: {
    esbuild: {
      bundle: true,
      minify: false,
      sourcemap: true,
      exclude: ['aws-sdk'],
      target: 'node14',
      define: { 'require.resolve': undefined },
      platform: 'node',
      concurrency: 10,
    },
    esbuildPluginTsc: {
      force: true
    },
    ['serverless-offline']: {
          httpPort: 3000,
          babelOptions: {
            presets: ["env"]
          }
    }
  },
};

module.exports = serverlessConfiguration;
