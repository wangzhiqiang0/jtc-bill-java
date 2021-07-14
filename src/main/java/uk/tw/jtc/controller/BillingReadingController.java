package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.service.BillingService;
import uk.tw.jtc.service.PackageReadingService;


@RestController
@RequestMapping("/billing")
public class BillingReadingController {
    private BillingService billingService;
    private PackageReadingService packageReadingService;

    public BillingReadingController(BillingService billingService, PackageReadingService packageReadingService) {
        this.billingService = billingService;
        this.packageReadingService = packageReadingService;
    }

    @GetMapping("/subscriptPackage/{packageId}")
    public ResponseEntity subscriptPackage(@RequestHeader("customerId") String customerId,@PathVariable String packageId){
        if (null != packageReadingService.getPackageByCustomerID(customerId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        billingService.subscriptPackage(customerId,packageId);
        return ResponseEntity.accepted().build();
    }
    @GetMapping("/currentBillingPeriod")
    public ResponseEntity currentBillingPeriod(@RequestHeader("customerId") String customerId){
        CurrentBillingAllowance currentBillingAllowance = billingService.currentBillingPeriod(customerId);
        return ResponseEntity.ok(currentBillingAllowance);
    }

    @GetMapping("/usedPhone/{minute}")
    public ResponseEntity usedPhone(@RequestHeader("customerId") String customerId,@PathVariable Integer minute){
        billingService.usedPhone(customerId,minute);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/usedSMS/{num}")
    public ResponseEntity usedSMS(@RequestHeader("customerId") String customerId,@PathVariable Integer num){
        billingService.usedSMS(customerId,num);
        return ResponseEntity.accepted().build();
    }
}
