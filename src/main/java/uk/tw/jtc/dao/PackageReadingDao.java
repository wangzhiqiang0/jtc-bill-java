package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.PackageInfo;

@Mapper
public interface PackageReadingDao {

    public PackageInfo getPackageByCustomerID(String customerID);
}
