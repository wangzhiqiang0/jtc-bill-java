package uk.tw.jtc.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.tw.jtc.dao.InvoiceDao;
import uk.tw.jtc.enums.PayEnum;
import uk.tw.jtc.enums.UsageTypeEnum;
import uk.tw.jtc.mock.InvoiceDaoImpl;
import uk.tw.jtc.model.Invoice;
import uk.tw.jtc.model.Subscript;
import uk.tw.jtc.utils.TestUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InvoiceServiceTest {
    private InvoiceService invoiceService;
    InvoiceDaoImpl invoiceDao = new InvoiceDaoImpl();
    @BeforeEach
    public void setUp() {

        invoiceService = new InvoiceService(invoiceDao,null,null,null);
    }

    @Test
    public void shouldGivenSubscriptAndUsageThenReturnInvoicePayWhenSMSandPhoneExtra(){
        Assertions.assertThat(invoiceService.getTotalInvoicePay(
                new Subscript("customer1", Instant.parse("2021-06-17T15:00:00Z"), TestUtils.packageInfoList.get(0)),
                new HashMap(){{
                    put(UsageTypeEnum.PHONE.getType(),20l);
                    put(UsageTypeEnum.SMS.getType(),20l);

                }})).isEqualTo(new BigDecimal(53));
    }

    @Test
    public void shouldGivenSubscriptAndUsageThenReturnInvoicePa(){
        Assertions.assertThat(invoiceService.getTotalInvoicePay(
                new Subscript("customer1", Instant.parse("2021-06-17T15:00:00Z"), TestUtils.packageInfoList.get(0)),
                new HashMap(){{
                    put(UsageTypeEnum.PHONE.getType(),5l);
                    put(UsageTypeEnum.SMS.getType(),5l);

                }})).isEqualTo(new BigDecimal(38));
    }


}
