package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.Billing;

import java.util.List;

@Mapper
public interface BillingDao {
    void createNewBill(Billing billing);
    void updateBill(Billing billing);
    Billing getBillByCustomerId(String customerId);
    void updatePhoneUsed(String billingId,int phoneUsed);
    void updateSMSUsed(String billingId,int smsUsed);
    void updatePhonePay(String billingId,int phonePay);
    void updateSMSPay(String billingId,int smsPay);
    List<Billing>  getBillingList();
}
