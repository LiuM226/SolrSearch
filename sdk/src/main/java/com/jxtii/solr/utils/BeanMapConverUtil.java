package com.jxtii.solr.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Map转Bean
 * @date 17/7/8.
 * @author guolf
 */
public class BeanMapConverUtil {

    private static Logger logger = LoggerFactory.getLogger("BeanMapConverUtil");

    /**
     * Map转实体类
     * @param paramMap
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> paramMap, Class<T> clazz) {
        T beanObj = null;
        try {
            beanObj = clazz.newInstance();
            String propertyName = null;
            Object propertyValue = null;
            for (Map.Entry<String, Object> entity : paramMap.entrySet()) {
                propertyName = entity.getKey();
                propertyValue = entity.getValue();
                setProperties(beanObj, propertyName, propertyValue);
            }
        } catch (IllegalArgumentException e) {
            logger.error("不合法或不正确的参数", e);
        } catch (IllegalAccessException e) {
            logger.error("实例化JavaBean失败", e);
        } catch (Exception e) {
            logger.error("Map转换为Java Bean对象失败", e);
        }
        return beanObj;
    }

    private static <T> void setProperties(T entity, String propertyName,
                                         Object value) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        PropertyDescriptor pd = new PropertyDescriptor(propertyName, entity.getClass());
        Method methodSet = pd.getWriteMethod();
        methodSet.invoke(entity, value);
    }

}
