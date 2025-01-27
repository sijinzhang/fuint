package com.fuint.base.util;

import com.fuint.util.DateUtil;
import com.fuint.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * 查询条件filter
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public class SearchFilter {

    private static final Logger logger = LoggerFactory.getLogger(SearchFilter.class);

    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE, NQ, IN
    }

    public String fieldName;
    public Object value;
    public Operator operator;
    public Collection array;


    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public SearchFilter(String fieldName, Operator operator, Collection array) {
        this.fieldName = fieldName;
        this.array = array;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtil.isBlank((String) value)) {
                continue;
            }
            // 拆分operator与filedAttribute
            String[] names = StringUtil.split(key, "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);
            // 创建searchFilter
            SearchFilter filter = new SearchFilter(filedName, operator, value);
            filters.put(key, filter);
        }
        return filters;
    }


    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, SearchFilter> parse(Map<String, Object> searchParams, Class clss) {
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();
        Field[] fields = clss.getDeclaredFields();
        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtil.isBlank((String) value)) {
                continue;
            }
            // 拆分operator与filedAttribute
            String[] names = StringUtil.split(key, "_");
            if (names.length != 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }
            String filedName = names[1];
            Operator operator = Operator.valueOf(names[0]);
            if (operator == Operator.IN) {//IN的查询方式,需要根据当前对象属性的类型 设置list反省类型数组值
                String[] array = ((String) value).split(",");
                if (array.length > 0) {
                    Collection list = null;
                    if (fields != null && fields.length > 0) {
                        for (Field field : fields) {
                            if (StringUtil.equals(field.getName(), filedName)) {
                                if (field.getType() == Integer.class || field.getType() == int.class) {
                                    list = new ArrayList<Integer>();
                                    for (String str : array) {
                                        if (StringUtil.isNotBlank(str)) {
                                            list.add(Integer.parseInt(str));
                                        }
                                    }
                                } else if (field.getType() == Long.class || field.getType() == long.class) {
                                    list = new ArrayList<Long>();
                                    for (String str : array) {
                                        if (StringUtil.isNotBlank(str)) {
                                            list.add(Long.parseLong(str));
                                        }
                                    }
                                } else {
                                    list = new ArrayList<String>();
                                    for (String str : array) {
                                        if (StringUtil.isNotBlank(str)) {
                                            list.add(str);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // 创建searchFilter
                    SearchFilter filter = new SearchFilter(filedName, operator, list);
                    filters.put(key, filter);
                }
            } else {
                SearchFilter filter = null;
                if (filedName.indexOf(".") >= 0) {//输入是级联条件,比如:EQ_OBJECT.ID,直接创建filter
                    filter = new SearchFilter(filedName, operator, value);
                } else {//否则根据属性的类型,设置filter值
                    // 创建searchFilter
                    if (fields != null && fields.length > 0) {
                        if (value != null && StringUtil.isNotBlank(value.toString())) {
                            for (Field field : fields) {
                                try {
                                    if (StringUtil.equals(field.getName(), filedName)) {
                                        if (field.getType() == Integer.class || field.getType() == int.class) {
                                            filter = new SearchFilter(filedName, operator, Integer.parseInt(value.toString()));
                                        } else if (field.getType() == Long.class || field.getType() == long.class) {
                                            filter = new SearchFilter(filedName, operator, Long.parseLong(value.toString()));
                                        } else if (field.getType() == Double.class || field.getType() == double.class) {
                                            filter = new SearchFilter(filedName, operator, Double.parseDouble(value.toString()));
                                        } else if (field.getType() == BigDecimal.class) {
                                            filter = new SearchFilter(filedName, operator, new BigDecimal(value.toString()));
                                        } else if (field.getType() == Date.class) {
                                            filter = new SearchFilter(filedName, operator, DateUtil.parseDateNewFormat(value.toString()));
                                        } else {
                                            filter = new SearchFilter(filedName, operator, value);
                                        }
                                    }
                                } catch (Exception e) {
                                    logger.error("查询数据类型转换异常.{0}", e);
                                    break;
                                }
                            }
                        }
                    }
                }
                if (filter != null) {
                    filters.put(key, filter);
                }
            }

        }
        return filters;
    }
}