package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.Billing;

@Mapper
public interface BillingDao {
    void createNewBill(Billing billing);
    void updateBill(Billing billing);
}
