package dynamo_spring_package.onlineShop;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class OnlineShopRepository {
    private final DynamoDbTable<OnlineShop> onlineShopDynamoDbTable;
    private final DynamoDbIndex<OnlineShop> onlineShopDynamoDbIndexGSI1;
    private final DynamoDbIndex<OnlineShop> onlineShopDynamoDbIndexGSI2;
    private final String CUSTOMER_ALIAS;
    private final String PRODUCT_ALIAS;
    private final String WAREHOUSE_ALIAS;
    private final String ORDER_ALIAS;
    private final String SHIPMENT_SORT_ALIAS;
    private final String SHIPMENT_ITEM_SORT_ALIAS;
    private final String INVOICE_SORT_ALIAS;
    private final String CUSTOMER_ENTITY_TYPE;
    private final String PRODUCT_ENTITY_TYPE;
    private final String WAREHOUSE_ENTITY_TYPE;
    private final String WAREHOUSE_ITEM_ENTITY_TYPE;
    private final String ORDER_ENTITY_TYPE;
    private final String ORDER_ITEM_ENTITY_TYPE;
    private final String SHIPMENT_ENTITY_TYPE;
    private final String SHIPMENT_ITEM_ENTITY_TYPE;
    private final String INVOICE_ENTITY_TYPE;
    private final String EXPRESS_TYPE;

    public OnlineShopRepository(DynamoDbEnhancedClient enhancedClient) {
        this.onlineShopDynamoDbTable = enhancedClient.table(OnlineShopEnum.ONLINE_SHOP_TABLE_NAME.getData(), TableSchema.fromBean(OnlineShop.class));
        this.onlineShopDynamoDbIndexGSI1 = enhancedClient.table(OnlineShopEnum.ONLINE_SHOP_TABLE_NAME.getData(), TableSchema.fromBean(OnlineShop.class)).index("GSI1");
        this.onlineShopDynamoDbIndexGSI2 = enhancedClient.table(OnlineShopEnum.ONLINE_SHOP_TABLE_NAME.getData(), TableSchema.fromBean(OnlineShop.class)).index("GSI2");
        this.CUSTOMER_ALIAS = OnlineShopEnum.CUSTOMER_ALIAS.getData();
        this.PRODUCT_ALIAS = OnlineShopEnum.PRODUCT_ALIAS.getData();
        this.WAREHOUSE_ALIAS = OnlineShopEnum.WAREHOUSE_ALIAS.getData();
        this.ORDER_ALIAS = OnlineShopEnum.ORDER_ALIAS.getData();
        this.SHIPMENT_SORT_ALIAS = OnlineShopEnum.SHIPMENT_SORT_ALIAS.getData();
        this.SHIPMENT_ITEM_SORT_ALIAS = OnlineShopEnum.SHIPMENT_ITEM_SORT_ALIAS.getData();
        this.INVOICE_SORT_ALIAS = OnlineShopEnum.INVOICE_SORT_ALIAS.getData();
        this.CUSTOMER_ENTITY_TYPE = OnlineShopEnum.CUSTOMER_ENTITY_TYPE.getData();
        this.PRODUCT_ENTITY_TYPE = OnlineShopEnum.PRODUCT_ENTITY_TYPE.getData();
        this.WAREHOUSE_ENTITY_TYPE = OnlineShopEnum.WAREHOUSE_ENTITY_TYPE.getData();
        this.WAREHOUSE_ITEM_ENTITY_TYPE = OnlineShopEnum.WAREHOUSE_ITEM_ENTITY_TYPE.getData();
        this.ORDER_ENTITY_TYPE = OnlineShopEnum.ORDER_ENTITY_TYPE.getData();
        this.ORDER_ITEM_ENTITY_TYPE = OnlineShopEnum.ORDER_ITEM_ENTITY_TYPE.getData();
        this.SHIPMENT_ENTITY_TYPE = OnlineShopEnum.SHIPMENT_ENTITY_TYPE.getData();
        this.SHIPMENT_ITEM_ENTITY_TYPE = OnlineShopEnum.SHIPMENT_ITEM_ENTITY_TYPE.getData();
        this.INVOICE_ENTITY_TYPE = OnlineShopEnum.INVOICE_ENTITY_TYPE.getData();
        this.EXPRESS_TYPE = OnlineShopEnum.EXPRESS_TYPE.getData();
    }

    public OnlineShop getCustomerByCustomerId(String id) {
        try {
            Key key = Key.builder()
                    .partitionValue(CUSTOMER_ALIAS + id)
                    .sortValue(CUSTOMER_ALIAS + id)
                    .build();

            return onlineShopDynamoDbTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public OnlineShop getProductByProductId(String id) {
        try {
            Key key = Key.builder()
                    .partitionValue(PRODUCT_ALIAS + id)
                    .sortValue(PRODUCT_ALIAS + id)
                    .build();

            return onlineShopDynamoDbTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public OnlineShop getWarehouseByWarehouseId(String id) {
        try {
            Key key = Key.builder()
                    .partitionValue(WAREHOUSE_ALIAS + id)
                    .sortValue(WAREHOUSE_ALIAS + id)
                    .build();

            return onlineShopDynamoDbTable.getItem(
                    (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key));
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getProductInventoryByProductId(String id, String skId) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(PRODUCT_ALIAS + id)
                    .sortValue(WAREHOUSE_ALIAS + skId)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return onlineShopDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getOrderDetailsByOrderId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(ORDER_ALIAS + id)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return onlineShopDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getProductByOrderId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ORDER_ALIAS + id)
                    .sortValue(PRODUCT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return onlineShopDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getInvoiceByOrderId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ORDER_ALIAS + id)
                    .sortValue(INVOICE_SORT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return onlineShopDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getShipmentByOrderId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ORDER_ALIAS + id)
                    .sortValue(SHIPMENT_SORT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return onlineShopDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getOrderByProductIdForDateRange(String id, String fromDate, String toDate) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBetween(Key.builder()
                            .partitionValue(PRODUCT_ALIAS + id)
                            .sortValue(fromDate)
                            .build()
                    , Key.builder()
                            .partitionValue(PRODUCT_ALIAS + id)
                            .sortValue(toDate)
                            .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI1.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(OnlineShop::getDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getInvoiceByInvoiceId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                            .partitionValue(INVOICE_SORT_ALIAS + id)
                            .sortValue(INVOICE_SORT_ALIAS + id)
                            .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI1.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(OnlineShop::getDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getPaymentByInvoiceId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(INVOICE_SORT_ALIAS + id)
                    .sortValue(INVOICE_SORT_ALIAS + id)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI1.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(OnlineShop::getDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getShipmentDetailsByShipmentId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(SHIPMENT_SORT_ALIAS + id)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI1.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> collectedItems.addAll(page.items()));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getShipmentByWarehouseId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(WAREHOUSE_ALIAS + id)
                    .sortValue(SHIPMENT_SORT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI2.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(OnlineShop::getDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getProductInventoryByWarehouseId(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(WAREHOUSE_ALIAS + id)
                    .sortValue(PRODUCT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI2.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> collectedItems.addAll(page.items()));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getInvoiceByCustomerIdForDateRange(String id, String fromDate, String toDate) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBetween(Key.builder()
                            .partitionValue(CUSTOMER_ALIAS + id)
                            .sortValue(INVOICE_SORT_ALIAS + fromDate)
                            .build()
                    , Key.builder()
                            .partitionValue(CUSTOMER_ALIAS + id)
                            .sortValue(INVOICE_SORT_ALIAS + toDate)
                            .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI2.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(OnlineShop::getDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<OnlineShop> getProductsByCustomerIdForDateRange(String id, String fromDate, String toDate) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBetween(Key.builder()
                            .partitionValue(CUSTOMER_ALIAS + id)
                            .sortValue(PRODUCT_ALIAS + fromDate)
                            .build()
                    , Key.builder()
                            .partitionValue(CUSTOMER_ALIAS + id)
                            .sortValue(PRODUCT_ALIAS + toDate)
                            .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<OnlineShop>> query = onlineShopDynamoDbIndexGSI2.query(request);

            List<OnlineShop> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> collectedItems.addAll(page.items()));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }
}
