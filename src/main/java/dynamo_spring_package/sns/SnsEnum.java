package dynamo_spring_package.sns;

import lombok.Getter;

@Getter
public enum SnsEnum {
    SNS_TABLE_NAME("SNS"),
    USER_PARTITION_ALIAS("u#"),
    POST_PARTITION_ALIAS("p#"),
    INFO_SORT_ALIAS("info"),
    COUNT_SORT_ALIAS("count"),
    FOLLOWER("#follower"),
    FOLLOWING("#following"),
    POST("#post"),
    LIKE_LIST("#likelist"),
    LIKE_COUNT("#likecount"),
    TIMELINE("#timeline"),;

    private final String data;

    SnsEnum(String data) {
        this.data = data;
    }
}
