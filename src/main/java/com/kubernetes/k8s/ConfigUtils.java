package com.kubernetes.k8s;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtils {

	private static final Logger logger = Logger.getLogger(ConfigUtils.class);

	// 读配置文件
	// fileName为配置文件的名字
	// property为要读取属性的名字
	private static String getpath(String fileName, String property) {
		StringBuilder spb = new StringBuilder();
		Properties prop = new Properties();
		try {
			// 读取属性文件a.properties
			String filePath = ConfigUtils.class.getResource("/").getFile() + fileName;
			filePath = URLDecoder.decode(filePath, "utf-8");
			//logger.info("filePath==>" + filePath);
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			prop.load(in); /// 加载属性列表
			String value = prop.getProperty(property);
			//logger.info("propnalue==>" + value);
			spb.append(value);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return spb.toString();
	}
	
	public static String getBootstrapTokenSecret(){
		return getpath("config.properties", "Bootstrap_Token_Secret");
	}
}
