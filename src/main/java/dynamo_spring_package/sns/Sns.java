package dynamo_spring_package.sns;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
public class Sns {
    private String PK;
    private String SK;
    private String content;
    private String etc;
    private Long follower;
    private Long following;
    private String imageUrl;
    private String name;
    private Long post;
    private String timestamp;
    private Long ttl;

    @DynamoDbPartitionKey
    public String getPK() {
        return PK;
    }

    @DynamoDbSortKey
    public String getSK() {
        return SK;
    }

    @DynamoDbAttribute("content")
    public String getContent() {
        return content;
    }

    @DynamoDbAttribute("etc")
    public String getEtc() {
        return etc;
    }

    @DynamoDbAttribute("follower#")
    public Long getFollower() {
        return follower;
    }

    @DynamoDbAttribute("following#")
    public Long getFollowing() {
        return following;
    }

    @DynamoDbAttribute("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("post#")
    public Long getPost() {
        return post;
    }

    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @DynamoDbAttribute("ttl")
    public Long getTtl() {
        return ttl;
    }
}
