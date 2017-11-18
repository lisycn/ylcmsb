package com.yuanlai.wpnos.ylcmsb.service;

/**
 * 自增序列号服务
 * 
 * @author Lee
 *
 */
public interface MySeqNoService {
	
	String getSeqNo(String key, int length);

    /**
     * 获取treecode
     * @param key
     * @return
     */
    String getTreeCode(String parentCode, String level, String type) throws Exception;

}
