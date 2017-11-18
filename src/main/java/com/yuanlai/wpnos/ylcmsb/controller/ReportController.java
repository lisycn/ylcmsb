/**
 * 
 */
package com.yuanlai.wpnos.ylcmsb.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.constants.SourceConstants;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayDailyReport;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayMonthReport;
import com.yuanlai.wpnos.ylcmsb.entity.CodePayReport;
import com.yuanlai.wpnos.ylcmsb.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tangdi.def.base.controller.TdBaseController;
import com.tangdi.def.constant.Constant;


/**
 * @author Lee
 *
 */
@Controller
@RequestMapping("report")
public class ReportController extends TdBaseController {
	
	@Resource
	private ReportService reportService;
	
	/**
	 * 查询交易报表
	 * @param report
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "codePayReport")
	@ResponseBody
	public Map<String, Object> queryCodePayReportList(HttpServletRequest request, CodePayReport report, int pageSize, int pageNum) throws Exception {
		Map<String, Object> usrInfo = (Map<String, Object>) request.getAttribute(Constant.USR_INFO);
		String orgId = (String) usrInfo.get(AuthConstants.ORG_ORGID);
		// 系统管理员能能查询所有
		if (!SourceConstants.ROOT_ORG_ID.equals(orgId)) {
			report.setAccessPart(orgId);
		}
		Map<String, Object> map = reportService.getCodePayReportList(report, pageSize, pageNum);
		log.info("返回结果:" + map);
		return genSuccessResult(map);
	}
	
	/**
	 * 查询日报表
	 * @param report
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "dailyReport")
	@ResponseBody
	public Map<String, Object> queryDailyReportList(HttpServletRequest request, CodePayDailyReport report, int pageSize, int pageNum) throws Exception {
		Map<String, Object> usrInfo = (Map<String, Object>) request.getAttribute(Constant.USR_INFO);
		String orgId = (String) usrInfo.get(AuthConstants.ORG_ORGID);
		// 系统管理员能能查询所有
		if (!SourceConstants.ROOT_ORG_ID.equals(orgId)) {
			report.setAccessPart(orgId);
		}
		Map<String, Object> map = reportService.getDailyReportList(report, pageSize, pageNum);
		log.info("返回结果:" + map);
		return genSuccessResult(map);
	}
	
	/**
	 * 查询月报表
	 * @param report
	 * @param pageSize
	 * @param pageNum
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "monthReport")
	@ResponseBody
	public Map<String, Object> queryMonthReportList(HttpServletRequest request, CodePayMonthReport report, int pageSize, int pageNum) throws Exception {
		Map<String, Object> usrInfo = (Map<String, Object>) request.getAttribute(Constant.USR_INFO);
		String orgId = (String) usrInfo.get(AuthConstants.ORG_ORGID);
		// 系统管理员能能查询所有
		if (!SourceConstants.ROOT_ORG_ID.equals(orgId)) {
			report.setAccessPart(orgId);
		}
		Map<String, Object> map = reportService.getMonthReportList(report, pageSize, pageNum);
		log.info("返回结果:" + map);
		return genSuccessResult(map);
	}
	
	/**
	 * 查询总报表
	 * @param report
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "totalReport")
	@ResponseBody
	public Map<String, Object> queryTotalReportList(CodePayDailyReport report) throws Exception {
		Map<String, Object> map = reportService.getTotalReport(report);
		return genSuccessResult(map);
	}

}
