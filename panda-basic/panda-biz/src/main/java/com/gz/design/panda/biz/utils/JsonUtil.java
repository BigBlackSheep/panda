/***********************************************************************   
 *
 *   JsonUtil 通用操作类  
 *
 ***********************************************************************/
package com.gz.design.panda.biz.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author zyb since 2011-4-11
 */
public class JsonUtil {

	/**
	 * <b>概要：</b> ·根据java POJO对象获得相应的json字符串 <b>作者：</b>suxh </br> <b>日期：</b>2015-1-28 </br>
	 * @param javaObj 需要转换层json字符串的对象
	 * @param dataFormat 日期转换格式
	 * @return 对应的json字符串
	 */
/*	public static String getJsonString4JavaPOJO(Object javaObj, String dataFormat) {
		JSONObject json;
		JsonConfig jsonConfig = configJson(dataFormat);
		json = JSONObject.fromObject(javaObj, jsonConfig);
		return json.toString();
	}

	*//**
	 * <b>概要：</b> 获得json配置（配置一系列转换的工具类） Utility class that helps configuring the serialization process. <b>作者：</b>suxh </br> <b>日期：</b>2015-1-28 </br>
	 *     日期格式，用于日期转换为json格式数据
	 * @return jsonConfi对象
	 *//*
	public static JsonConfig configJson(String datePattern) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "" });
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor(datePattern));
		return jsonConfig;
	}*/

	public static String stringToJson(String s) {
		if (s == null) {
			return nullToJson();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if (ch >= '\u0000' && ch <= '\u001F') {
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for (int k = 0; k < 4 - ss.length(); k++) {
							sb.append('0');
						}
						sb.append(ss.toUpperCase());
					} else {
						sb.append(ch);
					}
			}
		}
		return sb.toString();
	}

	/**
	 * <b>概要：</b> 对象为空，则返回空字符串 <b>作者：</b>suxh </br> <b>日期：</b>2015-1-28 </br>
	 * @return 空字符串
	 */
	public static String nullToJson() {
		return "";
	}

	/**
	 * <b>概要：</b> 对象转换成json字符串 <b>作者：</b>suxh </br> <b>日期：</b>2015-1-28 </br>
	 * @param obj 对象（Number，Boolean）等等
	 * @return json字符串
	 */
	public static String objectToJson(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof Number) {
			json.append(numberToJson((Number) obj));
		} else if (obj instanceof Boolean) {
			json.append(booleanToJson((Boolean) obj));
		} else if (obj instanceof String) {
			json.append("\"").append(stringToJson(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(arrayToJson((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(listToJson((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(mapToJson((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(setToJson((Set<?>) obj));
		} else {
			json.append(beanToJson(obj));
		}
		return json.toString();
	}

	public static String numberToJson(Number number) {
		return number.toString();
	}

	public static String booleanToJson(Boolean bool) {
		return bool.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getList4Json(String jsonString, Class pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;

		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}

	/**
	 * @param bean bean对象
	 * @return String
	 */
	public static String beanToJson(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = objectToJson(props[i].getName());
					String value = objectToJson(props[i].getReadMethod().invoke(bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param list list对象
	 * @return String
	 */
	public static String listToJson(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param array 对象数组
	 * @return String
	 */
	public static String arrayToJson(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param map map对象
	 * @return String
	 */
	public static String mapToJson(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(objectToJson(key));
				json.append(":");
				json.append(objectToJson(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * json字符串中含有json数组时递归转换每个json串
	 *
	 * @param map
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static void jsonToMap(Map<String, Object> map, String json, String jsonKey) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = jsonObject.getString(key);
//			System.out.println(value);
			if (jsonKey != null && !"".equals(jsonKey) && jsonKey.equals(key)) {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				if (value != null && !"".equals(value) && !"null".equals(value)) {
					JSONArray jsonArray = JSONArray.fromObject(value);
					for (int i = 0; i < jsonArray.size(); i++) {
						Map<String, Object> recordMap = new HashMap<String, Object>();
						jsonToMap(recordMap, jsonArray.getString(i), "");
						list.add(recordMap);
					}
				}
				map.put(jsonKey, list);
			} else {
				map.put(key, value);
			}
		}
		return;
	}

	/**
	 * @param set 集合对象
	 * @return String
	 */
	public static String setToJson(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(objectToJson(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

}
