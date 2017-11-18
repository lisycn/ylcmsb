package com.yuanlai.wpnos.ylcmsb.service.impl;

import com.yuanlai.wpnos.ylcmsb.constants.TpConstant;
import com.yuanlai.wpnos.ylcmsb.dao.SeqNoDao;
import com.yuanlai.wpnos.ylcmsb.model.SeqNo;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 自增序列号服务
 * 
 * @author Lee
 *
 */
@SuppressWarnings("Duplicates")
@Service
public class SeqNoServiceImpl implements MySeqNoService {
	
	@Resource
	private SeqNoDao seqNoMapper;
	
	/** 区间最大值 */  
    private int intervalMax = 0;
      
    /** 每次增加量 */  
    private int interval = 50;

    /** 预定的最大值 */
    private int maxNum = 999999999;
      
    /** 序列号 */  
    private int serialNum = -1;

    /*8 机构treecode用 */
    private String serialNum2 = "-1";
    private String intervalMax2 = "0";
    private String serialNum3 = "-1";
    private String intervalMax3 = "0";
    private String serialNum4 = "-1";
    private String intervalMax4 = "0";
    private String serialNum5 = "-1";
    private String intervalMax5 = "0";

    /** 门店treecode生成用 */
    private String serialStoreNum2 = "-1";
    private String intervalStoreMax2 = "0";
    private String serialStoreNum3 = "-1";
    private String intervalStoreMax3 = "0";
    private String serialStoreNum4 = "-1";
    private String intervalStoreMax4 = "0";
    private String serialStoreNum5 = "-1";
    private String intervalStoreMax5 = "0";

    
    /** 
     * 初始化序列号
     * @return 初始序列号 
     * @throws Exception 
     */  
    private int initStartNum(String key) {
    	SeqNo seqNo = seqNoMapper.selectByPrimaryKey(key);
    	if (null == seqNo) {
    		return 1;
    	} else {
    		return Integer.parseInt(seqNo.getKeyval());
    	}
    }

    private String initStartTreeCode(String key) throws Exception {
        SeqNo seqNo = seqNoMapper.selectByPrimaryKey(key);
        if (null == seqNo) {
            throw new Exception("未查询到treecode");
        } else {
            return seqNo.getKeyval();
        }
    }

      
    /** 
     * 更新区间最大值到缓存系统
     * @param intervalMax 区间最大值 
     * @throws Exception 
     */  
    private void updateStartNum(String key, int value, int length) {
    	SeqNo seqNo = new SeqNo();
    	seqNo.setKeynam(key);
    	seqNo.setKeyval(StringUtils.leftPad(String.valueOf(value), length, "0"));
    	seqNoMapper.updateByPrimaryKey(seqNo);
    }

    /**
     * 更新区间最大值到缓存系统-treecode改
     * 固定更新传入值
     * @param intervalMax 区间最大值
     * @throws Exception
     */
    private void updateStartTreeCode(String key, String value) {
        SeqNo seqNo = new SeqNo();
        seqNo.setKeynam(key);
        seqNo.setKeyval(value);
        seqNoMapper.updateByPrimaryKey(seqNo);
    }
      
    /** 
     * 重置序列号，从1开始 
     */  
    private void resetSerialNum(String key, int length) {  
        this.serialNum = 1;  
        updateStartNum(key, serialNum, length);
    }

    @Override
	public synchronized String getSeqNo(String key, int length) {
		if (serialNum == -1) {  
            serialNum = initStartNum(key);
            intervalMax = serialNum + interval;  
            updateStartNum(key, intervalMax, length);  
            return StringUtils.leftPad(String.valueOf(serialNum), length, "0");  
        }  
        if (serialNum >= maxNum) {
            resetSerialNum(key, length);  
            return StringUtils.leftPad(String.valueOf(serialNum), length, "0"); 
        }  
        serialNum++;  
        if (serialNum >= (intervalMax - 1)) {
            intervalMax += interval;  
            updateStartNum(key, intervalMax, length);  
        }  
        return StringUtils.leftPad(String.valueOf(serialNum), length, "0");
	}

    @Override
    public String getTreeCode(String parentCode, String level, String type) throws Exception {
        int treeCodeInterval = 5;
        // 如果是生成机构treecode的话
        if (TpConstant.ORG_TREE_CODE_TYPE.equals(type)) {
            return generateOrgTreeCode(level, parentCode, treeCodeInterval);
        } else {
            // 生成门店treecode
            return generateStoreTreeCode(level, parentCode, treeCodeInterval);
        }

    }


    private String generateStoreTreeCode(String level, String parentCode, int treeCodeInterval) throws Exception {
        // 根据level级别确定要生成的code
        String keyVal;
        String tempAdd;
        switch (String.valueOf(level)) {
            case "2":
                keyVal = TpConstant.STORE_TREE_CODE_LEVEL2;
                if ("-1".equals(serialStoreNum2)) {
                    serialStoreNum2 = initStartTreeCode(keyVal);
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum2.substring(serialStoreNum2.length() - 5, serialStoreNum2.length())) + treeCodeInterval));
                    intervalStoreMax2 = serialStoreNum2.substring(0, serialStoreNum2.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax2);
                    return parentCode + serialStoreNum2;
                }
                tempAdd = String.valueOf((Integer.valueOf("1" + serialStoreNum2.substring(serialStoreNum2.length() - 5, serialStoreNum2.length())) + 1));
                serialStoreNum2 = serialStoreNum2.substring(0, serialStoreNum2.length() - 5) +  tempAdd.substring(1, tempAdd.length());
                if ((Integer.valueOf(serialStoreNum2.substring(serialStoreNum2.length() - 5, serialStoreNum2.length()))) >= ((Integer.valueOf(intervalStoreMax2.substring(intervalStoreMax2.length() - 5, intervalStoreMax2.length()))) - 1)) {
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum2.substring(serialStoreNum2.length() - 5, serialStoreNum2.length())) + treeCodeInterval));
                    intervalStoreMax2 = serialStoreNum2.substring(0, serialStoreNum2.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax2);
                }
                return parentCode + serialStoreNum2;
            case "3":
                keyVal = TpConstant.STORE_TREE_CODE_LEVEL3;
                if ("-1".equals(serialStoreNum3)) {
                    serialStoreNum3 = initStartTreeCode(keyVal);
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum3.substring(serialStoreNum3.length() - 5, serialStoreNum3.length())) + treeCodeInterval));
                    intervalStoreMax3 = serialStoreNum3.substring(0, serialStoreNum3.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax3);
                    return parentCode + serialStoreNum3;
                }
                tempAdd = String.valueOf((Integer.valueOf("1" + serialStoreNum3.substring(serialStoreNum3.length() - 5, serialStoreNum3.length())) + 1));
                serialStoreNum3 = serialStoreNum3.substring(0, serialStoreNum3.length() - 5) + tempAdd.substring(1, tempAdd.length());
                if ((Integer.valueOf(serialStoreNum3.substring(serialStoreNum3.length() - 5, serialStoreNum3.length()))) >= ((Integer.valueOf(intervalStoreMax3.substring(intervalStoreMax3.length() - 5, intervalStoreMax3.length()))) - 1)) {
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum3.substring(serialStoreNum3.length() - 5, serialStoreNum3.length())) + treeCodeInterval));
                    intervalStoreMax3 = serialStoreNum3.substring(0, serialStoreNum3.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax3);
                }
                return parentCode + serialStoreNum3;
            case "4":
                keyVal = TpConstant.STORE_TREE_CODE_LEVEL4;
                if ("-1".equals(serialStoreNum4)) {
                    serialStoreNum4 = initStartTreeCode(keyVal);
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum4.substring(serialStoreNum4.length() - 5, serialStoreNum4.length())) + treeCodeInterval));
                    intervalStoreMax4 = serialStoreNum4.substring(0, serialStoreNum4.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax4);
                    return parentCode + serialStoreNum4;
                }
                tempAdd = serialStoreNum4.substring(0, serialStoreNum4.length() - 5) +  String.valueOf((Integer.valueOf("1" + serialStoreNum4.substring(serialStoreNum4.length() - 5, serialStoreNum4.length())) + 1));
                serialStoreNum4 = tempAdd.substring(1, tempAdd.length());
                if ((Integer.valueOf(serialStoreNum4.substring(serialStoreNum4.length() - 5, serialStoreNum4.length()))) >= ((Integer.valueOf(intervalStoreMax4.substring(intervalStoreMax4.length() - 5, intervalStoreMax4.length()))) - 1)) {
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum4.substring(serialStoreNum4.length() - 5, serialStoreNum4.length())) + treeCodeInterval));
                    intervalStoreMax4 = serialStoreNum4.substring(0, serialStoreNum4.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax4);
                }
                return parentCode + serialStoreNum4;
            case "5":
                keyVal = TpConstant.STORE_TREE_CODE_LEVEL5;
                if ("-1".equals(serialStoreNum5)) {
                    serialStoreNum5 = initStartTreeCode(keyVal);
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum5.substring(serialStoreNum5.length() - 5, serialStoreNum5.length())) + treeCodeInterval));
                    intervalStoreMax5 = serialStoreNum5.substring(0, serialStoreNum5.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax5);
                    return parentCode + serialStoreNum5;
                }
                tempAdd = String.valueOf((Integer.valueOf("1" + serialStoreNum5.substring(serialStoreNum5.length() - 5, serialStoreNum5.length())) + 1));
                serialStoreNum5 = serialStoreNum5.substring(0, serialStoreNum5.length() - 5) +  tempAdd.substring(1, tempAdd.length());
                if ((Integer.valueOf(serialStoreNum5.substring(serialStoreNum5.length() - 5, serialStoreNum5.length()))) >= ((Integer.valueOf(intervalStoreMax5.substring(intervalStoreMax5.length() - 5, intervalStoreMax5.length()))) - 1)) {
                    String tempInterval = String.valueOf((Integer.valueOf("1" + serialStoreNum5.substring(serialStoreNum5.length() - 5, serialStoreNum5.length())) + treeCodeInterval));
                    intervalStoreMax5 = serialStoreNum5.substring(0, serialStoreNum5.length() - 5) + tempInterval.substring(1, tempInterval.length());
                    updateStartTreeCode(keyVal, intervalStoreMax5);
                }
                return parentCode + serialStoreNum5;
            default:
                return null;

        }
    }

    /**
     * 生成机构treecode
     * @param level
     * @param parentCode
     * @param treeCodeInterval
     * @return
     * @throws Exception
     */
    private String generateOrgTreeCode(String level, String parentCode, int treeCodeInterval) throws Exception {
        // 根据level级别确定要生成的code
        String keyVal;
        switch (String.valueOf(level)) {
            case "2":
                keyVal = TpConstant.TREE_CODE_LEVEL2;
                if ("-1".equals(serialNum2)) {
                    serialNum2 = initStartTreeCode(keyVal);
                    intervalMax2 = String.valueOf((Integer.valueOf(serialNum2.substring(serialNum2.length() - 5, serialNum2.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax2);
                    return parentCode + serialNum2;
                }
                serialNum2 = String.valueOf((Integer.valueOf(serialNum2.substring(serialNum2.length() - 5, serialNum2.length())) + 1));
                if ((Integer.valueOf(serialNum2.substring(serialNum2.length() - 5, serialNum2.length()))) >= ((Integer.valueOf(intervalMax2.substring(intervalMax2.length() - 5, intervalMax2.length()))) - 1)) {
                    intervalMax2 = String.valueOf((Integer.valueOf(serialNum2.substring(serialNum2.length() - 5, serialNum2.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax2);
                }
                return parentCode + serialNum2;
            case "3":
                keyVal = TpConstant.TREE_CODE_LEVEL3;
                if ("-1".equals(serialNum3)) {
                    serialNum3 = initStartTreeCode(keyVal);
                    intervalMax3 = String.valueOf((Integer.valueOf(serialNum3.substring(serialNum3.length() - 5, serialNum3.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax3);
                    return parentCode + serialNum3;
                }
                serialNum3 = String.valueOf((Integer.valueOf(serialNum3.substring(serialNum3.length() - 5, serialNum3.length())) + 1));
                if ((Integer.valueOf(serialNum3.substring(serialNum3.length() - 5, serialNum3.length()))) >= ((Integer.valueOf(intervalMax3.substring(intervalMax3.length() - 5, intervalMax3.length()))) - 1)) {
                    intervalMax3 = String.valueOf((Integer.valueOf(serialNum3.substring(serialNum3.length() - 5, serialNum3.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax3);
                }
                return parentCode + serialNum3;
            case "4":
                keyVal = TpConstant.TREE_CODE_LEVEL4;
                if ("-1".equals(serialNum4)) {
                    serialNum4 = initStartTreeCode(keyVal);
                    intervalMax4 = String.valueOf((Integer.valueOf(serialNum4.substring(serialNum4.length() - 5, serialNum4.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax4);
                    return parentCode + serialNum4;
                }
                serialNum4 = String.valueOf((Integer.valueOf(serialNum4.substring(serialNum4.length() - 5, serialNum4.length())) + 1));
                if ((Integer.valueOf(serialNum4.substring(serialNum4.length() - 5, serialNum4.length()))) >= ((Integer.valueOf(intervalMax4.substring(intervalMax4.length() - 5, intervalMax4.length()))) - 1)) {
                    intervalMax4 = String.valueOf((Integer.valueOf(serialNum4.substring(serialNum4.length() - 5, serialNum4.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax4);
                }
                return parentCode + serialNum4;
            case "5":
                keyVal = TpConstant.TREE_CODE_LEVEL5;
                if ("-1".equals(serialNum5)) {
                    serialNum5 = initStartTreeCode(keyVal);
                    intervalMax5 = String.valueOf((Integer.valueOf(serialNum5.substring(serialNum5.length() - 5, serialNum5.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax5);
                    return parentCode + serialNum5;
                }
                serialNum5 = String.valueOf((Integer.valueOf(serialNum5.substring(serialNum5.length() - 5, serialNum5.length())) + 1));
                if ((Integer.valueOf(serialNum5.substring(serialNum5.length() - 5, serialNum5.length()))) >= ((Integer.valueOf(intervalMax5.substring(intervalMax5.length() - 5, intervalMax5.length()))) - 1)) {
                    intervalMax5 = String.valueOf((Integer.valueOf(serialNum5.substring(serialNum5.length() - 5, serialNum5.length())) + treeCodeInterval));
                    updateStartTreeCode(keyVal, intervalMax5);
                }
                return parentCode + serialNum5;
            default:
                return null;

        }
    }



}
