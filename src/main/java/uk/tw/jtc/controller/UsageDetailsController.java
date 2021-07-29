package uk.tw.jtc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.exception.JwtException;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.request.UsageRequest;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.SubscriptService;
import uk.tw.jtc.service.UsageDetailsService;

@RestController
@RequestMapping("/usage")
public class UsageDetailsController {
    private SubscriptService subscriptService;

    private UsageDetailsService usageDetailsService;

    public UsageDetailsController(SubscriptService subscriptService, UsageDetailsService usageDetailsService) {
        this.subscriptService = subscriptService;
        this.usageDetailsService = usageDetailsService;
    }

    @PostMapping("/phone")
    public ResponseEntity usagePhone(@RequestHeader("customerId") String customerId, @RequestBody UsageRequest usage) {
        Subscript billing = subscriptService.getBillByComerId(customerId);
        if (billing == null || 0 == usage.getUsage()) {
            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
        }
        usageDetailsService.usage(customerId, usage, UsageTypeEnum.PHONE);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/sms")
    public ResponseEntity usageSMS(@RequestHeader("customerId") String customerId, @RequestBody UsageRequest usage) {
        Subscript billing = subscriptService.getBillByComerId(customerId);
        if (billing == null || 0 == usage.getUsage()) {
            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
        }
        usageDetailsService.usage(customerId, usage, UsageTypeEnum.SMS);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

}
