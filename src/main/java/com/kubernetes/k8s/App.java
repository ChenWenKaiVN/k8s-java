package com.kubernetes.k8s;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
    	String url = "https://10.3.10.144:6443/api/v1/services";
    	Map<String, String> param = new HashMap<>();
    	Map<String, String> headers = new HashMap<>();
    	headers.put("Authorization", ConfigUtils.getBootstrapTokenSecret());
        String result = HttpUtils.get(url, param, headers);
    	System.out.println(result);
    }
}
