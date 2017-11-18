package com.yuanlai.wpnos.ylcmsb.common;

import com.yuanlai.wpnos.ylcmsb.constants.TpConstant;
import com.yuanlai.wpnos.ylcmsb.entity.Dict;
import com.yuanlai.wpnos.ylcmsb.entity.DictObj;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictUtils {
	private static Map<String, DictObj> dictMap = new HashMap<String, DictObj>();
	private static final Logger log = LoggerFactory.getLogger(DictUtils.class);
	
	/**
	 * 更新字典对象至变量Map中
	 * 
	 * @param dictList
	 * @return
	 */
	public static int updateDictMap(List<Dict> dictList) {
		Map<String, DictObj> dictTempMap = new HashMap<String, DictObj>();
		DictObj dRootObj = new DictObj();
		dRootObj.setAttrMap("DICT_NAME", "ROOT");
		dRootObj.setAttrMap("DICT_CODE", "0");
		dRootObj.setAttrMap("DICT_LEVEL", "0");
		dRootObj.setAttrMap("DICT_ID", "0");

		dictTempMap.put("0", dRootObj);

		DictObj dChildObj = new DictObj();
		for (Dict dict : dictList) {
			dChildObj = new DictObj();
			dChildObj.setAttrMap("DICT_LEVEL", dict.getDict_level());
			dChildObj.setAttrMap("PARENT_ID", dict.getParent_id());
			dChildObj.setAttrMap("DICT_ID", dict.getDict_id());
			dChildObj.setAttrMap("ABR", dict.getAbr());
			dChildObj.setAttrMap("DICT_CODE", dict.getDict_code());
			dChildObj.setAttrMap("DICT_NAME", dict.getDict_name());
			dChildObj.setAttrMap("DICT_VALUE", dict.getDict_value());
			dChildObj.setAttrMap("SEQ_NUM", dict.getSeq_num());

			addChild2Root(dRootObj, dChildObj);
		}

		dictMap = dictTempMap;
		log.info("构建对象dictMap完毕！");
		return 0;
	}

	/**
	 * 获得整个字典Map
	 * @return
	 */
	public static Map<String, DictObj> getDictMap() {
		return dictMap;
	}

	/**
	 * 获得字典Code对应的对象
	 * @param dictObj
	 * @param sDictCode
	 * @return
	 */
	public static DictObj getDictObjByCode(DictObj dictObj, String sDictCode) {
		if (null == dictObj) {
			return null;
		}

		DictObj dictObjRes = null;
		String sDictIdTemp = dictObj.getAttrMap("DICT_CODE");
		if (sDictCode.equals(sDictIdTemp)) {
			return dictObj;
		}
		for (DictObj dictObjTemp : dictObj.getChildList()) {
			dictObjRes = getDictObjByCode(dictObjTemp, sDictCode);
			if (null != dictObjRes) {
				return dictObjRes;
			}
		}
		return dictObjRes;
	}

	/**
	 * 将子节点添加到父节点上
	 * @param
	 * */
	public static void addChild2Root(DictObj dRootObj, DictObj dChildObj) {
		// 查找子节点的上级父节点
		DictObj dParentObj = findParent(dRootObj, dChildObj);
		if(dParentObj != null){
			// 将子节点添加到上级父节点的子列表中
			dParentObj.setChild(dChildObj);
		}
	}

	/**
	 * 查找子节点的上级父节点 前提：依赖于查询时的顺序 该节点一定会存在
	 * @param dRootObj
	 *            根节点
	 * @param dRootObj
	 *            查找父节点的子节点
	 * */
	public static DictObj findParent(DictObj dRootObj, DictObj dChildObj) {
		DictObj dParentObj = null;
		// 查找子节点
		String sParentId = dChildObj.getAttrMap("PARENT_ID");
		String sRootId = dRootObj.getAttrMap("DICT_ID");
		if (null != sParentId && sParentId.equals(sRootId)) {
			return dRootObj;
		}
		for (DictObj dTempObj : dRootObj.getChildList()) {
			dParentObj = findParent(dTempObj, dChildObj);
			if (null != dParentObj) {
				break;
			}
		}
		return dParentObj;
	}
	
	public static String getDictName(String sLanguage,DictObj dictObj, String sVal) {
		String sRes = "";
		if(null==dictObj || StringUtils.isEmpty(sVal)){
			return sRes;
		}
		List<DictObj> childList = dictObj.getChildList();
		String sValue           = null;
		String sName            = null;
		for(DictObj dictChild:childList){
			sValue = dictChild.getAttrMap("DICT_VALUE");
			sName  = dictChild.getAttrMap("DICT_NAME");//默认显示中文
			
			//根据前端语言类型，返回不同的语言显示
			if(TpConstant.SYS_LANGUAGE_EN.equalsIgnoreCase(sLanguage)){
				sName  = dictChild.getAttrMap("ABR");//显示翻译内容
			}
			
			if(sVal.equals(sValue)){
				sRes = sName;
				break;
			}
		}
		return sRes;
	}
	
	public static String getDictVal(DictObj dictObj, String sName) {
		String sRes = "";
		if(null==dictObj || StringUtils.isEmpty(sName)){
			return sRes;
		}
		List<DictObj> childList = dictObj.getChildList();
		if(childList.size()==0){
			return sRes;
		}
		String sValueTemp       = null;
		String sNameTemp        = null;
		for(DictObj dictChild:childList){
			sValueTemp = dictChild.getAttrMap("DICT_VALUE");
			sNameTemp  = dictChild.getAttrMap("DICT_NAME");
			if(sName.trim().equals(sNameTemp.trim())){
				return sValueTemp;
			}
			sRes = getDictVal(dictChild,sName);
			if(StringUtils.isNotEmpty(sRes)){
				break;
			}
		}
		return sRes;
	}
	
	public static String getDictVal(DictObj dictObj, String sName,String sLevel) {
		String sRes = "";
		if(null==dictObj || StringUtils.isEmpty(sName)){
			return sRes;
		}
		List<DictObj> childList = dictObj.getChildList();
		if(childList.size()==0){
			return sRes;
		}
		String sValueTemp       = null;
		String sNameTemp        = null;
		String sLevelTemp       = null;
		for(DictObj dictChild:childList){
			sValueTemp = dictChild.getAttrMap("DICT_VALUE");
			sNameTemp  = dictChild.getAttrMap("DICT_NAME");
			sLevelTemp = dictChild.getAttrMap("DICT_LEVEL");
			if(sName.trim().equals(sNameTemp.trim()) && sLevel.trim().equals(sLevelTemp)){
				return sValueTemp;
			}
			sRes = getDictVal(dictChild,sName,sLevel);
			if(StringUtils.isNotEmpty(sRes)){
				break;
			}
		}
		return sRes;
	}
	
	/**
	 * @desc 获得列名中的名称
	 * @param sColName eg:STATUS_CC_ZJLX 
	 * @return STATUS
	 * */
	public static String getAddNodeName(String sColName){
		String sRes = "";
		if(null==sColName){
			return sRes;
		}
		if(sColName.toUpperCase().indexOf("_CC_")!=-1){
			String[] arr = sColName.toUpperCase().split("_CC_");
			sRes = arr[0].trim();
		}
		return sRes;
	}
	
	/**
	 * @desc 获得列名中的数据字典DICT_CODE
	 * @param sColName eg:STATUS_CC_ZJLX 
	 * @return ZJLX
	 * */
	public static String getDictType(String sColName){
		String sRes = "";
		if(null==sColName){
			return sRes;
		}
		if(sColName.toUpperCase().indexOf("_CC_")!=-1){
			String[] arr = sColName.toUpperCase().split("_CC_");
			sRes = arr[arr.length-1].trim();
		}
		return sRes;
	}
	
	/**
	 * @desc 对传入的数据进行星号(*)加密处理
	 * @param sStr 
	 * @return 加密后的数据
	 * */
	public static String getHideStr(String sStr){
		String sRes = "";
		if(null == sStr||0==sStr.length()){
			return sStr;
		}
		int iStr = sStr.length();
		if(1==iStr){
			return "*";
		}else if(2==iStr){
			return sStr.substring(0, 1)+"*";
		}else if(3==iStr){
			return sStr.substring(0, 1)+"*"+sStr.substring(2, 3);
		}else if(4==iStr){
			return sStr.substring(0, 1)+"**"+sStr.substring(3, 4);
		}
		
		if(iStr>4 && iStr<=8){
			return sStr.substring(0, 2)+"****"+sStr.substring(iStr-2, iStr);
		}
		
		if(iStr>8){
			return sStr.substring(0, 4)+"****"+sStr.substring(iStr-4, iStr);
		}
		
		return sRes;
	}
	
	public static void clearDictMap(){
		dictMap = null;
		dictMap = new HashMap<String, DictObj>();
	}
}
