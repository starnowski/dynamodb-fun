package helloworld.dao;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
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
        createUserStatsTableAsync().get();
        createLeadsTable();
    }

    private static AmazonDynamoDB getDynamoClient() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion("us-east-1")
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("FAKE", "FAKE")))
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

    private static CompletableFuture<CreateTableResponse> createUserStatsTableAsync() {
        return dynamoDbAsyncClient.createTable(CreateTableRequest.builder()
                .keySchema(
                        KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("user_id")
                                .build(),
                        KeySchemaElement.builder()
                                .keyType(KeyType.RANGE)
                                .attributeName("timestamp")
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("user_id")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("timestamp")
                                .attributeType(ScalarAttributeType.N)
                                .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
                .tableName("user_stats")
                .build()
        );
    }
}
