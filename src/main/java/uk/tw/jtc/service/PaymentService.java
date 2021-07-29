package uk.tw.jtc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.PaymentDao;
import uk.tw.jtc.model.Payment;
import uk.tw.jtc.request.PaymentRequest;


@Service
public class PaymentService {
    private PaymentDao paymentDao;

    public PaymentService(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    public void createPayment(String customerId, PaymentRequest paymentRequest) {
        paymentDao.createPayment(new Payment(paymentRequest.getInvoiceId(),customerId,paymentRequest.getPay()));
    }
}
