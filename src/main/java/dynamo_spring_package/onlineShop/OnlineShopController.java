package dynamo_spring_package.onlineShop;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class OnlineShopController {

    private final OnlineShopRepository onlineShopRepository;

    @GetMapping("/customer")
    public OnlineShop getCustomerByCustomerId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":12345
//        }
        return onlineShopRepository.getCustomerByCustomerId(onlineShop.getPK());
    }

    @GetMapping("/product")
    public OnlineShop getProductByProductId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":12345
//        }
        return onlineShopRepository.getProductByProductId(onlineShop.getPK());
    }

    @GetMapping("/warehouse")
    public OnlineShop getWarehouseByWarehouseId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":12345
//        }
        return onlineShopRepository.getWarehouseByWarehouseId(onlineShop.getPK());
    }

    @GetMapping("/warehouse-items")
    public List<OnlineShop> getProductInventoryByProductId(@RequestBody OnlineShop onlineShop) {
//        {
//            "pk":"12345",
//            "sk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getProductInventoryByProductId(onlineShop.getPK(), onlineShop.getSK());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/order-details")
    public List<OnlineShop> getOrderDetailsByOrderId(@RequestBody OnlineShop onlineShop) {
//        {
//            "pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getOrderDetailsByOrderId(onlineShop.getPK());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/order-items")
    public List<OnlineShop> getProductByOrderId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getProductByOrderId(onlineShop.getPK());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/invoices/order")
    public List<OnlineShop> getInvoiceByOrderId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getInvoiceByOrderId(onlineShop.getPK());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/shipments")
    public List<OnlineShop> getShipmentByOrderId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getShipmentByOrderId(onlineShop.getPK());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/order-items/range")
    public List<OnlineShop> getOrderByProductIdForDateRange(@RequestBody Map<String, String> data) {
        System.out.println("data = " + data);
//        {
//            "gsi1Pk":"12345",
//            "fromDate":"2020-06-21T19:17:00",
//            "toDate":"2020-06-21T19:19:00"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getOrderByProductIdForDateRange(data.get("gsi1Pk"), data.get("fromDate"), data.get("toDate"));
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/invoice")
    public List<OnlineShop> getInvoiceByInvoiceId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "gsi1Pk":"55443"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getInvoiceByInvoiceId(onlineShop.getGsi1Pk());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/payment/invoice")
    public List<OnlineShop> getPaymentByInvoiceId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "gsi1Pk":"55443"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getPaymentByInvoiceId(onlineShop.getGsi1Pk());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/shipment-detail")
    public List<OnlineShop> getShipmentDetailsByShipmentId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "gsi1Pk":"88899"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getShipmentDetailsByShipmentId(onlineShop.getGsi1Pk());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/shipment/warehouse")
    public List<OnlineShop> getShipmentByWarehouseId(@RequestBody OnlineShop onlineShop) {
        //        {
//            "gsi2Pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getShipmentByWarehouseId(onlineShop.getGsi2Pk());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/warehouse-item/warehouse")
    public List<OnlineShop> getProductInventoryByWarehouseId(@RequestBody OnlineShop onlineShop) {
//        {
//            "gsi2Pk":"12345"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getProductInventoryByWarehouseId(onlineShop.getGsi2Pk());
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/invoice/customer/range")
    public List<OnlineShop> getInvoiceByCustomerIdForDateRange(@RequestBody Map<String, String> data) {
//        {
//            "gsi2Pk":"12345",
//            "fromDate":"2020-06-21T19:17:00",
//            "toDate":"2020-06-21T19:19:00"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getInvoiceByCustomerIdForDateRange(data.get("gsi2Pk"), data.get("fromDate"), data.get("toDate"));
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }

    @GetMapping("/product/customer")
    public List<OnlineShop> getProductsByCustomerIdForDateRange(@RequestBody Map<String, String> data) {
//        {
//            "gsi2Pk":"12345",
//            "fromDate":"2020-06-21T19:17:00",
//            "toDate":"2020-06-21T19:19:00"
//        }
        List<OnlineShop> customerByCustomerId = onlineShopRepository.getProductsByCustomerIdForDateRange(data.get("gsi2Pk"), data.get("fromDate"), data.get("toDate"));
        if (customerByCustomerId.isEmpty()) {
            return null;
        }
        return customerByCustomerId;
    }
}
