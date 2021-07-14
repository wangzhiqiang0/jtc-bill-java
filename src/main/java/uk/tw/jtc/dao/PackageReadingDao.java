package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import uk.tw.jtc.model.PackageInfo;

import java.util.List;

@Mapper
public interface PackageReadingDao {

    PackageInfo getPackageByCustomerID(String customerID);

    List<PackageInfo> listPackages();
}
