package uk.tw.jtc.dao;


import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.CustomerMapperPackage;

import java.util.List;
@Mapper
public interface CustomerMapperPackageDao {
    void createCustomerMapperPackage(CustomerMapperPackage customerMapperPackage);

}
