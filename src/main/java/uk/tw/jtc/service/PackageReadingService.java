package uk.tw.jtc.service;

import org.springframework.stereotype.Service;
import uk.tw.jtc.dao.PackageReadingDao;
import uk.tw.jtc.model.PackageInfo;

import java.util.List;

@Service
public class PackageReadingService {
    private PackageReadingDao packageReadingDao;


    public PackageReadingService(PackageReadingDao packageReadingDao) {
        this.packageReadingDao = packageReadingDao;
    }

    public PackageInfo getPackageById(String packageId){
        return packageReadingDao.getPackageById(packageId);
    }

    public List<PackageInfo> listPackages() {
        return packageReadingDao.listPackages();
    }


}
