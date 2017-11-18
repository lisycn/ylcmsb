package com.yuanlai.wpnos.ylcmsb.service;

import com.yuanlai.wpnos.ylcmsb.entity.*;

import java.util.Map;


public interface StoreService {

	/**
	 * 分页查询门店信息
	 * 
	 * @param store
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> listStore(Store store, int pageSize, int pageNum) throws Exception;

	/**
	 * 门店下拉列表查询
	 *
	 * @param store
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> dictStore(Store store) throws Exception;

	/**
	 * 添加门店
	 * @param store storeId系统生成;storeName必填
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> addStore(Map<String,Object> storeMap) throws Exception;
	/**
	 * 删除门店
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	public int deleteStore (String storeId) throws Exception;
	/**
	 * 查询门店
	 * @param storeId
	 * @return
	 * @throws Exception
	 */
	public  Map<String, Object> queryStoreById(String storeId) throws Exception;
	/**
	 * 更新门店
	 * @param store
	 * @return
	 * @throws Exception
	 */
	public int updateStore(Store store) throws Exception;
	
	/**
	 * 
	 * 更改门店状态
	 * @param map
	 * @return
	 */
	public int updateSoreStatus( Map<String, Object> map)  throws Exception;
}
