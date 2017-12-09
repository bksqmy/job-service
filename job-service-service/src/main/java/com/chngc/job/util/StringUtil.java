package com.chngc.job.util;

/**
 * 字符串操作的公共类
 *
 * @FileName: StringUtil
 * @Version: 1.0
 * @Author: yanpeng
 * @Date: 2015/8/31 14:49
 */
public class StringUtil {

    private static final String regValidatorIp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";

    /**
     * 判断是否为空或者空字符串
     *
     * @param obj
     * @return 空返回true
     */
    public static boolean isEmptyOrNull(Object obj) {
        if (obj != null && !"".equals(obj.toString().trim())) {
            return false;
        }
        return true;
    }

    /**
     * 安全的转换为字符串
     *
     * @param obj
     * @return
     */
    public static String valueOf(Object obj) {
        if (isEmptyOrNull(obj)) {
            return "";
        }
        return obj.toString();
    }

    /**
     * 比较两个字符型对象是否相等
     *
     * @param o1 传入参数1
     * @param o2 传入参数2
     * @return
     */
    public static boolean equal(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if (o1 == null && o2 != null) {
            return false;
        } else if (o1 != null && o2 == null) {
            return false;
        } else {
            return String.valueOf(o1).equals(String.valueOf(o2));
        }
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     *
     * @param value     要检查的字符串
     * @param trim      是否去掉头尾的特定字符
     * @param trimChars 要去掉的特定字符
     * @return
     */
    public static boolean isEmpty(String value, boolean trim, char... trimChars) {
        if (trim)
            return value == null || trim(value, trimChars).length() <= 0;
        return value == null || value.length() <= 0;
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     *
     * @param value 要检查的字符串
     * @param trim  是否去掉头尾的空格
     * @return
     */
    public static boolean isEmpty(String value, boolean trim) {
        return isEmpty(value, trim, ' ');
    }

    /**
     * 检查字符串是否为空
     * <p>为null或者长度为0视为空字符串
     *
     * @param value 要检查的字符串
     * @return
     */
    public static boolean isEmpty(String value) {
        return isEmpty(value, false);
    }

    /**
     * 如果为null，转换为""
     *
     * @param value
     * @return
     */
    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }

    /**
     * 确保存入数据库的string值不会引起数据库报错。
     * <p/>
     * 1. 数据库不允许为null，value为nul时返回""；<br />
     * 2. 超过最大长度时截断字符串。
     *
     * @param value     要存入数据库的字符串值。
     * @param nullable  是否允许为null。
     * @param maxLength 最大长度。
     * @return
     */
    public static String dbSafeString(String value, boolean nullable, int maxLength) {
        if (value == null) {
            if (nullable)
                return null;
            return nullSafeString(value);
        }
        if (value.length() > maxLength)
            return value.substring(0, maxLength);
        return value;
    }

    /**
     * 去掉头尾空格字符
     *
     * @param value 待处理的字符串
     * @return
     */
    public static String trim(String value) {
        return trim(3, value, ' ');
    }

    /**
     * 去除字符串头尾的特定字符
     *
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trim(String value, char... chars) {
        return trim(3, value, chars);
    }

    /**
     * 去除字符串头部的特定字符
     *
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimStart(String value, char... chars) {
        return trim(1, value, chars);
    }

    /**
     * 去除字符串尾部的特定字符
     *
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    public static String trimEnd(String value, char... chars) {
        return trim(2, value, chars);
    }

    /**
     * 去掉字符串头尾特定字符
     *
     * @param mode  <li>1: 去掉头部特定字符；
     *              <li>2: 去掉尾部特定字符；
     *              <li>3: 去掉头尾特定字符；
     * @param value 待处理的字符串
     * @param chars 需要去掉的特定字符
     * @return
     */
    private static String trim(int mode, String value, char... chars) {
        if (value == null || value.length() <= 0)
            return value;

        int startIndex = 0, endIndex = value.length(), index = 0;
        if (mode == 1 || mode == 3) {
            // trim头部
            while (index < endIndex) {
                if (contains(chars, value.charAt(index++))) {
                    startIndex++;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";

        if (mode == 2 || mode == 3) {
            // trim尾部
            index = endIndex - 1;
            while (index >= 0) {
                if (contains(chars, value.charAt(index--))) {
                    endIndex--;
                    continue;
                }
                break;
            }
        }

        if (startIndex >= endIndex)
            return "";
        if (startIndex == 0 && endIndex == value.length() - 1)
            return value;
        return value.substring(startIndex, endIndex);
    }

    private static boolean contains(char[] chars, char chr) {
        if (chars == null || chars.length <= 0)
            return false;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == chr)
                return true;
        }
        return false;
    }

    /**
     * 判断是否是有效的IP地址。
     *
     * @param value
     * @return
     */
    public static boolean isIp(String value) {
        if (isEmpty(value))
            return false;
        return value.matches(regValidatorIp);
    }
}
