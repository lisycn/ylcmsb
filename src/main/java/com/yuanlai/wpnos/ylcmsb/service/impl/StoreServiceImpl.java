package com.yuanlai.wpnos.ylcmsb.service.impl;

import com.google.common.base.Strings;
import com.tangdi.def.utils.exception.TdRuntimeException;
import com.yuanlai.wpnos.ylcmsb.common.AuthBeanUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthCommonUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.common.DateUtils;
import com.yuanlai.wpnos.ylcmsb.dao.*;
import com.yuanlai.wpnos.ylcmsb.entity.*;
import com.yuanlai.wpnos.ylcmsb.service.IPubAttachmentService;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;
import com.yuanlai.wpnos.ylcmsb.service.StoreService;
import com.yuanlai.wpnos.ylcmsb.util.BaseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Service
public class StoreServiceImpl implements StoreService {

	@Resource
	private StoreDao storeMapper;

	@Resource
	private MySeqNoService mySeqNoService;
	@Resource
	private IPubAttachmentService attachmentService;
	private static Logger log = LoggerFactory.getLogger(StoreServiceImpl.class);

	/**
	 * 门店查询
	 */
	@Override
	public Map<String, Object> listStore(Store store, int pageSize, int pageNum) throws Exception {
		Map<String, Object> paraMap = AuthBeanUtil.toMap(store);
		paraMap = AuthCommonUtil.setPageParam(paraMap, pageSize, pageNum);
		try {
			log.info("paraMap:" + paraMap.toString());
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> storeList = storeMapper.listStore(paraMap);
			int total = storeMapper.countStore(paraMap);
			data.put(AuthConstants.COMMON_LIST, storeList);
			data.put(AuthConstants.COMMON_TOTAL, total);
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TdRuntimeException("数据库错误");
		}
	}

	/**
	 * 门店下拉列表查询
	 */
	@Override
	public Map<String, Object> dictStore(Store store) throws Exception {
		Map<String, Object> paraMap = AuthBeanUtil.toMap(store);
		try {
			log.info("paraMap:" + paraMap.toString());
			Map<String, Object> data = new HashMap<String, Object>();
			List<Map<String, Object>> storeList = storeMapper.dictStore(paraMap);
			data.put(AuthConstants.COMMON_LIST, storeList);
			return data;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new TdRuntimeException("数据库错误");
		}
	}

	/**
	 * 添加门店
	 */
	@Override
	public Map<String, Object> addStore(Map<String, Object> storeMap) throws Exception {
		// 返回map创建
		Map<String, Object> resultMap = new HashMap<>();
		// ID，创建时间，更改时间 三要素添加。
		storeMap.put("storeId", mySeqNoService.getSeqNo("STORE_ID", 9));
		storeMap.put("createTime", DateUtils.GETDATETIME());
		storeMap.put("modifyTime", DateUtils.GETDATETIME());
		// 添加
		int i = storeMapper.insertStore(storeMap);
		if (i <= 0) {
			resultMap.put("result", "0");
			resultMap.put("msg", "门店添加失败");
		} else {
			resultMap.put("result", "1");
			resultMap.put("msg", "门店添加成功");
		}
		return resultMap;
	}

	/**
	 * 删除门店
	 */
	@Override
	public int deleteStore(String storeId) throws Exception {
		// TODO Auto-generated method stub
		return storeMapper.delectStore(storeId);
	}

	/**
	 * 查询门店
	 */
	@Override
	public Map<String, Object> queryStoreById(String storeId) throws Exception {
		// TODO Auto-generated method stub
		Store store = storeMapper.queryStoreById(storeId);
		Map<String, Object> resultMap = BaseUtil.convertBean2Map(store);
		// 如果存在图片的话，根据id转换二进制数据。
		if (!Strings.isNullOrEmpty(store.getStoreImage())) {
			Map<String, Object> attPara = new HashMap<String, Object>();
			attPara.put("ID", store.getStoreImage());
			log.info("查询门店图片的参数：" + attPara);
			Map<String, Object> attResult = null;
			try {
				attResult = attachmentService.queryAttachMapNew(attPara);
				log.info("查询门店图片结果：" + attResult);
				/*
				 * app上传的图片，可能为jpg或者png，所以要从FJSRC_image/jpeg_0 或者FJSRC_image/png_0中获取
				 * web上传的图片，从FJSRC_PIC_03中获取。 三者只会有一个不为null
				 */
				Object appJpgPath = attResult.get("FJSRC_image/jpeg_0");
				Object appPngPath = attResult.get("FJSRC_image/png_0");
				Object webPath = attResult.get("FJSRC_PIC_01");
				// 把查询结果放入map中
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
				log.error("查询门店图片错误" + e.getMessage());
			}
		}
		return resultMap;
	}

	@Override
	public int updateStore(Store store) throws Exception {
		// TODO Auto-generated method stub
		return storeMapper.updateStore(store);
	}

	@Override
	public int updateSoreStatus(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		String status = (String) map.get("storeStatus");
		if (status.equals("1")) {
			status = "0";
		} else if (status.equals("0")) {
			status = "1";
		}
		map.put("storeStatus", status);

		return storeMapper.updateStoreStatus(map);
	}
}
