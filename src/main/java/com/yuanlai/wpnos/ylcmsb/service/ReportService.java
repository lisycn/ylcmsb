package com.yuanlai.wpnos.ylcmsb.service;

import com.yuanlai.wpnos.ylcmsb.entity.CodePayDailyReport;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayMonthReport;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayReport;

import java.util.Map;


/**
 * 报表服务
 * @author Lee
 *
 */
public interface ReportService {
	
	// 生成交易报表
	public void codePayReportTask() throws Exception;

	// 生成日报表任务
	public void dailyReportTask() throws Exception;

	// 生成月报表任务
	public void monthReportTask() throws Exception;
	
	// 查询日报表
	public Map<String, Object> getDailyReportList(CodePayDailyReport report, int pageSize, int pageNum) throws Exception;
	
	// 查询月报表
	public Map<String, Object> getMonthReportList(CodePayMonthReport report, int pageSize, int pageNum) throws Exception;
	
	// 查询总报表
	public Map<String, Object> getTotalReport(CodePayDailyReport report) throws Exception;

	// 查询交易报表
	public Map<String, Object> getCodePayReportList(CodePayReport report, int pageSize, int pageNum) throws Exception;
	
}
