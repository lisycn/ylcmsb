package com.yuanlai.wpnos.ylcmsb.dao;

import com.yuanlai.wpnos.ylcmsb.entity.CateringDishes;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author invlong
 * @createTime 2017-07-24 18:26
 */
public interface CateringDishesDao {
	int insertDishes(Map<String, Object> dishesMap) throws Exception;

	List<Map<String, Object>> listDishes(Map<String, Object> dishesMap) throws Exception;

	int countDishes(Map<String, Object> dishesMap) throws Exception;

	List<Map<String, Object>> listDish(Map<String, Object> dishMap) throws Exception;

	/**
	 * usrIds(001,002,003),usrStatus查询
	 * @return
	 */
	List<CateringDishes> selectStatusDishes(Map<String, Object> map);

	int deleteDishesByDishesIds(List<String> dishesIds);

	List<Map<String, Object>> dictDishes(Map<String, Object> dishesMap) throws Exception;

	int updateDishes(CateringDishes dishes) throws Exception;

	/**
	 * 启用或禁用菜品类别状态
	 * @param dishesId,status,updObj,updTim
	 * @return
	 */
	int disOrEnable(String dishesId,String status,String updObj,String updTim) throws  Exception;
}
