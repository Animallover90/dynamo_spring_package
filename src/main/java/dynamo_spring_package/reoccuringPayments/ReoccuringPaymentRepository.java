package dynamo_spring_package.reoccuringPayments;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class ReoccuringPaymentRepository {
    private final DynamoDbTable<ReoccuringPayments> paymentsDynamoDbTable;
    private final DynamoDbIndex<ReoccuringPayments> paymentsDynamoDbIndexGSI_1;
    private final DynamoDbIndex<ReoccuringPayments> paymentsDynamoDbIndexGSI_2;
    private final String ACCOUNT_PARTITION_ALIAS;
    private final String SUB_SORT_ALIAS;
    private final String SKU_SORT_ALIAS;
    private final String RECEIPT_SORT_ALIAS;

    public ReoccuringPaymentRepository(DynamoDbEnhancedClient enhancedClient) {
        this.paymentsDynamoDbTable = enhancedClient.table(ReoccuringEnum.REOCCURING_PAYMENT_TABLE_NAME.getData(), TableSchema.fromBean(ReoccuringPayments.class));
        this.paymentsDynamoDbIndexGSI_1 = enhancedClient.table(ReoccuringEnum.REOCCURING_PAYMENT_TABLE_NAME.getData(), TableSchema.fromBean(ReoccuringPayments.class)).index("GSI-1");
        this.paymentsDynamoDbIndexGSI_2 = enhancedClient.table(ReoccuringEnum.REOCCURING_PAYMENT_TABLE_NAME.getData(), TableSchema.fromBean(ReoccuringPayments.class)).index("GSI-2");
        this.ACCOUNT_PARTITION_ALIAS = ReoccuringEnum.ACCOUNT_PARTITION_ALIAS.getData();
        this.SUB_SORT_ALIAS = ReoccuringEnum.SUB_SORT_ALIAS.getData();
        this.SKU_SORT_ALIAS = ReoccuringEnum.SKU_SORT_ALIAS.getData();
        this.RECEIPT_SORT_ALIAS = ReoccuringEnum.RECEIPT_SORT_ALIAS.getData();
    }

    public void createSubscription(ReoccuringPayments reoccuringPayments) {
        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            String nowIsoInstantDate = isoInstantDate(now);

            LocalDate todayUtc = LocalDateTime.now(ZoneOffset.UTC).toLocalDate();

            reoccuringPayments.setSK(SUB_SORT_ALIAS + unixTimestamp() + "#" + SKU_SORT_ALIAS + reoccuringPayments.getSku());
            reoccuringPayments.setPK(ACCOUNT_PARTITION_ALIAS + reoccuringPayments.getPK());
            reoccuringPayments.setLastPaymentDate(nowIsoInstantDate);
            reoccuringPayments.setLastReminderDate(nowIsoInstantDate);
            reoccuringPayments.setNextPaymentDate(oneMonthLaterIsoLocalDate(todayUtc));
            reoccuringPayments.setNextReminderDate(oneMonthLaterBeforeOneWeekIsoLocalDate(todayUtc));

            PutItemEnhancedResponse<ReoccuringPayments> response = paymentsDynamoDbTable.putItemWithResponse(PutItemEnhancedRequest.builder(ReoccuringPayments.class)
                    .item(reoccuringPayments)
//                    .returnConsumedCapacity(ReturnConsumedCapacity.TOTAL)
//                    .returnValuesOnConditionCheckFailure(ReturnValuesOnConditionCheckFailure.ALL_OLD)
                    .build());
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createReceipt(ReoccuringPayments reoccuringPayments) {
        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
            String nowIsoInstantDate = isoInstantDate(now);

            reoccuringPayments.setSK(RECEIPT_SORT_ALIAS + nowIsoInstantDate.replace("Z", "") + "#" + SKU_SORT_ALIAS + reoccuringPayments.getSku());
            reoccuringPayments.setPK(ACCOUNT_PARTITION_ALIAS + reoccuringPayments.getPK());
            reoccuringPayments.setProcessedDate(nowIsoInstantDate);
            reoccuringPayments.setTtl(unixTimestamp());

            PutItemEnhancedResponse<ReoccuringPayments> response = paymentsDynamoDbTable.putItemWithResponse(PutItemEnhancedRequest.builder(ReoccuringPayments.class)
                    .item(reoccuringPayments)
                    .build());
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateSubscription(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ACCOUNT_PARTITION_ALIAS + id)
                    .sortValue(SUB_SORT_ALIAS)
                    .build());

            // 기본적으로 sort key 기준으로 asc 정렬되어 반환된다.
            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .limit(1)
                    .build();

            List<ReoccuringPayments> list = paymentsDynamoDbTable.query(request).items().stream().toList();

            if (!list.isEmpty()) {
                ReoccuringPayments reoccuringPayments = list.get(0);

                LocalDate todayUtc = LocalDateTime.now(ZoneOffset.UTC).toLocalDate();

                reoccuringPayments.setNextPaymentDate(oneMonthLaterIsoLocalDate(todayUtc));
                reoccuringPayments.setNextReminderDate(oneMonthLaterBeforeOneWeekIsoLocalDate(todayUtc));

                UpdateItemEnhancedResponse<ReoccuringPayments> response = paymentsDynamoDbTable.updateItemWithResponse(UpdateItemEnhancedRequest.builder(ReoccuringPayments.class)
                        .item(reoccuringPayments)
                        .build());
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<ReoccuringPayments> getDueRemindersByDate() {
        try {
            String today = isoLocalDate(LocalDateTime.now(ZoneOffset.UTC).toLocalDate());
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(today)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<ReoccuringPayments>> query = paymentsDynamoDbIndexGSI_1.query(request);

            List<ReoccuringPayments> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(ReoccuringPayments::getLastReminderDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<ReoccuringPayments> getDuePaymentsByDate() {
        try {
            String today = isoLocalDate(LocalDateTime.now(ZoneOffset.UTC).toLocalDate());
            QueryConditional queryConditional = QueryConditional.keyEqualTo(Key.builder()
                    .partitionValue(today)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            SdkIterable<Page<ReoccuringPayments>> query = paymentsDynamoDbIndexGSI_2.query(request);

            List<ReoccuringPayments> collectedItems = new ArrayList<>();
            query.stream().forEach(page -> page.items().stream()
                    .sorted(Comparator.comparing(ReoccuringPayments::getLastPaymentDate)) // 특정 값을 기준으로 정렬
                    .forEach(collectedItems::add));

            return collectedItems;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<ReoccuringPayments> getSubscriptionsByAccount(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ACCOUNT_PARTITION_ALIAS + id)
                    .sortValue(SUB_SORT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return paymentsDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public List<ReoccuringPayments> getReceiptsByAccount(String id) {
        try {
            QueryConditional queryConditional = QueryConditional.sortBeginsWith(Key.builder()
                    .partitionValue(ACCOUNT_PARTITION_ALIAS + id)
                    .sortValue(RECEIPT_SORT_ALIAS)
                    .build());

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(queryConditional)
                    .build();

            return paymentsDynamoDbTable.query(request).items().stream().toList();
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public String isoInstantDate(ZonedDateTime now) {
        // 오늘 날짜 (UTC) 구하기
        String date = now.format(DateTimeFormatter.ISO_INSTANT);
        System.out.println("Today (UTC): " + date);
        return date;
    }

    public String oneMonthLaterIsoInstantDate(ZonedDateTime now) {
        // 한 달 뒤 날짜 (UTC) 구하기
        ZonedDateTime oneMonthLater = now.plusMonths(1);
        String date = oneMonthLater.format(DateTimeFormatter.ISO_INSTANT);
        System.out.println("One Month Later (UTC): " + date);
        return date;
    }

    public String isoLocalDate(LocalDate todayUtc) {
        // 오늘 날짜 (UTC) 구하기
        String date = todayUtc.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("Today (UTC): " + date);
        return date;
    }

    public String oneMonthLaterIsoLocalDate(LocalDate todayUtc) {
        // 한 달 뒤 날짜 (UTC) 구하기
        LocalDate oneMonthLaterUtc = todayUtc.plusMonths(1);
        String date = oneMonthLaterUtc.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("One Month Later (UTC): " + date);
        return date;
    }

    public String oneMonthLaterBeforeOneWeekIsoLocalDate(LocalDate todayUtc) {
        // 한 달 뒤에서 일주일 전 날짜 (UTC) 구하기
        LocalDate oneMonthLaterUtc = todayUtc.plusMonths(1);
        LocalDate oneWeekBeforeUtc = oneMonthLaterUtc.minusWeeks(1);
        String date = oneWeekBeforeUtc.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("One Month Later (UTC): " + date);
        return date;
    }

    public long unixTimestamp() {
        // 현재 UTC 시간의 Unix 타임스탬프 구하기
        Instant now = Instant.now();
        long unixTimestampNow = now.getEpochSecond();
        System.out.println("Current Unix Timestamp (UTC): " + unixTimestampNow);
        return unixTimestampNow;
    }
}
