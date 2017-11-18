package com.yuanlai.wpnos.ylcmsb.controller;

import com.tangdi.def.base.controller.TdBaseController;
import com.tangdi.def.constant.Constant;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.common.TdCommonUtil;
import com.yuanlai.wpnos.ylcmsb.entity.Store;
import com.yuanlai.wpnos.ylcmsb.entity.UserInfo;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;
import com.yuanlai.wpnos.ylcmsb.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 类名称：ChannelController 类描述：查询数据控制层
 */
@SuppressWarnings("Duplicates")
@Controller
@RequestMapping("/store")
public class StoreController extends TdBaseController {

	// 注入服务层
	@Resource
	private StoreService storeService;

	@Autowired
	private MySeqNoService mySeqNoService;

	/**
	 * 门店列表查询
	 * 
	 * @param Store
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/listStore")
	@ResponseBody
	public Map<String, Object> listStore(HttpServletRequest request,Store store, int pageSize, int pageNum) throws Exception {
		log.info("传入的参数:" + store.getStoreName() + "##############" + store.getBranchName());
        Map<String, Object> usrInfo = (Map<String, Object>)request.getAttribute(Constant.USR_INFO);
        store.setTreeCode(usrInfo.get("treeCode").toString());
        Map<String, Object> map = storeService.listStore(store, pageSize, pageNum);

		return genSuccessResult(map);
	}

	/**
	 * 门店下拉列表查询
	 * 
	 * @param Store
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dictStore")
	@ResponseBody
	public Map<String, Object> dictStore(Store store) throws Exception {
		Map<String, Object> map = storeService.dictStore(store);
		return genSuccessResult(map);
	}

	/**
	 * 门店添加
	 * 
	 * @param Store
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addStore")
	@ResponseBody
	public Map<String, Object> addStore(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> paramMap = TdCommonUtil.getParameterMap(request);
            Map<String, Object> usrInfo = (Map<String, Object>)request.getAttribute(Constant.USR_INFO);

            //获取门店对应的treecode
            String parentCode = usrInfo.get("treeCode").toString();
            Integer level = parentCode.length()/5+1;

            //level 最高级别为五级
            if (level > 5) {
                return genErrorResult("该用户所处机构等级下不允许继续创建门店");
            }

			String treeCode = mySeqNoService.getTreeCode(parentCode,level.toString(),"store");
			paramMap.put("treeCode",treeCode);

            log.info("StoreController门店添加参数：" + paramMap);
			resultMap = storeService.addStore(paramMap);
			log.info("StoreController门店添加返回信息：" + resultMap);
			if ("1".equals(resultMap.get("result"))) {
				return genSuccessResult(null != resultMap.get("msg") ? resultMap.get("msg") : "门店添加成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("门店添加失败！", e.getMessage());
		}
		return genErrorResult(null != resultMap.get("msg") ? resultMap.get("msg").toString() : "门店添加失败！");
	}

	@RequestMapping(value = "/deleteStore")
	@ResponseBody
	public Object deleteStore(@RequestParam(value = "storeId", required = false) String[] storeId) throws Exception {
		log.info("传入的参数为：" + storeId);
		try {
			for (String string : storeId) {
				storeService.deleteStore(string);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("删除失败：" + e.getMessage());
			return genErrorResult("删除失败:" + e.getMessage());
		}
		log.info("删除成功");
		return genSuccessResult("删除成功！");
	}
	@RequestMapping(value="/queryStore")
	@ResponseBody
	public Object queryStore(@RequestParam(value="storeId" , required=false) String storeId) throws Exception{
		try {
			log.info("查询传入的参数为：" + storeId);
			Map<String, Object> store = storeService.queryStoreById(storeId);
			log.info("返回的参数为：" + store);
			log.info("图片"+store.get("FJID_PIC_01"));
			return genSuccessResult(store);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询门店信息失败！" + e.getMessage());
		}
		return genErrorResult("查询门店信息失败！");
	}
	
	
	
	@RequestMapping(value="updateStore")
	@ResponseBody
	public Object updateStore(Store store) throws Exception {
		try {
			log.info("修改的参数为:" + store);
			storeService.updateStore(store);
			return genSuccessResult();
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("修改门店信息失败！"+e.getMessage());
		}

		return genErrorResult("修改门店信息失败！");
	}
	@RequestMapping(value="updateStoreStatus")
	@ResponseBody
	public Object setStatus(@RequestParam(value = "storeId", required = false) String storeId, @RequestParam(value = "status", required = false) String status) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("storeId", storeId);
			map.put("storeStatus", status);
			storeService.updateSoreStatus(map);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return genSuccessResult("修改成功!");
	}
}
