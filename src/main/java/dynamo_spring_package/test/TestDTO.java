package dynamo_spring_package.test;

import lombok.Data;

@Data
public class TestDTO {
    private String id;
    private String title;
    private double price;
    private Long stockCount;
}
