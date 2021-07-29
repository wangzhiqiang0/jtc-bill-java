package uk.tw.jtc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.exception.JwtException;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.response.CurrentBillingAllowance;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.SubscriptService;
import uk.tw.jtc.service.PackageReadingService;



@RestController
@RequestMapping("/subscript")
public class SubscriptReadingController {
    private SubscriptService subscriptService;
    private PackageReadingService packageReadingService;

    private InvoiceService invoiceService;

    public SubscriptReadingController(SubscriptService subscriptService, PackageReadingService packageReadingService,InvoiceService invoiceService) {
        this.subscriptService = subscriptService;
        this.packageReadingService = packageReadingService;
        this.invoiceService = invoiceService;
    }

    @PostMapping("/subscriptPackage/{packageId}")
    @Transactional
    public ResponseEntity subscriptPackage(@RequestHeader("customerId") String customerId, @PathVariable String packageId){
        Subscript subscript = subscriptService.getBillByComerId(customerId);
        PackageInfo info =  packageReadingService.getPackageById(packageId);
        if (null != subscript || null == info) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JtcResponse.badRequest());
        }
        subscriptService.subscriptPackage(customerId,info);
        invoiceService.createInvoice(customerId,info);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


//    @GetMapping("/currentBillingPeriod")
//    public ResponseEntity currentBillingPeriod(@RequestHeader("customerId") String customerId){
//        Subscript billing = checkCustomerId(customerId);
//        if(billing == null){
//            return ResponseEntity.badRequest().body(JtcResponse.badRequest());
//        }
//        CurrentBillingAllowance currentBillingAllowance = subscriptService.currentBillingPeriod(billing);
//        return ResponseEntity.ok(JtcResponse.ok(currentBillingAllowance));
//    }
//
//
//
//    @GetMapping("/getInvoiceAnyTime")
//    public ResponseEntity getBillAtAnyTime(@RequestHeader("customerId") String customerId){
//        Subscript billing = checkCustomerId( customerId);
//        return billing!=null?
//                ResponseEntity.ok(JtcResponse.ok(subscriptService.getBillAtAnyTime(billing))):
//                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JtcResponse.badRequest());
//    }

}
