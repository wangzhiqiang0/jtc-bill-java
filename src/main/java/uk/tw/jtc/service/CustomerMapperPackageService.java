package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.CustomerMapperPackageDao;
import uk.tw.jtc.model.CustomerMapperPackage;
import uk.tw.jtc.model.PackageInfo;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CustomerMapperPackageService {
    private CustomerMapperPackageDao customerMapperPackageDao;

    public CustomerMapperPackageService(CustomerMapperPackageDao customerMapperPackageDao) {
        this.customerMapperPackageDao = customerMapperPackageDao;
    }

    public void createCustomerMapperPackage(CustomerMapperPackage customerMapperPackage){
        customerMapperPackageDao.createCustomerMapperPackage (customerMapperPackage);
    }

    public void subscriptPackage(String customerId, PackageInfo packageInfo) {
        CustomerMapperPackage customerMapperPackage  = new CustomerMapperPackage(UUID.randomUUID().toString(),
                packageInfo.getPackageId(),customerId, LocalDate.now());
        createCustomerMapperPackage(customerMapperPackage);
    }

}
