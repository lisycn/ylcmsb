package com.yuanlai.wpnos.ylcmsb.service.impl;

import com.google.common.base.Strings;
import com.tangdi.def.base.redis.TdRedisService;
import com.tangdi.def.utils.common.TdDateUtil;
import com.tangdi.def.utils.exception.TdRuntimeException;
import com.yuanlai.wpnos.ylcmsb.common.AuthBeanUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthCommonUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.common.DateUtils;
import com.yuanlai.wpnos.ylcmsb.dao.CateringDishDao;
import com.yuanlai.wpnos.ylcmsb.dao.CateringDishesDao;
import com.yuanlai.wpnos.ylcmsb.entity.CateringDish;
import com.yuanlai.wpnos.ylcmsb.entity.CateringDishes;
import com.yuanlai.wpnos.ylcmsb.service.DishesService;
import com.yuanlai.wpnos.ylcmsb.service.IPubAttachmentService;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;
import com.yuanlai.wpnos.ylcmsb.util.BaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("Duplicates")
@Service
public class DishesServiceImpl implements DishesService {
	

	@Resource
	private CateringDishesDao cateringDishesMapper;

	@Resource
	private CateringDishDao cateringDishMapper;

	@Resource
	private MySeqNoService mySeqNoService;

	@Autowired
	private TdRedisService tdRedisService;

    @Resource
    private IPubAttachmentService attachmentService;

	private static Logger log = LoggerFactory.getLogger(DishesServiceImpl.class);

	/**
	 * 菜品类型查询
	 * */
	@Override
	public Map<String, Object> listDishes(CateringDishes cateringDishes, int pageSize, int pageNum) throws Exception {
		Map<String, Object> paraMap = AuthBeanUtil.toMap(cateringDishes);
        paraMap = AuthCommonUtil.setPageParam(paraMap, pageSize, pageNum);
		try {
			log.info("paraMap:" + paraMap.toString());
			Map<String, Object> data = new HashMap<String,Object>();
			List<Map<String, Object>> dishesList = cateringDishesMapper.listDishes(paraMap);
			int total = cateringDishesMapper.countDishes(paraMap);
			data.put(AuthConstants.COMMON_LIST, dishesList);
			data.put(AuthConstants.COMMON_TOTAL, total);
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TdRuntimeException("数据库错误");
		}
	}

	@Override
	public Map<String,Object> addDishes(Map<String,Object> dishesMap) throws Exception {
		//返回map创建
		Map<String,Object> resultMap = new HashMap<>();
		//ID，创建时间，更改时间 三要素添加。
		dishesMap.put("dishesId",mySeqNoService.getSeqNo("DISHES_ID",4));
		dishesMap.put("createTime", DateUtils.GETDATETIME());
		dishesMap.put("modifyTime", DateUtils.GETDATETIME());
		//默认启用
		dishesMap.put("enabled","01");

		//添加
		int i = cateringDishMapper.insertDish(dishesMap);
		if(i<=0){
			resultMap.put("result","0");
			resultMap.put("msg","菜品类型添加失败");
		}else{
			resultMap.put("result","1");
			resultMap.put("msg","菜品类型添加成功");
		}
		return resultMap;
	}

    @Override
    public int updateDishes(CateringDishes cateringDishes) throws Exception {
        // TODO Auto-generated method stub
        return cateringDishesMapper.updateDishes(cateringDishes);
    }

    /**
	 * 菜品类型，下拉选择框
	 * */
	@Override
	public Map<String, Object> dictDishes(CateringDishes cateringDishes) throws Exception {
		Map<String, Object> paraMap = AuthBeanUtil.toMap(cateringDishes);
		try {
			log.info("paraMap:" + paraMap.toString());
			Map<String, Object> data = new HashMap<String,Object>();
			List<Map<String, Object>> dishesList = cateringDishesMapper.dictDishes(paraMap);
			data.put(AuthConstants.COMMON_LIST, dishesList);
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TdRuntimeException("数据库错误");
		}
	}

    /**
     * 菜品类型批量删除
     * */
    @Override
    public void deleteDishesByUsrIds(String dishesId) throws TdRuntimeException {
        log.info("批量删除菜品类别，usrIds:" + dishesId);
        List<String> idsList = AuthCommonUtil.handleIds(dishesId, AuthConstants.COMMON_COMMA);
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(AuthConstants.ENABLED, AuthConstants.STRING_1);//0-禁用，1-启用
        paraMap.put(AuthConstants.COMMON_LIST, idsList);
        List<CateringDishes> list = cateringDishesMapper.selectStatusDishes(paraMap);
        if (list != null && list.size() > 0) {
            throw new TdRuntimeException("有 " + list.size() + " 个菜品类别状态为启用");
        }
        try {
            cateringDishesMapper.deleteDishesByDishesIds(idsList);
        } catch (Exception e) {
            throw new TdRuntimeException("数据库错误");
        }

    }

    @Override
	public Map<String,Object> addDish(Map<String,Object> dishMap) throws Exception {
		//返回map创建
		Map<String,Object> resultMap = new HashMap<>();
		//ID，创建时间，更改时间 三要素添加。
		dishMap.put("dishId",mySeqNoService.getSeqNo("DISH_ID",5));
		dishMap.put("createTime", DateUtils.GETDATETIME());
		dishMap.put("modifyTime", DateUtils.GETDATETIME());
		//元转分
		dishMap.put("price", BaseUtil.element2Branch(String.valueOf(dishMap.get("price"))));
		dishMap.put("discountPrice", BaseUtil.element2Branch(String.valueOf(dishMap.get("discountPrice"))));
		//默认启用
		dishMap.put("enabled","01");

		//添加
		int i = cateringDishMapper.insertDish(dishMap);
		if(i<=0){
			resultMap.put("result","0");
			resultMap.put("msg","菜品添加失败");
		}else{
			resultMap.put("result","1");
			resultMap.put("msg","菜品添加成功");
		}
		return resultMap;
	}

	/**
	 * 菜品
	 * */
	@Override
	public Map<String, Object> listDish(CateringDish cateringDish, int pageSize, int pageNum) throws Exception {
		Map<String, Object> paraMap = AuthBeanUtil.toMap(cateringDish);
		paraMap = AuthCommonUtil.setPageParam(paraMap, pageSize, pageNum);
		try {
			log.info("paraMap:" + paraMap.toString());
			Map<String, Object> data = new HashMap<String,Object>();
			List<Map<String, Object>> dishList = cateringDishMapper.listDish(paraMap);
			int total = cateringDishMapper.countDish(paraMap);
			data.put(AuthConstants.COMMON_LIST, dishList);
			data.put(AuthConstants.COMMON_TOTAL, total);
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TdRuntimeException("数据库错误");
		}
	}

    @Override
    public int deleteDish(String dishId) throws Exception {
        // TODO Auto-generated method stub
        return cateringDishMapper.deleteDish(dishId);

    }

	@Override
	public Map<String, Object> queryEntityById(String dishId) throws Exception {
        CateringDish cateringDish = cateringDishMapper.queryEntityById(dishId);
        Map<String, Object> resultMap = BaseUtil.convertBean2Map(cateringDish);
        //如果存在图片的话，根据id转换二进制数据。
        if (!Strings.isNullOrEmpty(cateringDish.getDishImage())) {
            Map<String, Object> attPara = new HashMap<String, Object>();
            attPara.put("ID", cateringDish.getDishImage());
            log.info("查询菜品图片1的参数：" + attPara);
            Map<String, Object> attResult = null;
            try {
                attResult = attachmentService.queryAttachMapNew(attPara);
                log.info("查询菜品图片1结果：" + attResult);
                /*
                 * app上传的图片，可能为jpg或者png，所以要从FJSRC_image/jpeg_0 或者FJSRC_image/png_0中获取
                 * web上传的图片，从FJSRC_PIC_03中获取。
                 * 三者只会有一个不为null
                 */
                Object appJpgPath = attResult.get("FJSRC_image/jpeg_0");
                Object appPngPath = attResult.get("FJSRC_image/png_0");
                Object webPath = attResult.get("FJSRC_PIC_01");
                //把查询结果放入map中
                if (null != appJpgPath) {
                    resultMap.put("FJSRC_PIC_01", appJpgPath);
                }
                if (null != appPngPath) {
                    resultMap.put("FJSRC_PIC_01", appPngPath);
                }
                if (null != webPath) {
                    resultMap.put("FJSRC_PIC_01", webPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("查询菜品图片1错误" + e.getMessage());
            }
        }
        if (!Strings.isNullOrEmpty(cateringDish.getDishImage2())) {
            Map<String, Object> attPara = new HashMap<String, Object>();
            attPara.put("ID", cateringDish.getDishImage2());
            log.info("查询菜品图片2的参数：" + attPara);
            Map<String, Object> attResult = null;
            try {
                attResult = attachmentService.queryAttachMapNew(attPara);
                log.info("查询菜品图片2的结果：" + attResult);
                /*
                 * app上传的图片，可能为jpg或者png，所以要从FJSRC_image/jpeg_0 或者FJSRC_image/png_0中获取
                 * web上传的图片，从FJSRC_PIC_03中获取。
                 * 三者只会有一个不为null
                 */
                Object appJpgPath = attResult.get("FJSRC_image/jpeg_0");
                Object appPngPath = attResult.get("FJSRC_image/png_0");
                Object webPath = attResult.get("FJSRC_PIC_02");
                //把查询结果放入map中
                if (null != appJpgPath) {
                    resultMap.put("FJSRC_PIC_02", appJpgPath);
                }
                if (null != appPngPath) {
                    resultMap.put("FJSRC_PIC_02", appPngPath);
                }
                if (null != webPath) {
                    resultMap.put("FJSRC_PIC_02", webPath);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("查询菜品图片2错误" + e.getMessage());
            }
        }
        return resultMap;
	}

	@Override
	public int updateDish(CateringDish cateringDish) throws Exception {
		// TODO Auto-generated method stub
		return cateringDishMapper.updateDish(cateringDish);
	}

    /**
     * 菜品类型状态修改
     * */
    @Override
    public void disOrEnable(String dishesId,String status,String currUsername) throws Exception{
        log.info("禁用/启用菜品类别，enable = {},dishesId = {}",status,dishesId);

        if (AuthConstants.STRING_1.equals(status)) {//启用

        } else if (AuthConstants.STRING_0.equals(status)) {//禁用
            Object tokenObj = tdRedisService.getObject(dishesId);
            if (tokenObj != null) {
                String token = String.valueOf(tokenObj);
                Map map = (Map) tdRedisService.getObject(String.valueOf(token));
                if (map != null) {
                    log.info("修改redis中的用户状态");
                    map.put(AuthConstants.ENABLED, status);
                    tdRedisService.saveObject(token, map, 30 * 60);
                }
            }
        } else {
            throw new TdRuntimeException("禁用/启用标志只能是0或者1");
        }

        try {
            cateringDishesMapper.disOrEnable(dishesId, status, currUsername, TdDateUtil.getDateTime());
        } catch (Exception e) {
            throw new TdRuntimeException("数据库错误");
        }
    }
}
