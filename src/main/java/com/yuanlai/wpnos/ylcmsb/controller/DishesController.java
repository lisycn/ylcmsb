package com.yuanlai.wpnos.ylcmsb.controller;

import com.tangdi.def.base.controller.TdBaseController;
import com.tangdi.def.constant.Constant;
import com.tangdi.def.utils.exception.TdRuntimeException;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.common.TdCommonUtil;
import com.yuanlai.wpnos.ylcmsb.entity.CateringDish;
import com.yuanlai.wpnos.ylcmsb.entity.CateringDishes;
import com.yuanlai.wpnos.ylcmsb.service.DishesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：DishesController 类描述：菜品控制
 */
@SuppressWarnings("Duplicates")
@Controller
@RequestMapping("/dishes")
public class DishesController extends TdBaseController {

	// 注入服务层
	@Resource
	private DishesService dishesService;

	/**
	 * 菜品类别列表查询
	 * 
	 * @param cateringDishes
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listDishes")
	@ResponseBody
	public Map<String, Object> listStore(CateringDishes cateringDishes, int pageSize, int pageNum) throws Exception {
		Map<String, Object> map = dishesService.listDishes(cateringDishes, pageSize, pageNum);
		return genSuccessResult(map);
	}

	/**
	 * 菜品类别添加
	 * 
	 * @param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addDishes")
	@ResponseBody
	public Map<String, Object> addDishes(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = TdCommonUtil.getParameterMap(request);
			log.info("DishesController菜品类别添加参数：" + paramMap);
			resultMap = dishesService.addDishes(paramMap);
			log.info("DishesController菜品类别添加返回信息：" + resultMap);
			if ("1".equals(resultMap.get("result"))) {
				return genSuccessResult(null != resultMap.get("msg") ? resultMap.get("msg") : "菜品类别添加成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("菜品类别添加失败！", e.getMessage());
		}
		return genErrorResult(null != resultMap.get("msg") ? resultMap.get("msg").toString() : "菜品类别添加失败！");
	}

	/**
	 * 菜品类别修改
	 *
	 * @param dishes
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updateDishes")
	public Object updateDishes(CateringDishes dishes) throws Exception {
		try {
			log.info("修改的参数为:" + dishes);
			dishesService.updateDishes(dishes);
			return genSuccessResult();

		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改菜品类型信息失败！"+e.getMessage());
		}

		return genErrorResult("修改菜品类型信息失败！");

	}

	/**
	 * 菜品类别删除
	 *
	 * @param dishesId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteDishes")
	@ResponseBody
	public Object deleteDishes(@RequestParam(value = "dishesId", required = false) String[] dishesId) throws Exception {
		log.info("传入的参数为：" + dishesId);
		try {
			for (String string : dishesId) {
				dishesService.deleteDishesByUsrIds(string);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("删除失败：" + e.getMessage());
			return genErrorResult("删除失败:" + e.getMessage());
		}
		log.info("删除成功");
		return genSuccessResult("删除成功！");
	}

	@RequestMapping(value = "/disOrEnable")
	@ResponseBody
	public Map<String,Object> disOrEnable(HttpServletRequest request,@RequestParam("enable") String enable,@RequestParam("dishesId") String dishesId) throws TdRuntimeException{
		log.info("传入的参数为：" + dishesId);
		try {
			Map<String, Object> userInfo = (Map<String, Object>) request.getAttribute(Constant.USR_INFO);
			String currUsername = String.valueOf(userInfo.get(AuthConstants.USR_USRNAME));
			dishesService.disOrEnable(dishesId,enable,currUsername);
		}catch (Exception e){
			log.error("修改失败：" + e.getMessage());
			return genErrorResult("修改失败:" + e.getMessage());
		}
		log.info("修改成功");
		return genSuccessResult("修改成功！");
	}
	/**
	 * 菜品下拉列表
	 *
	 */
	@RequestMapping(value = "/dictDishes")
	@ResponseBody
	public Map<String, Object> dictDishes(CateringDishes cateringDishes) throws Exception {
		Map<String, Object> map = dishesService.dictDishes(cateringDishes);
		return genSuccessResult(map);
	}

	/**
	 * 菜品添加
	 *
	 */
	@RequestMapping(value = "/addDish")
	@ResponseBody
	public Map<String, Object> addDish(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = TdCommonUtil.getParameterMap(request);
			log.info("DishesController菜品添加参数：" + paramMap);
			resultMap = dishesService.addDish(paramMap);
			log.info("DishesController菜品添加返回信息：" + resultMap);
			if ("1".equals(resultMap.get("result"))) {
				return genSuccessResult(null != resultMap.get("msg") ? resultMap.get("msg") : "菜品添加成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("菜品添加失败！", e.getMessage());
		}
		return genErrorResult(null != resultMap.get("msg") ? resultMap.get("msg").toString() : "菜品添加失败！");
	}

	/**
	 * 菜品列表查询
	 * 
	 * @param
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listDish")
	@ResponseBody
	public Map<String, Object> listStore(CateringDish cateringDish, int pageSize, int pageNum) throws Exception {
		Map<String, Object> map = dishesService.listDish(cateringDish, pageSize, pageNum);
		return genSuccessResult(map);
	}

	/**
	 * 菜品删除
	 * 
	 * @param dishId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteDish")
	@ResponseBody
	public Object deleteDish(@RequestParam(value = "dishId", required = false) String[] dishId) throws Exception {
		log.info("传入的参数为：" + dishId);
		try {
			for (String string : dishId) {
				dishesService.deleteDish(string);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("删除失败：" + e.getMessage());
			return genErrorResult("删除失败:" + e.getMessage());
		}
		log.info("删除成功");
		return genSuccessResult("删除成功！");
	}

	/**
	 * 菜品查询
	 * 
	 * @param dishId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryDish")
	@ResponseBody
	public Object queryDish(@RequestParam(value = "dishId", required = false) String dishId) throws Exception {
		try {
			log.info("查询传入的参数为：" + dishId);
			Map dish = dishesService.queryEntityById(dishId);
			log.info("返回的参数为：" + dish);
			return genSuccessResult(dish);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询菜品信息失败！" + e.getMessage());
		}
		return genErrorResult("查询菜品信息失败！");
	}

	/**
	 * 修改菜品
	 * 
	 * @param dish
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updateDish")
	public Object updateDish(CateringDish dish) throws Exception {
		try {
			log.info("修改的参数为:" + dish);
			dishesService.updateDish(dish);
			return genSuccessResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改菜品信息失败！"+e.getMessage());
		}

		return genErrorResult("修改菜品信息失败！");

	}
}
