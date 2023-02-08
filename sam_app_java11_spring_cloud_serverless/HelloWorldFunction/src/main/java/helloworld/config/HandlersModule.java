package helloworld.config;

import dagger.Module;

@Module
public class HandlersModule {

//    @Singleton
//    @Provides
//    UserStatsPostHandler userStatsPostHandler() {
//        String dynamoDBHOst = System.getProperty("DYNAMODB_HOST");
//        dynamoDBHOst = dynamoDBHOst != null && !dynamoDBHOst.trim().isEmpty() ? dynamoDBHOst : System.getenv("DYNAMODB_HOST");
//        if (dynamoDBHOst != null && !dynamoDBHOst.trim().isEmpty()) {
//            return AmazonDynamoDBClientBuilder.standard()
//                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("DUMMYIDEXAMPLE", "DUMMYEXAMPLEKEY")))
//                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + dynamoDBHOst + ":" + "9000", "us-east-1"))
//                    .build();
//        }
//        return AmazonDynamoDBClientBuilder.defaultClient();
//    }
}
