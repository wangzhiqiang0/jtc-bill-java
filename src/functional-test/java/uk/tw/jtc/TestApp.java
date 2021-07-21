package uk.tw.jtc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.tw.jtc.dao.PackageReadingDao;

@SpringBootApplication
@EnableTransactionManagement
public class TestApp {

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }



}
