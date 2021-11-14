package helloworld.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppDynamoDBModule {

    @Singleton
    @Provides
    AmazonDynamoDB dynamoDb() {
        String dynamoDBHOst = System.getProperty("DYNAMODB_HOST");
        if (dynamoDBHOst != null && !dynamoDBHOst.trim().isEmpty()) {
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("FAKE", "FAKE")))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + dynamoDBHOst + ":" + "9000", null))
                    .build();
        }
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
