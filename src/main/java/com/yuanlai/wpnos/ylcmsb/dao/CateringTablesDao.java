package com.yuanlai.wpnos.ylcmsb.dao;

import com.yuanlai.wpnos.ylcmsb.entity.CateringTable;

import java.util.List;
import java.util.Map;


public interface CateringTablesDao {

    List<Map<String, Object>> listTables(Map<String, Object> tablesMap) throws Exception;

    List<Map<String, Object>> listAccount(Map<String, Object> tableInfo) throws Exception;

    void deleteTables(List<String> tableId) throws Exception ;

    int updateTable(CateringTable cateringTable) throws Exception;

    int addTables(Map<String, Object > insTables) throws Exception;

    int countList(Map<String, Object> tablesMap) throws Exception;

    List<Map<String, Object>> dictStores(Map<String, Object> dishesMap) throws Exception;


}
