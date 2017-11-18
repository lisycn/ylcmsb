package com.yuanlai.wpnos.ylcmsb.dao;

import java.util.List;
import java.util.Map;

import com.yuanlai.wpnos.ylcmsb.entity.CateringDish;

/**
 * ${DESCRIPTION}
 *
 * @author invlong
 * @createTime 2017-07-24 18:25
 */
public interface CateringDishDao {
	int insertDish(Map<String, Object> dishesMap) throws Exception;

	List<Map<String, Object>> listDish(Map<String, Object> dishesMap) throws Exception;

	int countDish(Map<String, Object> dishesMap) throws Exception;

	List<Map<String, Object>> dictDish(Map<String, Object> dishesMap) throws Exception;

	int deleteDish(String dishId) throws Exception;

	CateringDish queryEntityById(String dishId) throws Exception;

	int updateDish(CateringDish dish) throws Exception;

}
