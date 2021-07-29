package uk.tw.jtc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.exception.JwtException;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.UsageDetailsService;

@RestController
@RequestMapping("/usage")
public class UsageDetailsController {
    @Autowired
    private BillingService billingService;
    @Autowired
    private UsageDetailsService usageDetailsService;

    @PostMapping("/phone")
    public ResponseEntity usagePhone(@RequestHeader("customerId") String customerId, @RequestBody UsageRequest usage) {
        Billing billing = checkCustomerId(customerId);
        if (billing == null || 0 == usage.getUsage()) {
            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
        }
        usageDetailsService.usage(customerId, usage, UsageTypeEnum.PHONE);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sms")
    public ResponseEntity usageSMS(@RequestHeader("customerId") String customerId, @RequestBody UsageRequest usage) {
        Billing billing = checkCustomerId(customerId);
        if (billing == null || 0 == usage.getUsage()) {
            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
        }
        usageDetailsService.usage(customerId, usage, UsageTypeEnum.SMS);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    private Billing checkCustomerId(String customerId) {
        Billing billing = billingService.getBillByComerId(customerId);
        if (billing == null) {
            throw new JwtException(400, "the customer is not bind");
        }
        return billingService.getBillByComerId(customerId);

    }

    @PostMapping("/execute")
    public ResponseEntity execute(){
        usageDetailsService.generateInvoice();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
