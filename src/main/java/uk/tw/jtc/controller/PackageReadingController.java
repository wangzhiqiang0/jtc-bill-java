package uk.tw.jtc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.jtc.model.PackageInfo;
import uk.tw.jtc.service.PackageReadingService;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageReadingController {
    private PackageReadingService packageReadingService;

    public PackageReadingController(PackageReadingService packageReadingService) {
        this.packageReadingService = packageReadingService;
    }

    @GetMapping("/subscriptionPackage")
    public ResponseEntity getSubscriptionPackage(@RequestHeader("customerId") String customerId){
        PackageInfo packageInfo = packageReadingService.getPackageByCustomerID(customerId);
        if (packageInfo ==null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(packageInfo);
    }

    @GetMapping("/listPackages")
    public ResponseEntity listPackages(){
        List<PackageInfo> packageInfoList = packageReadingService.listPackages();
        if (packageInfoList ==null || packageInfoList.size() ==0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(packageInfoList);
    }
}