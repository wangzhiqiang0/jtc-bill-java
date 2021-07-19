package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uk.tw.jtc.model.Billing;
import uk.tw.jtc.request.Used;
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

    @PostMapping("/subscriptPackage/{packageId}")
    public ResponseEntity subscriptPackage(@RequestHeader("customerId") String customerId,@PathVariable String packageId){
        if (null != billingService.getBillByComerId(customerId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        billingService.subscriptPackage(customerId,packageId);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/execute")
    public ResponseEntity execute(){

        billingService.generateBill();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/currentBillingPeriod")
    public ResponseEntity currentBillingPeriod(@RequestHeader("customerId") String customerId){
        Billing billing = checkCustomerId(customerId);
        if(billing == null){
            return ResponseEntity.badRequest().build();
        }
        CurrentBillingAllowance currentBillingAllowance = billingService.currentBillingPeriod(customerId);
        return ResponseEntity.ok(currentBillingAllowance);
    }


    @PostMapping("/usedPhone")
    public ResponseEntity usedPhone(@RequestHeader("customerId") String customerId,@RequestBody Used pay){
        Billing billing = checkCustomerId(customerId);
        if(billing == null || 0==pay.getPhoneUsed()){
            return ResponseEntity.badRequest().build();
        }
        billingService.usedPhone(customerId,pay.getPhoneUsed());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/usedSMS")
    public ResponseEntity usedSMS(@RequestHeader("customerId") String customerId,@RequestBody Used pay){
        Billing billing = checkCustomerId(customerId);
        if(billing == null || 0==pay.getSmsUsed()){
            return ResponseEntity.badRequest().build();
        }
        billingService.usedSMS(customerId,pay.getSmsUsed());
        return ResponseEntity.accepted().build();

    }

    @GetMapping("/getInvoiceAnyTime")
    public ResponseEntity getBillAtAnyTime(@RequestHeader("customerId") String customerId){
        Billing billing = checkCustomerId( customerId);
        return billing!=null?
                ResponseEntity.ok(billingService.getBillAtAnyTime(customerId)):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    private Billing checkCustomerId(String customerId){

        return billingService.getBillByComerId(customerId);

    }
}
