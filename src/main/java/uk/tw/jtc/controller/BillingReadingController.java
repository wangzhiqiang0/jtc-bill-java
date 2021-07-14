package uk.tw.jtc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/billing")
public class BillingReadingController {


    @GetMapping("/currentBillingPeriod")
    public ResponseEntity getCurrentBillingPeriod(@RequestHeader("customerId") String customerId){

        return null;
    }
}
