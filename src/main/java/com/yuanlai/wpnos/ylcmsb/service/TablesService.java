package com.yuanlai.wpnos.ylcmsb.service;

import com.yuanlai.wpnos.ylcmsb.entity.CateringTable;

import java.util.Map;

public interface TablesService {

    Map<String, Object> listTables(CateringTable cateringTable, int pageSize, int pageNum) throws Exception;

    Map<String, Object> updateTable(CateringTable cateringTable) throws Exception;

    Map<String, Object> addTables(Map<String, Object> addTables) throws Exception;

    void deleteTable(String tableId) throws Exception;

    Map<String, Object> listAccount(Map<String, Object> tableInfo) throws Exception;

    /**
     * 菜品类型下拉选择框
     * */
    public Map<String,Object> dictStores(CateringTable cateringTable) throws Exception;

}
