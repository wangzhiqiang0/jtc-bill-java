package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.PaymentDao;
import uk.tw.jtc.model.Payment;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class PaymentDaoImpl implements PaymentDao {
    private List<Payment> paymentList;
    @Override
    public void createPayment(Payment payment) {
        paymentList.add(payment);
    }

    @Override
    public List<Payment> getSubscriptByCustomerId(String customerId) {
        return paymentList.stream().filter(
                e -> e.getCustomerId().equals(customerId)
        ).collect(Collectors.toList());
    }
}
