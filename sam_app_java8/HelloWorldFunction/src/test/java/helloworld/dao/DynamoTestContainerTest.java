package helloworld.dao;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.dynamodb.model.CreateTableResponse;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Testcontainers
public abstract class DynamoTestContainerTest {

    private static AmazonDynamoDB dynamoDbAsyncClient;

    @Container
    public static GenericContainer genericContainer = new GenericContainer(
            DockerImageName.parse("amazon/dynamodb-local")
    ).withExposedPorts(8000);

    @BeforeAll
    public static void setupDynamoDB() throws ExecutionException, InterruptedException {
        dynamoDbAsyncClient = getDynamoClient();
        createUserStatsTable();
        createLeadsTable();
    }

    private static AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("FAKE", "FAKE")))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:" + genericContainer.getFirstMappedPort(), null))
                .build();
    }

    private static CreateTableResult createLeadsTable() {
        return dynamoDbAsyncClient.createTable(new CreateTableRequest()
                .withKeySchema(
                        new KeySchemaElement("name", KeyType.HASH),
                        new KeySchemaElement("type", KeyType.RANGE)
                )
                .withAttributeDefinitions(
                        new AttributeDefinition()
                                .withAttributeName("name")
                                .withAttributeType(ScalarAttributeType.S),
                        new AttributeDefinition()
                                .withAttributeName("type")
                                .withAttributeType(ScalarAttributeType.S)
                )
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L))
                .withTableName("leads")
        );
    }

    private static CreateTableResult createUserStatsTable() {
        return dynamoDbAsyncClient.createTable(new CreateTableRequest()
                .withKeySchema(
                        new KeySchemaElement()
                                .withKeyType(KeyType.HASH)
                                .withAttributeName("user_id"),
                        new KeySchemaElement()
                                .withKeyType(KeyType.RANGE)
                                .withAttributeName("timestamp")
                )
                .withAttributeDefinitions(
                        new AttributeDefinition()
                                .withAttributeName("user_id")
                                .withAttributeType(ScalarAttributeType.S),
                        new AttributeDefinition()
                                .withAttributeName("timestamp")
                                .withAttributeType(ScalarAttributeType.N)
                )
                .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(5L))
                .withTableName("user_stats")
        );
    }
}
