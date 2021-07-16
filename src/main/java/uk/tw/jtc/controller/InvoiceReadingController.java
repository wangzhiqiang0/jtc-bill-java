package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.service.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceReadingController {
    private InvoiceService invoiceService;

    public InvoiceReadingController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/active")
    public ResponseEntity getActiveInvoice(@RequestHeader("customerId") String customerId) {
        List<Invoice> invoice = invoiceService.getActiveInvoice(customerId);
        if(null == invoice){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invoice);
    }

    @PostMapping("/paid")
    public ResponseEntity paidInvoice(@RequestHeader("customerId")String customerId,@RequestBody Invoice invoice) {
       // Invoice invoiceFromDB = invoiceService.getActiveInvoice(customerId);
//        if(null == invoiceFromDB){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        if(invoiceFromDB.getPay().compareTo(invoice.getPay())!=0){
//           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
        invoiceService.updateInvoice(invoice);
        return ResponseEntity.accepted().build();
    }
}
