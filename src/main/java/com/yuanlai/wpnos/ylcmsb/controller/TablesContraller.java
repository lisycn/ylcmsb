package com.yuanlai.wpnos.ylcmsb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tangdi.def.base.controller.TdBaseController;
import com.yuanlai.wpnos.ylcmsb.common.TdCommonUtil;
import com.yuanlai.wpnos.ylcmsb.constants.SourceConstants;
import com.yuanlai.wpnos.ylcmsb.entity.CateringTable;
import com.yuanlai.wpnos.ylcmsb.service.TablesService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

@SuppressWarnings("Duplicates")
@Controller
@RequestMapping("/tables")
public class TablesContraller extends TdBaseController{



    //注入服务层
    @Resource
    private TablesService tablesService;

    @RequestMapping("/listTables")
    @ResponseBody
    public Map<String, Object> listTables(CateringTable cateringTable, int pageSize, int pageNum) throws Exception{
            Map<String, Object> map = tablesService.listTables(cateringTable, pageSize, pageNum);
            return genSuccessResult(map);
    }

    @RequestMapping("/listAccount")
    @ResponseBody
    public Map<String, Object> listAccount(HttpServletRequest request) throws Exception{
        Map map = TdCommonUtil.getParameterMap(request);
        Map<String, Object> result = tablesService.listAccount(map);
        return genSuccessResult();
    };

    @RequestMapping("/addTable")
    @ResponseBody
    public Map<String,Object> addTables(HttpServletRequest request) {
        Map<String, Object> resultMap = null;
        try {
            Map paramMap = TdCommonUtil.getParameterMap(request);
            log.info("TablesContraller 桌位添加参数：" + paramMap);
            resultMap = tablesService.addTables(paramMap);
            log.info("TablesContraller 桌位添加返回信息：" + resultMap);
            if("1".equals(resultMap.get("result"))){
                return genSuccessResult(null != resultMap.get("msg")?resultMap.get("msg"):"菜品类别添加成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("菜品类别添加失败",e.getMessage());
        }
        return genSuccessResult(null != resultMap.get("msg")?resultMap.get("msg"):"菜品类别添加失败！");
    }

    @RequestMapping("/updateTable")
    @ResponseBody()
    public Map<String, Object> updateTables(CateringTable cateringTable){
        log.info("要修改的桌位信息为："+cateringTable);
        Map<String, Object> resultMap = null;
        try {
             resultMap = tablesService.updateTable(cateringTable);
             if("1".equals(resultMap.get("result"))){
                 return genSuccessResult(null !=resultMap.get("msg")?resultMap.get("msg"):"桌位信息修改成功");
              }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("菜品类别修改失败",e.getMessage());
        }
        return genSuccessResult(null !=resultMap.get("msg")?resultMap.get("msg"):"桌位信息修改失败");
    }

    @RequestMapping("/deleteTables")
    @ResponseBody
    public Map<String, Object> deleteTables(@RequestParam(value = "tablesId", required = false) String[] tablesId) throws  Exception{

        log.info("传入的参数为：" + tablesId);
        try {
            for (String str : tablesId) {
                tablesService.deleteTable(str);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("删除失败：" + e.getMessage());
            return genErrorResult("删除失败" + e.getMessage());
        }
        log.info("删除成功");
        return genSuccessResult("删除成功");
    }

    /**
     * 菜品下拉列表
     *
     */
    @RequestMapping(value = "/dictStores")
    @ResponseBody
    public Map<String, Object> dictDishes(CateringTable cateringTable) throws Exception {
        Map<String, Object> map = tablesService.dictStores(cateringTable);
        return genSuccessResult(map);
    }

    @RequestMapping("/getQRCode")
    @ResponseBody
    public Map<String, Object> getQRCode(HttpServletRequest request) throws Exception{
        Map paramMap = TdCommonUtil.getParameterMap(request);
        Map<String, Object> resultMap = null;

        //拿到生成access_token 的常量
        String weixinAppid = SourceConstants.WEIXIN_APPID;
        String weixinAppsecret = SourceConstants.WEIXIN_APPSECRET;

        //调用微信接口，生成access_token
        String urlGet ="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+weixinAppid+"&secret="+weixinAppsecret;
        JSONObject joAccessToken = httpGet(urlGet);
        String access_token = joAccessToken.getString("access_token");


        //用于拼接生成ticket和url使用的body
        String name = paramMap.get("tableName").toString();
        String tableNum = paramMap.get("tableNum").toString();
        String storeId = paramMap.get("storeId").toString();

        //调用微信接口，生成ticket和url
        String urlPost="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+access_token;
        String json="{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+storeId+tableNum+"\"}}}";
        JSONObject joBody = JSON.parseObject(json);
        JSONObject joTicAndUrl = httpPost(urlPost, joBody);

        resultMap = JSON.parseObject(joTicAndUrl.toString(), Map.class);

        return genSuccessResult(resultMap);
    }

    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static JSONObject httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult =JSON.parseObject(strResult);
                //jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {

            }
        } catch (IOException e) {
            //log.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }

    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());

                    /**把json字符串转换成json对象**/
                    jsonResult =JSON.parseObject(str);
                   // jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                  //  logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
           // logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }

}
