package uk.tw.jtc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceReadingController {
    private InvoiceService invoiceService;

    public InvoiceReadingController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/activeInvoice")
    public ResponseEntity getActiveInvoice(String customerId) {
        Invoice invoice = invoiceService.getActiveInvoice(customerId);
        return ResponseEntity.ok(invoice);
    }
}
