package com.yuanlai.wpnos.ylcmsb.service.impl;

import com.tangdi.def.utils.exception.TdRuntimeException;
import com.yuanlai.wpnos.ylcmsb.common.AuthBeanUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthCommonUtil;
import com.yuanlai.wpnos.ylcmsb.common.AuthConstants;
import com.yuanlai.wpnos.ylcmsb.common.DateUtils;
import com.yuanlai.wpnos.ylcmsb.constants.SourceConstants;
import com.yuanlai.wpnos.ylcmsb.dao.CateringTablesDao;
import com.yuanlai.wpnos.ylcmsb.entity.CateringTable;
import com.yuanlai.wpnos.ylcmsb.service.MySeqNoService;
import com.yuanlai.wpnos.ylcmsb.service.TablesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("/Duplicates")
@Service
public class TableServiceImpl implements TablesService {

    @Resource
    private CateringTablesDao cateringTablesMapper;

    @Resource
    private MySeqNoService mySeqNoService;

    private static Logger log = LoggerFactory.getLogger(TableServiceImpl.class);

    @Override
    public Map<String, Object> listTables(CateringTable cateringTable, int pageSize, int pageNum) throws Exception {
        Map<String, Object> paraMap = AuthBeanUtil.toMap(cateringTable);
        paraMap = AuthCommonUtil.setPageParam(paraMap, pageSize, pageNum);
        try {
            log.info("paraMap:" + paraMap.toString());
            HashMap data = new HashMap<>();
            List<Map<String, Object>> tablesList = cateringTablesMapper.listTables(paraMap);
            int total = cateringTablesMapper.countList(paraMap);
            data.put(AuthConstants.COMMON_LIST,tablesList);
            data.put(AuthConstants.COMMON_TOTAL,total);
            return data;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new TdRuntimeException("数据库错误");
        }
    }

    public Map<String, Object> listAccount(Map<String, Object> tableInfo) throws Exception{
        try {
            log.info("paraMap:" + tableInfo.toString());
            tableInfo.put("tableNumber", "#"+tableInfo.get("tableNum"));
            List<Map<String, Object>> accountList = cateringTablesMapper.listAccount(tableInfo);
            HashMap<String, Object> data = new HashMap<>();
            data.put(AuthConstants.COMMON_LIST,accountList);
            data.put(AuthConstants.COMMON_TOTAL,1);
            return data;
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new TdRuntimeException("数据库错误");
        }
    }

    @Override
    public Map<String, Object> updateTable(CateringTable cateringTable) throws Exception {
        Map<String, Object> resultMap =new HashMap<>();
        log.info("paraMap :"+cateringTable);
        int i = cateringTablesMapper.updateTable(cateringTable);
        if(i>0){
            resultMap.put("result",0);
            resultMap.put("msg","桌位信息修改成功");
        }else{
            resultMap.put("result", 1);
            resultMap.put("msg", "桌位信息添加失败");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> addTables(Map<String, Object> addTables) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        log.info("paramMap :"+addTables);
        String table_id = mySeqNoService.getSeqNo("TABLE_ID", 11);
        addTables.put("tableId", table_id);
        addTables.put("openStatus","0");
        addTables.put("redirectUrl", SourceConstants.redirectUrl);
        addTables.put("createTime", DateUtils.GETDATETIME());
        int i = cateringTablesMapper.addTables(addTables);

        if(i>0){
            resultMap.put("result",0);
            resultMap.put("msg","桌位信息添加成功");
        }else{
            resultMap.put("result", 1);
            resultMap.put("msg", "桌位信息添加失败");
        }
        return resultMap;
    }

    /**
     * 菜品类型，下拉选择框
     * */
    @Override
    public Map<String, Object> dictStores(CateringTable cateringTable) throws Exception {
        Map<String, Object> paraMap = AuthBeanUtil.toMap(cateringTable);
        try {
            log.info("paraMap:" + paraMap.toString());
            Map<String, Object> data = new HashMap<String,Object>();
            List<Map<String, Object>> storesList = cateringTablesMapper.dictStores(paraMap);
            data.put(AuthConstants.COMMON_LIST, storesList);
            return data;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new TdRuntimeException("数据库错误");
        }
    }

    @Override
    public void deleteTable(String tablesId) throws Exception {
        //将通过指定的分隔符（AuthConstants.COMMON_COMMA）分割的字符串解析为list
        List<String> idsList = AuthCommonUtil.handleIds(tablesId, AuthConstants.COMMON_COMMA);

        try {
            cateringTablesMapper.deleteTables(idsList);
        }catch (Exception e){
            e.printStackTrace();
            throw new TdRuntimeException("数据库错误");
        }

    }
}
