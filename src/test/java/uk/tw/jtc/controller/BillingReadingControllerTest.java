package uk.tw.jtc.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class BillingReadingControllerTest {
    private static final String CUSTOMER_ID = "10101010";
    private BillingReadingController billingReadingController;

    @BeforeEach
    public void setUp() {
        this.billingReadingController = new BillingReadingController();
    }
    @Test
    public void givenCustomerIdShouldReturn() {


     //   assertThat(billingReadingController.getCurrentBillingPeriod(CUSTOMER_ID).getBody()).isEqualTo(expectedElectricityReadings);
    }
}
