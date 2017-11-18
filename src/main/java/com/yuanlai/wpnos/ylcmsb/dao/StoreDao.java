package com.yuanlai.wpnos.ylcmsb.dao;

import java.util.List;
import java.util.Map;

import javax.transaction.xa.XAException;

import com.yuanlai.wpnos.ylcmsb.entity.Store;

/**
 * 门店管理
 * 
 * @author youdd
 *
 */
public interface StoreDao {

	int insertStore(Map<String, Object> storeMap) throws Exception;

	List<Map<String, Object>> listStore(Map<String, Object> storeMap) throws Exception;

	List<Map<String, Object>> dictStore(Map<String, Object> storeMap) throws Exception;

	int countStore(Map<String, Object> storeMap) throws Exception;

	int delectStore(String storeId) throws Exception;

	Store queryStoreById(String storeId) throws Exception;
	
	int updateStore (Store store) throws Exception;
	
	int updateStoreStatus (Map<String, Object> map) throws Exception;
	
}
