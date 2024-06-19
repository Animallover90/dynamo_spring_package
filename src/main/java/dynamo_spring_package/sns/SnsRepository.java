package dynamo_spring_package.sns;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.Iterator;
import java.util.List;

@Repository
public class SnsRepository {

    private final DynamoDbTable<Sns> snsDynamoDbTable;
    private final String USER_PARTITION_ALIAS;
    private final String POST_PARTITION_ALIAS;

    public SnsRepository(DynamoDbEnhancedClient enhancedClient) {
        this.snsDynamoDbTable = enhancedClient.table(SnsEnum.SNS_TABLE_NAME.getData(), TableSchema.fromBean(Sns.class));
        this.USER_PARTITION_ALIAS = SnsEnum.USER_PARTITION_ALIAS.getData();
        this.POST_PARTITION_ALIAS = SnsEnum.POST_PARTITION_ALIAS.getData();
    }

    public Sns getUserInfoByUserID(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(USER_PARTITION_ALIAS + id)
                    .sortValue(SnsEnum.INFO_SORT_ALIAS.getData())
                    .build());

            Iterator<Sns> results = snsDynamoDbTable.query(queryConditional).items().iterator();
            if (results.hasNext()) {
                return results.next();
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Sns> getFollowerListByUserID(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(USER_PARTITION_ALIAS + id + SnsEnum.FOLLOWER.getData())
                    .build());

            return snsDynamoDbTable.query(queryConditional).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Sns> getFollowingListByUserID(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(USER_PARTITION_ALIAS + id + SnsEnum.FOLLOWING.getData())
                    .build());

            return snsDynamoDbTable.query(queryConditional).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Sns> getPostListByUserID(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(USER_PARTITION_ALIAS + id + SnsEnum.POST.getData())
                    .build());

            return snsDynamoDbTable.query(queryConditional).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<Sns> getUserLikesByPostID(String postId) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(POST_PARTITION_ALIAS + postId + SnsEnum.LIKE_LIST.getData())
                    .build());

            return snsDynamoDbTable.query(queryConditional).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Long getLikeCountByPostID(String postId) {
        try {
            Key key = Key.builder()
                    .partitionValue(POST_PARTITION_ALIAS + postId + SnsEnum.LIKE_COUNT.getData())
                    .sortValue(SnsEnum.COUNT_SORT_ALIAS.getData())
                    .build();

            Sns result = snsDynamoDbTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
            if (result != null) {
                return Long.valueOf(result.getEtc());
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return 0L;
    }

    public List<Sns> getTimelineByUserID(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(USER_PARTITION_ALIAS + id + SnsEnum.TIMELINE.getData())
                    .build());

            return snsDynamoDbTable.query(queryConditional).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
