package com.yuanlai.wpnos.ylcmsb.dao;

import com.tangdi.def.toolkit.mybatis.data.BaseDao;
import com.yuanlai.wpnos.ylcmsb.entity.CallbackRecord;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayMonthReport;


public interface CodePayMonthReportDao extends BaseDao<CallbackRecord, Exception> {
	
	// 写入月报表
	public void addMonthReport(CodePayMonthReport report) throws Exception;

}