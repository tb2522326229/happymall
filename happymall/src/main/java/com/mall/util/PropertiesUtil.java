package com.mall.util;

import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause.MainModelClause;


/**
 * 获取图片服务器的各种属性
 */
public class PropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static Properties p;

    static{
    	try {
    		p = new Properties();
			p.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("mmall.properties"),"utf-8"));
		} catch (Exception e) {
			logger.error("配置文件读取异常",e);
		}
    }
    
    /**
     * 根据传入的属性名来返回对应的属性值
     *     如果不存在该属性，则返回空
     * @param key 属性名
     * @return 属性名来返回对应的属性值
     */
    public static String getProperty(String key){
    	String value = p.getProperty(key.trim());
    	if(StringUtils.isBlank(key.trim())){
    		return null;
    	}
    	return value.trim();
    }
    
    /**
     * 根据传入的属性名来返回对应的属性值
     * 如果属性值为空，则以自己传入的字段为属性值
     * @param key 属性名
     * @param defaultValue 自己传入的字段
     * @return 属性名来返回对应的属性值
     */
    public static String getProperty(String key,String defaultValue){
    	String value = p.getProperty(key.trim());
    	if(StringUtils.isBlank(key.trim())){
    		value = defaultValue;
    	}
    	return value.trim();
    }
    public static void main(String[] args) {
		String property = PropertiesUtil.getProperty("ftp.server.ip");
		System.out.println(property);
	}
}
