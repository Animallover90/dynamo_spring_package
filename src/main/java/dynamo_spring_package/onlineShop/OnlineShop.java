package dynamo_spring_package.onlineShop;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.Map;

@DynamoDbBean
@Data
public class OnlineShop {
    private String PK;
    private String SK;
    private String gsi1Pk;
    private String gsi1Sk;
    private String gsi2Pk;
    private String gsi2Sk;
    private String email;
    private String name;
    private Map<String, String> detail;
    private String price;
    private Map<String, String> address;
    private String entityType;
    private String quantity;
    private String type;
    private String amount;
    private String date;

    @DynamoDbPartitionKey
    public String getPK() {
        return PK;
    }

    @DynamoDbSortKey
    public String getSK() {
        return SK;
    }

    @DynamoDbAttribute("GSI1-PK")
    @DynamoDbSecondaryPartitionKey(indexNames = "GSI1")
    public String getGsi1Pk() {
        return gsi1Pk;
    }

    @DynamoDbAttribute("GSI1-SK")
    @DynamoDbSecondarySortKey(indexNames = "GSI1")
    public String getGsi1Sk() {
        return gsi1Sk;
    }

    @DynamoDbAttribute("GSI2-PK")
    @DynamoDbSecondaryPartitionKey(indexNames = "GSI2")
    public String getGsi2Pk() {
        return gsi2Pk;
    }

    @DynamoDbAttribute("GSI2-SK")
    @DynamoDbSecondarySortKey(indexNames = "GSI2")
    public String getGsi2Sk() {
        return gsi2Sk;
    }

    @DynamoDbAttribute("Name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("Detail")
    public Map<String, String> getDetail() {
        return detail;
    }

    @DynamoDbAttribute("Price")
    public String getPrice() {
        return price;
    }

    @DynamoDbAttribute("Address")
    public Map<String, String> getAddress() {
        return address;
    }

    @DynamoDbAttribute("Quantity")
    public String getQuantity() {
        return quantity;
    }

    @DynamoDbAttribute("Type")
    public String getType() {
        return type;
    }

    @DynamoDbAttribute("Amount")
    public String getAmount() {
        return amount;
    }

    @DynamoDbAttribute("Date")
    public String getDate() {
        return date;
    }

    @DynamoDbAttribute("Email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("EntityType")
    public String getEntityType() {
        return entityType;
    }
}
