package uk.tw.jtc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/get")
public class BillingReadingController {


    @GetMapping("/currentBillingPeriod")
    public ResponseEntity getCurrentBillingPeriod(@RequestHeader("customerId") String customerId){

        return null;
    }
}
