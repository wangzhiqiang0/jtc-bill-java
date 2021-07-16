package uk.tw.jtc.mock;

import lombok.Getter;
import lombok.Setter;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;

import java.util.List;

@Setter
@Getter
public class PackageReadingDaoImpl implements PackageReadingDao {
    private PackageInfo packageInfo;
    private List<PackageInfo> packageInfoList;


    @Override
    public List<PackageInfo> listPackages() {
        return packageInfoList;
    }

    @Override
    public PackageInfo getPackageById(String packageId) {
        return packageInfo;
    }
}
