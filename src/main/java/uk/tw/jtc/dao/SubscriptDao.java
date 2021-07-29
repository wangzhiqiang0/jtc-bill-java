package uk.tw.jtc.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import uk.tw.jtc.model.Subscript;

import java.util.List;

@Mapper
public interface SubscriptDao {
    void createNewSubscript(@Param("subscript") Subscript subscript);
    Subscript getSubscriptByCustomerId(@Param("customerId") String customerId);
    List<Subscript>  getSubscriptList();
}
