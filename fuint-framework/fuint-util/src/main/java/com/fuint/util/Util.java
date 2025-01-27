package com.fuint.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类。
 *
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public class Util {
	/**
	 * 默认的私有的构造函数，不能生成新的实例.
	 * **/
	private Util() {

	}

	
	public static String getURL(String url, String method) {
		return url+"/"+method;
	}
	/**
	 * 判断字符串是否匹配正则表达式.
	 *
	 *@param aimStr
	 *            目标字符串
	 *@param regex
	 *            正则表达式
	 *@return 匹配结果(true:匹配, false:不匹配)
	 * **/
	public static boolean getMatchResult(final String aimStr, final String regex) {
  		 Pattern pattern = Pattern.compile(regex);
  		 Matcher matcher = pattern.matcher(aimStr);
  		 return matcher.matches();
	}
	
	public static String getIdSQLParam(String[] ids) {
		if(ids==null||ids.length==0){
			return null;
		}
		StringBuffer param = new StringBuffer("");
		for(int i=0;i<ids.length;i++){
			param.append(ids[i]);
			if(i<ids.length-1){
				param.append(",");
			}
		}
		return param.toString();
	}
}
