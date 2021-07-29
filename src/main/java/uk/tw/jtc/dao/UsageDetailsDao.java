package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.Usage;

import java.time.Instant;
import java.util.List;
@Mapper
public interface UsageDetailsDao {
    List<Usage> getUsageListByCustomerId(@Param("customerId") String customerId);

    void addNewUsage(@Param("usage") Usage usage);

    List<Usage> getCurrentCycleUsageList();

    List<Usage> getCurrentCycleUsageListByCustomerId(@Param("customerId")String customerId,@Param("start")Instant start,@Param("end")Instant end);
}
