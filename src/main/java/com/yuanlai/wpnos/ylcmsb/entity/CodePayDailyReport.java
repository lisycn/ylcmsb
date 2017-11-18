package com.yuanlai.wpnos.ylcmsb.entity;

import com.yuanlai.wpnos.ylcmsb.model.QueryModel;

import java.util.Date;



public class CodePayDailyReport extends QueryModel {
	
    /**
	 * @param amount
	 * @param totalFee
	 * @param hfFee
	 * @param sourceProfit
	 * @param accessProfit
	 */
	public CodePayDailyReport() {
		super();
		this.amount = 0.00;
		this.totalFee = 0.00;
		this.hfFee = 0.00;
		this.sourceProfit = 0.00;
		this.accessProfit = 0.00;
	}

	private Integer reportId;

	private String accessPart;

	private Date transDate;

	private String channelCode;

	private Double amount;

	private Double totalFee;

	private Double hfFee;

	private Double sourceProfit;

	private Double accessProfit;

	private Date reportTime;

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getAccessPart() {
		return accessPart;
	}

	public void setAccessPart(String accessPart) {
		this.accessPart = accessPart;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getHfFee() {
		return hfFee;
	}

	public void setHfFee(Double hfFee) {
		this.hfFee = hfFee;
	}

	public Double getSourceProfit() {
		return sourceProfit;
	}

	public void setSourceProfit(Double sourceProfit) {
		this.sourceProfit = sourceProfit;
	}

	public Double getAccessProfit() {
		return accessProfit;
	}

	public void setAccessProfit(Double accessProfit) {
		this.accessProfit = accessProfit;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

}