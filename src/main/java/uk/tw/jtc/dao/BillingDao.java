package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.Billing;

import java.util.List;

@Mapper
public interface BillingDao {
    void createNewBill(@Param("billing") Billing billing);
    Billing getBillByCustomerId(@Param("customerId") String customerId);
    void updatePhoneUsed(@Param("billingId") String billingId,@Param("phoneUsed") int phoneUsed);
    void updateSMSUsed(@Param("billingId") String billingId,@Param("smsUsed") int smsUsed);
    void updatePhonePay(@Param("billingId") String billingId,@Param("phonePay") int phonePay);
    void updateSMSPay(@Param("billingId") String billingId,@Param("smsPay") int smsPay);
    void updateFistToFalse(@Param("billingId") String billingId);
    List<Billing>  getBillingList();
}
