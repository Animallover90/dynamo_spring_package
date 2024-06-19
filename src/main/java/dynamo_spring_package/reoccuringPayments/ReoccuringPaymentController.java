package dynamo_spring_package.reoccuringPayments;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class ReoccuringPaymentController {
    
    private final ReoccuringPaymentRepository paymentRepository;

    @PostMapping()
    public void createSubscription(@RequestBody ReoccuringPayments reoccuringPayments) {
//        {
//            "pk":2,
//            "sku":"888",
//            "email":"a@gmail.com",
//            "paymentAmount": "10.00",
//            "paymentDetails": { "default-card" : "1234123412341234", "default-address" : "12 Bridge Street, Birmingham, B12 7ST" },
//            "paymentDay": "28"
//        }
        paymentRepository.createSubscription(reoccuringPayments);
    }

    @PostMapping("/receipt")
    public void createReceipt(@RequestBody ReoccuringPayments reoccuringPayments) {
//        {
//            "pk":2,
//            "sku":"888",
//            "email":"a@gmail.com",
//            "processedAmount": "10.00"
//        }
        paymentRepository.createReceipt(reoccuringPayments);
    }

    @PutMapping("/receipt")
    public void updateSubscription(@RequestBody ReoccuringPayments reoccuringPayments) {
//        {
//            "pk":2
//        }
        paymentRepository.updateSubscription(reoccuringPayments.getPK());
    }

    @GetMapping("/due-reminder")
    public List<ReoccuringPayments> getDueRemindersByDate() {
        List<ReoccuringPayments> dueRemindersByDate = paymentRepository.getDueRemindersByDate();
        if (dueRemindersByDate.isEmpty()) {
            return null;
        }
        return dueRemindersByDate;
    }

    @GetMapping("/due-payment")
    public List<ReoccuringPayments> getDuePaymentsByDate() {
        List<ReoccuringPayments> duePaymentsByDate = paymentRepository.getDuePaymentsByDate();
        if (duePaymentsByDate.isEmpty()) {
            return null;
        }
        return duePaymentsByDate;
    }

    @GetMapping()
    public List<ReoccuringPayments> getSubscriptionsByAccount(@RequestBody ReoccuringPayments reoccuringPayments) {
//        {
//            "pk":2
//        }
        List<ReoccuringPayments> duePaymentsByDate = paymentRepository.getSubscriptionsByAccount(reoccuringPayments.getPK());
        if (duePaymentsByDate.isEmpty()) {
            return null;
        }
        return duePaymentsByDate;
    }

    @GetMapping("/receipt")
    public List<ReoccuringPayments> getReceiptsByAccount(@RequestBody ReoccuringPayments reoccuringPayments) {
//        {
//            "pk":2
//        }
        List<ReoccuringPayments> duePaymentsByDate = paymentRepository.getReceiptsByAccount(reoccuringPayments.getPK());
        if (duePaymentsByDate.isEmpty()) {
            return null;
        }
        return duePaymentsByDate;
    }
}
