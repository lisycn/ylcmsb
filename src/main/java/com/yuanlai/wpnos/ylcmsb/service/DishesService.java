package com.yuanlai.wpnos.ylcmsb.service;

import com.yuanlai.wpnos.ylcmsb.entity.CateringDish;
import com.yuanlai.wpnos.ylcmsb.entity.CateringDishes;

import java.util.Map;

public interface DishesService {

	/**
	 * 分页查询菜品类型
	 *
	 * @param cateringDishes
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> listDishes(CateringDishes cateringDishes, int pageSize, int pageNum) throws Exception;

	/**
	 * 分页查询菜品
	 *
	 * @param cateringDish
	 * @param pageNum
	 * @param numPerPage
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> listDish(CateringDish cateringDish, int pageSize, int pageNum) throws Exception;

	/**
	 * 添加菜品类型
	 * @param dishesMap dishesId系统生成;dishesName必填
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> addDishes(Map<String, Object> dishesMap) throws Exception;
	/**
	 * 更新菜品信息
	 */
	public int updateDishes(CateringDishes cateringDishes) throws Exception;

	/**
	 * 菜品类型下拉选择框
	 * */
	public Map<String,Object> dictDishes(CateringDishes cateringDishes) throws Exception;
	/**
	 * 删除菜品类别
	 */
	public void deleteDishesByUsrIds(String dishId) throws Exception;

	/**
	 * 添加菜品
	 * */
	public Map<String,Object> addDish(Map<String, Object> dishMap) throws Exception;
	/**
	 * 删除菜品
	 */
	public int deleteDish(String dishId) throws Exception;

	/**
	 * 查询菜品信息
	 */
	public Map<String, Object> queryEntityById(String dishId) throws Exception;
	/**
	 * 更新菜品信息
	 */
	public int updateDish(CateringDish cateringDish) throws Exception;

	/*
	* 更改菜品类别禁用状态
	* */
	public void disOrEnable(String dishesId,String status,String currUsername) throws Exception;

}
