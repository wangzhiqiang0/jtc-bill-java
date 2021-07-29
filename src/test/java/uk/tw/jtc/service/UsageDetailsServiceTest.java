package uk.tw.jtc.service;

import org.junit.jupiter.api.BeforeEach;
import uk.tw.jtc.dao.UsageDetailsDao;

public class UsageDetailsServiceTest {
    UsageDetailsService usageDetailsService;
    UsageDetailsDao usageDetailsDao;

    @BeforeEach
    public void setUp() {

        usageDetailsService = new UsageDetailsService(usageDetailsDao);
    }


}
