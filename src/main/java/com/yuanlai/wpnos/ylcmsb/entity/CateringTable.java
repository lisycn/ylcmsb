package com.yuanlai.wpnos.ylcmsb.entity;

public class CateringTable {
  private String tableId;
  private String tableNum;
  private String tableName;
  private String storeId;
  private String ticket;
  private String url;
  private String redirectUrl;
  private String openStatus;

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getOpenStatus() {
    return openStatus;
  }

  public void setOpenStatus(String openStatus) {
    this.openStatus = openStatus;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  private String createTime;
  private String createUser;

  public void setTableId(String tableId) {
    this.tableId = tableId;
  }

  public void setTableNum(String tableNum) {
    this.tableNum = tableNum;
  }

  public void setStoreId(String storeId) {
    this.storeId = storeId;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setRedirectUrl(String redirectUrl) {
    this.redirectUrl = redirectUrl;
  }

  public String getTableId() {

    return tableId;
  }

  public String getTableNum() {
    return tableNum;
  }

  public String getStoreId() {
    return storeId;
  }

  public String getTicket() {
    return ticket;
  }

  public String getUrl() {
    return url;
  }

  public String getRedirectUrl() {
    return redirectUrl;
  }
}
