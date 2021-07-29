package uk.tw.jtc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.request.PaymentRequest;
import uk.tw.jtc.response.JtcResponse;
import uk.tw.jtc.service.InvoiceService;
import uk.tw.jtc.service.PaymentService;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceReadingController {
    private InvoiceService invoiceService;
    PaymentService paymentService;

    public InvoiceReadingController(InvoiceService invoiceService,PaymentService paymentService) {
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
    }




    @GetMapping("/active")
    public ResponseEntity getActiveInvoice(@RequestHeader("customerId")  String customerId) {
        List<Invoice> invoice = invoiceService.getActiveInvoice(customerId);
        if(null == invoice){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(JtcResponse.notFound());
        }
        return ResponseEntity.ok(JtcResponse.ok(invoice));
    }



    @PostMapping("/execute")
    public ResponseEntity execute(){
        invoiceService.generateInvoice();
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/paid")
    public ResponseEntity paidPayment(@RequestHeader("customerId")String customerId, @RequestBody PaymentRequest paymentRequest) {
        Invoice invoice = invoiceService.getInvoiceByInvoiceIdAndCustomerId(customerId, paymentRequest.getInvoiceId());
        if (invoice == null || invoice.getPay().compareTo(paymentRequest.getPay()) != 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(JtcResponse.badRequest());
        }
        paymentService.createPayment(customerId,paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
