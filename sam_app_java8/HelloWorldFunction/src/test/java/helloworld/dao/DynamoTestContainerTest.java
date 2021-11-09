package helloworld.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Testcontainers
public abstract class DynamoTestContainerTest {

    private static DynamoDbAsyncClient dynamoDbAsyncClient;

    @Container
    public static GenericContainer genericContainer = new GenericContainer(
            DockerImageName.parse("amazon/dynamodb-local")
    ).withExposedPorts(8000);

    @BeforeAll
    public static void setupDynamoDB() throws ExecutionException, InterruptedException {
        dynamoDbAsyncClient = getDynamoClient();
        createUserStatsTableAsync().get();
        createLeadsTableAsync().get();
    }

    private static DynamoDbAsyncClient getDynamoClient() {
        return DynamoDbAsyncClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create("http://localhost:" + genericContainer.getFirstMappedPort()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("FAKE", "FAKE")))
                .build();
    }

    private static CompletableFuture<CreateTableResponse> createLeadsTableAsync() {
        return dynamoDbAsyncClient.createTable(CreateTableRequest.builder()
                .keySchema(
                        KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("name")
                                .build(),
                        KeySchemaElement.builder()
                                .keyType(KeyType.RANGE)
                                .attributeName("type")
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("name")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("type")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build())
                .tableName("leads")
                .build()
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
