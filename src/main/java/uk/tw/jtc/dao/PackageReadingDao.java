package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.PackageInfo;

import java.util.List;

@Mapper
public interface PackageReadingDao {


    List<PackageInfo> listPackages();

    PackageInfo getPackageById(@Param("packageId") String packageId);
}
