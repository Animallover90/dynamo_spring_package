package dynamo_spring_package.test;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TestRepository {

    private final DynamoDbTable<Test> testDynamoDbTable;

    public TestRepository(DynamoDbEnhancedClient enhancedClient) {
        this.testDynamoDbTable = enhancedClient.table("test", TableSchema.fromBean(Test.class));;
    }

    public List<TestDTO> getAllTests() {
        try {
            List<Test> tests = testDynamoDbTable.scan().items().stream().toList();
            return tests.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public TestDTO getTestById(String id) {
        Test result = null;
        try {
            Key key = Key.builder()
                    .partitionValue(id)
//                    .sortValue("tred@noserver.com")
                    .build();

            result = testDynamoDbTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return convertToDTO(result);
    }

    public TestDTO createNewTest(TestDTO dto) {
        Test test = new Test();
        try {
            BeanUtils.copyProperties(dto, test);
            testDynamoDbTable.putItem(test);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return convertToDTO(test);
    }

    public TestDTO updateTest(TestDTO dto) {
        Test test = new Test();
        try {
            Key key = Key.builder()
                    .partitionValue(dto.getId())
                    .build();

            test = testDynamoDbTable.getItem(r -> r.key(key));
            test.setPrice(dto.getPrice());
            testDynamoDbTable.updateItem(test);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return convertToDTO(test);
    }

    public void deleteTest(String id) {
        try {
            Key key = Key.builder()
                    .partitionValue(id)
                    .build();

            testDynamoDbTable.deleteItem(key);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    private TestDTO convertToDTO(Test test) {
        TestDTO dto = new TestDTO();
        BeanUtils.copyProperties(test, dto);
        return dto;
    }
}
