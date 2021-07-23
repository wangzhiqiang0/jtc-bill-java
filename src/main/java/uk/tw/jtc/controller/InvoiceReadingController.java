package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.request.RequestInvoice;
import uk.tw.jtc.response.JwtResponse;
import uk.tw.jtc.service.InvoiceService;

import java.util.List;
import java.util.Optional;

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(JwtResponse.notFound());
        }
        return ResponseEntity.ok(JwtResponse.ok(invoice));
    }

    @PostMapping("/paid")
    public ResponseEntity paidInvoice(@RequestHeader("customerId")String customerId,@RequestBody RequestInvoice requestInvoice) {
        Optional<Invoice> invoiceOptional = invoiceService.getActiveInvoice(customerId).stream().
                filter(e -> e.getInvoiceId().equals(requestInvoice.getInvoiceId())).findFirst();
        if(invoiceOptional.isPresent()){
            if(invoiceOptional.get().getPay().doubleValue() == requestInvoice.getPay().doubleValue()){
                Invoice invoice = invoiceOptional.get();
                invoice.setStatus(PayEnum.PAID.getStatus());
                invoiceService.updateInvoice(invoice);
            }else {
                return ResponseEntity.badRequest().body(JwtResponse.badRequest());
            }

        }else {
            return ResponseEntity.badRequest().body(JwtResponse.badRequest());
        }

        return ResponseEntity.noContent().build();
    }
}
