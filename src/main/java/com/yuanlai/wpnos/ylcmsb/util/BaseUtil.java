package com.yuanlai.wpnos.ylcmsb.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by youdd on 2017/7/18.
 */
public class BaseUtil {

    /**元转分*/
    public static String element2Branch(String amount){
        //初始值
        String branch = "0";
        //处理包含, ￥ 或者$的金额
        branch =  amount.replaceAll("\\$|\\￥|\\,", "");
        int index = branch.indexOf(".");
        int length = branch.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(branch+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((branch.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((branch.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((branch.substring(0, index+1)).replace(".", "")+"00");
        }
        branch =  amLong.toString();
        return branch;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map <一句话功能简述> <功能详细描述>
     *
     * @param bean
     * @return
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @see [类、类#方法、类#成员]
     */
    public static Map<String, Object> convertBean2Map(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class<? extends Object> type = bean.getClass();
        Map<String, Object> returnMap = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!"class".equals(propertyName)) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, null);
                }
            }
        }
        return returnMap;
    }
}
