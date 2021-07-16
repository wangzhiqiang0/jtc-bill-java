package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.BillingDao;
import uk.tw.jtc.model.Billing;
import uk.tw.jtc.model.PackageInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Setter
@Getter
public class BillingDaoImpl implements BillingDao {
    private List<Billing> billingList;

    @Override
    public void createNewBill(Billing billing) {
        billingList.add(billing);
    }

    @Override
    public void updateBill(Billing billing) {
        billingList = billingList.stream().filter(e -> !e.getBillingId().equals(billing.getBillingId())).collect(Collectors.toList());
        billingList.add(billing);
    }

    @Override
    public Billing getBillByCustomerId(String customerId) {
        Optional<Billing> billing = billingList.stream().filter(e -> e.getCustomerId().equals(customerId)).findFirst();
        if(!billing.isPresent()){
            return null;
        }
        return billing.get();
    }

    @Override
    public void updatePhoneUsed(String billingId, int phoneUsed) {
        Billing billing = billingList.stream().filter( e -> e.getBillingId().equals(billingId)).findFirst().get();
        billing.setPhoneUsed(phoneUsed);
    }

    @Override
    public void updateSMSUsed(String billingId, int smsUsed) {
        Billing billing = billingList.stream().filter( e -> e.getBillingId().equals(billingId)).findFirst().get();
        billing.setSmsUsed(smsUsed);
    }

    @Override
    public void updatePhonePay(String billingId, int phonePay) {
        Billing billing = billingList.stream().filter( e -> e.getBillingId().equals(billingId)).findFirst().get();
        billing.setPhonePay(phonePay);
    }

    @Override
    public void updateSMSPay(String billingId, int smsPay) {
        Billing billing = billingList.stream().filter( e -> e.getBillingId().equals(billingId)).findFirst().get();
        billing.setSmsPay(smsPay);
    }

    @Override
    public List<Billing> getBillingList() {
        return billingList;
    }
}
