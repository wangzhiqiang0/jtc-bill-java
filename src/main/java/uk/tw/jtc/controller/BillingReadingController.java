package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.exception.JwtException;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.JtcResponse;
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

    @PostMapping("/subscriptPackage/{packageId}")
    public ResponseEntity subscriptPackage(@RequestHeader("customerId") String customerId, @PathVariable String packageId){
        Billing billing = billingService.getBillByComerId(customerId);
        PackageInfo info =  packageReadingService.getPackageById(packageId);
        if (null != billing || null == info) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JtcResponse.badRequest());
        }
        billingService.subscriptPackage(customerId,info);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/currentBillingPeriod")
    public ResponseEntity currentBillingPeriod(@RequestHeader("customerId") String customerId){
        Billing billing = checkCustomerId(customerId);
        if(billing == null){
            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
        }
        CurrentBillingAllowance currentBillingAllowance = billingService.currentBillingPeriod(billing);
        return ResponseEntity.ok(JtcResponse.ok(currentBillingAllowance));
    }



    @GetMapping("/getInvoiceAnyTime")
    public ResponseEntity getBillAtAnyTime(@RequestHeader("customerId") String customerId){
        Billing billing = checkCustomerId( customerId);
        return billing!=null?
                ResponseEntity.ok(JtcResponse.ok(billingService.getBillAtAnyTime(billing))):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JtcResponse.badRequest());
    }

    private Billing checkCustomerId(String customerId){
        Billing billing = billingService.getBillByComerId(customerId);
        if(billing ==null){
            throw new JwtException(400,"the customer is not bind");
        }
        return billingService.getBillByComerId(customerId);

    }
}
