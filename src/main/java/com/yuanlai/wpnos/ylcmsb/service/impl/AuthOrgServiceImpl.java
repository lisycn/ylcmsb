package com.yuanlai.wpnos.ylcmsb.service.impl;

import com.tangdi.def.utils.common.TdDateUtil;
import com.tangdi.def.utils.common.TdStringUtil;
import com.tangdi.def.utils.exception.TdRuntimeException;
import com.yuanlai.wpnos.ylcmsb.common.AuthBeanUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthCommonUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.constants.TpConstant;
import com.yuanlai.wpnos.ylcmsb.dao.AuthOrgDao;
import com.yuanlai.wpnos.ylcmsb.dao.AuthUsrDao;
import com.yuanlai.wpnos.ylcmsb.entity.AuthOrg;
import com.yuanlai.wpnos.ylcmsb.service.AuthOrgService;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.patterns.IVerificationRequired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

@Service
public class AuthOrgServiceImpl implements AuthOrgService {
	@Autowired
	private AuthUsrDao authUsrMapper;
	@Autowired
	private AuthOrgDao authOrgMapper;
	@Resource
	private MySeqNoService seqNoService;
	
	private static Logger log = LoggerFactory.getLogger(AuthOrgServiceImpl.class);
	
	@Override
	public Map<String, Object> selectByPage(AuthOrg authOrg, int pageSize, int pageNum) throws TdRuntimeException {
		log.info("分页查询机构信息");
		Map<String, Object> paraMap = AuthBeanUtil.toMap(authOrg);
		paraMap = AuthCommonUtil.setPageParam(paraMap, pageSize, pageNum);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			log.info("paraMap:"+paraMap.toString());
			List<Map<String,Object>> retList = authOrgMapper.selectByPager(paraMap);
			int total = authOrgMapper.countByCondition(paraMap);
			map.put(AuthConstants.COMMON_LIST, retList);
			map.put(AuthConstants.COMMON_TOTAL, total);
			return map;
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
	}

	@Override
	public void add(AuthOrg authOrg) throws TdRuntimeException {
		log.info("添加机构:"+authOrg.toString());
		Map<String, Object> map = AuthBeanUtil.toMap(authOrg);
		TdStringUtil.chkRequiredParam(map, AuthConstants.ORG_ORGID,AuthConstants.ORG_PARENTORGID,AuthConstants.ORG_ORGNAME);
		if(AuthConstants.ORG_ROOT_ORGID.equals(authOrg.getOrgId())){
			throw new TdRuntimeException("不能操作顶级机构");
		}
		log.info("机构id或名称是否重复");
		List<AuthOrg> list = authOrgMapper.selectRepeatForAdd(authOrg);
		if(!CollectionUtils.isEmpty(list)){
			throw new TdRuntimeException("机构编号或机构名重复");
		}
		authOrg.setCreTim(TdDateUtil.getDateTime());
		String orgId=authOrg.getParentOrgId();
		Map<String,Object>treeCodeMap=authOrgMapper.selectTreeCode(orgId);
		String parentTreeCode=(String) treeCodeMap.get("treeCode");
		if(parentTreeCode==null||parentTreeCode.equals("")) {
			throw new TdRuntimeException("父级机构无TreeCode");
		}
		int length=parentTreeCode.length();
		int level=length/5;
		if (level>=5) {
			throw new TdRuntimeException("所建机构级别超过允许建立的级别");
		}
		try {
			String treeCode=seqNoService.getTreeCode(parentTreeCode, String.valueOf(level+1), TpConstant.ORG_TREE_CODE_TYPE);
			authOrg.setTreeCode(treeCode);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("插入前打印:"+authOrg.toString());
		try {
			authOrgMapper.insertSelective(authOrg);
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
	}

	@Override
	public void update(AuthOrg authOrg) throws TdRuntimeException {
		log.info("更新机构信息:"+authOrg.toString());
		Map<String, Object> map = AuthBeanUtil.toMap(authOrg);
		TdStringUtil.chkRequiredParam(map, AuthConstants.ORG_ORGID,AuthConstants.ORG_ORGNAME);
		if(AuthConstants.ORG_ROOT_ORGID.equals(authOrg.getOrgId())){
			throw new TdRuntimeException("不能操作顶级机构");
		}
		log.info("机构id或名称是否重复");
		List<AuthOrg> list = authOrgMapper.selectRepeatForUpdate(authOrg);
		if(!CollectionUtils.isEmpty(list)){
			throw new TdRuntimeException("机构名重复");
		}
		authOrg.setUpdTim(TdDateUtil.getDateTime());
		log.info("更新前打印:"+authOrg.toString());
		try {
			authOrgMapper.updateByPrimaryKeySelective(authOrg);
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
	}
	@Override
	public void disOrEnable(String isUse, String orgId, String updObj) throws TdRuntimeException {
		log.info("禁用/启用机构isUse={}，orgId={}",isUse,orgId);
		if(AuthConstants.STRING_1.equals(isUse)){
			enable(orgId,updObj);
		}else{
			disable(orgId,updObj);
		}
	}
	private void enable(String orgId, String updObj)throws TdRuntimeException{
		log.info("启用机构: "+orgId);
		AuthOrg org = authOrgMapper.selectByPrimaryKey(orgId);
		if(org == null){
			throw new TdRuntimeException("未查询到机构信息");
		}
		if(AuthConstants.STRING_1.equals(org.getIsUse())){
			throw new TdRuntimeException("机构状态已启用，无需操作");
		}
		if(AuthConstants.ORG_ROOT_ORGID.equals(org.getParentOrgId())){
			throw new TdRuntimeException("不能操作顶级机构");
		}
		AuthOrg parentOrg = authOrgMapper.selectByPrimaryKey(org.getParentOrgId());
		if(parentOrg == null){
			throw new TdRuntimeException("未查询到父级机构信息");
		}
		if(AuthConstants.STRING_0.equals(parentOrg.getIsUse())){
			throw new TdRuntimeException("请先启用父级机构");
		}
		log.info("启用机构");
		try {
			authOrgMapper.disOrEnable(AuthConstants.STRING_1, orgId,updObj,TdDateUtil.getDateTime());
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
	}
	private void disable(String orgId, String updObj)throws TdRuntimeException{
		log.info("禁用机构，orgId= "+orgId);
		AuthOrg org = authOrgMapper.selectByPrimaryKey(orgId);
		if(org == null){
			throw new TdRuntimeException("未查询到机构信息");
		}
		if(AuthConstants.STRING_0.equals(org.getIsUse())){
			throw new TdRuntimeException("机构状态已禁用，无需操作");
		}
		if(AuthConstants.ORG_ROOT_ORGID.equals(org.getParentOrgId())){
			throw new TdRuntimeException("不能操作顶级机构");
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(AuthConstants.ORG_PARENTORGID, orgId);
		map.put(AuthConstants.COMMON_ISUSE, AuthConstants.STRING_1);
		List<AuthOrg> childList = authOrgMapper.selectByCondition(map);
		if(CollectionUtils.isNotEmpty(childList)){
			throw new TdRuntimeException("请先禁用子机构");
		}
		log.info("禁用机构");
		try {
			authOrgMapper.disOrEnable(AuthConstants.STRING_0, orgId,updObj,TdDateUtil.getDateTime());
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
		
	}

	@Override
	public void delByOrgId(String orgId) throws TdRuntimeException {
		log.info("删除机构orgIds="+orgId);
		AuthOrg org = authOrgMapper.selectByPrimaryKey(orgId);
		if(org == null){
			throw new TdRuntimeException("未查询到机构信息");
		}
		if(AuthConstants.ORG_ROOT_ORGID.equals(org.getParentOrgId())){
			throw new TdRuntimeException("不能操作顶级机构");
		}
		if(AuthConstants.STRING_1.equals(org.getIsUse())){
			throw new TdRuntimeException("机构状态为启用，请禁用后删除");
		}
		List<AuthOrg> list = authOrgMapper.selectByParentOrgId(orgId);
		if(CollectionUtils.isNotEmpty(list)){
			throw new TdRuntimeException("请先删除子机构");
		}
		try {
			authUsrMapper.updateOrgIdEmpty(orgId);
			authOrgMapper.deleteByPrimaryKey(orgId);
		} catch (Exception e) {
			throw new TdRuntimeException("数据库错误");
		}
	}

	@Override
	public Map<String, Object> selectByOrgId(String orgId) throws TdRuntimeException {
		 Map<String, Object> map = new HashMap<String, Object>();
		AuthOrg authOrg = authOrgMapper.selectByPrimaryKey(orgId);
		map.put(AuthConstants.ORG_AUTHORG, authOrg);
		return map;
	}
	@Override
	public Map<String, Object> selectOrgTreeTable(AuthOrg org) throws TdRuntimeException {
		log.info("查询机构的树状表格:"+org.toString());
		if(StringUtils.isEmpty(org.getOrgId()) && StringUtils.isEmpty(org.getOrgName())){//查询条件为空，查询全部机构的权限树
			org.setParentOrgId(AuthConstants.ORG_ROOT_ORGID);
		}
		Map<String, Object> paraMap = AuthBeanUtil.toMap(org);
		log.info("查询顶级机构");
		List<Map<String, Object>> parentOrgs = authOrgMapper.selectOrgTreeForTable_parent(paraMap);
		log.info("查询全部机构总数");
		int count = authOrgMapper.countOrgTreeForTable_child(paraMap);
		paraMap.put(AuthConstants.COMMON_LIMIT, count);
		log.info("查询全部机构");
		List<Map<String, Object>> childOrgs = authOrgMapper.selectOrgTreeForTable_child(paraMap);
		Map<String, Object> retmap = new HashMap<String, Object>();
		retmap.put(AuthConstants.COMMON_LIST, getOrgTree(parentOrgs,childOrgs));
		return retmap;
	}
	/**
	 * 循环父级机构，组装机构的树状表格,父机构的key在sql中查询
	 * @param parentOrgs
	 * @param childOrgs
	 * @return
	 * @throws TdRuntimeException
	 */
	private List<Map<String,Object>> getOrgTree(List<Map<String, Object>> parentOrgs,List<Map<String, Object>> childOrgs)throws TdRuntimeException{
		List<Map<String,Object>> tree = new ArrayList<Map<String,Object>>();
		for(Map<String, Object> parentOrg : parentOrgs){
			tree.add(parseForOneParent(parentOrg,childOrgs));
		}
		return tree;
	}
	/**
	 * 组装一个父级机构的树,子机构的key在sql中查询
	 * @param parentOrg
	 * @param childOrgs
	 * @return
	 * @throws TdRuntimeException
	 */
	private Map<String,Object> parseForOneParent(Map<String, Object> parentOrg,List<Map<String, Object>> childOrgs)throws TdRuntimeException{
		List<Map<String,Object>> tree = new ArrayList<Map<String,Object>>();
		for(int i=0; i<childOrgs.size(); i++){
			Map<String, Object> childOrg = childOrgs.get(i);
			if(parentOrg.get(AuthConstants.ORG_ORGID).equals(childOrg.get(AuthConstants.ORG_PARENTORGID))){
				tree.add(parseForOneParent(childOrg,childOrgs));
			}
		}
		if(tree.size() > 0){
			parentOrg.put(AuthConstants.COMMON_CHILDREN, tree);
//			for(Map<String,Object> childOrg : tree){
//				childOrgs.remove(childOrg);
//			}
		}
		return parentOrg;
	}
	@Override
	public Map<String, Object> selectDropdownListTree(AuthOrg authOrg) throws TdRuntimeException {
		authOrg.setOrgId(null);
//		authOrg.setIsUse(AuthConstants.STRING_1);
		return selectOrgTreeTable(authOrg);
	}
}
