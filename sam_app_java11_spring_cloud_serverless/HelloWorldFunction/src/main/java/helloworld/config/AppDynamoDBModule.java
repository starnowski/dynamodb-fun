package helloworld.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class AppDynamoDBModule {

    @Bean
    AmazonDynamoDB dynamoDb() {
        String dynamoDBHOst = System.getProperty("DYNAMODB_HOST");
        dynamoDBHOst = dynamoDBHOst != null && !dynamoDBHOst.trim().isEmpty() ? dynamoDBHOst : System.getenv("DYNAMODB_HOST");
        if (dynamoDBHOst != null && !dynamoDBHOst.trim().isEmpty()) {
            return AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("DUMMYIDEXAMPLE", "DUMMYEXAMPLEKEY")))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://" + dynamoDBHOst + ":" + "9000", "us-east-1"))
                    .build();
        }
        return AmazonDynamoDBClientBuilder.defaultClient();
    }
}
