package dynamo_spring_package.onlineShop;

import lombok.Getter;

@Getter
public enum OnlineShopEnum {
    ONLINE_SHOP_TABLE_NAME("OnlineShop"),
    CUSTOMER_ALIAS("c#"),
    PRODUCT_ALIAS("p#"),
    WAREHOUSE_ALIAS("w#"),
    ORDER_ALIAS("o#"),
    SHIPMENT_SORT_ALIAS("sh#"),
    SHIPMENT_ITEM_SORT_ALIAS("shp#"),
    INVOICE_SORT_ALIAS("i#"),
    CUSTOMER_ENTITY_TYPE("customer"),
    PRODUCT_ENTITY_TYPE("product"),
    WAREHOUSE_ENTITY_TYPE("warehouse"),
    WAREHOUSE_ITEM_ENTITY_TYPE("warehouseItem"),
    ORDER_ENTITY_TYPE("order"),
    ORDER_ITEM_ENTITY_TYPE("orderItem"),
    SHIPMENT_ENTITY_TYPE("shipment"),
    SHIPMENT_ITEM_ENTITY_TYPE("shipmentItem"),
    INVOICE_ENTITY_TYPE("invoice"),
    EXPRESS_TYPE("Express"),;

    private final String data;

    OnlineShopEnum(String data) {
        this.data = data;
    }
}
