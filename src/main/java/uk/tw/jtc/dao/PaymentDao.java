package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.Payment;

import java.util.List;

@Mapper
public interface PaymentDao {
     void createPayment(@Param("payment") Payment payment);
    List<Payment> getSubscriptByCustomerId(@Param("customerId") String customerId);
}
