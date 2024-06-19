package dynamo_spring_package.reoccuringPayments;

import lombok.Getter;

@Getter
public enum ReoccuringEnum {
    REOCCURING_PAYMENT_TABLE_NAME("ReoccuringPayments"),
    ACCOUNT_PARTITION_ALIAS("ACC#"),
    RECEIPT_SORT_ALIAS("REC#"),
    SUB_SORT_ALIAS("SUB#"),
    SKU_SORT_ALIAS("SKU#"),;

    private final String data;

    ReoccuringEnum(String data) {
        this.data = data;
    }
}
