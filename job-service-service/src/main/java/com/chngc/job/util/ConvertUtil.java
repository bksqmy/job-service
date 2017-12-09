package com.chngc.job.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数据类型转换工具，目前没有考虑char（Character）类型。
 * @Filename: DataConvertUtil.java
 * @Description:
 * @Version: 1.0
 */
public final class ConvertUtil {

    /**
     * {@link String}转换为{@link Boolean}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) 字符串的"true"、"false"值（忽略大小写）会转化为boolean类型的true、false值；
     * <br /> 2) 字符串的"1"、"0"值会转化为boolean类型的true、fasle值；
     * <br /> 3) value为null或者空字符串，会返回defaultValue；
     * <br /> 4) 除以上情况外，均返回true值；
     * @param value 要转换成boolean值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Boolean toBool(String value, Boolean defaultValue) {
        if (value == null || value.length() <= 0)
            return defaultValue;
        value = value.trim().toLowerCase();
        if (value.length() <= 0)
            return defaultValue;
        if ("false".equals(value) || "0".equals(value))
            return false;
        if ("true".equals(value) || "1".equals(value))
            return true;
        return true;
    }

    /**
     * {@link Object}转换为{@link Boolean}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时，返回defaultValue；
     * <br /> 2) value为{@link Boolean}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toBool(String, Boolean)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，值为0返回false，否则返回true；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成boolean值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null)
            return defaultValue;
        Class<?> clazz = value.getClass();
        // 最可能出现的类型放在最前面判断
        if (Boolean.class.equals(clazz))
            return (Boolean) value;
        if (String.class.equals(clazz))
            return toBool(String.valueOf(value), defaultValue);
        if (Integer.class.equals(clazz))
            return (Integer) value != 0;
        if (Byte.class.equals(clazz))
            return (Byte) value != 0;
        if (Short.class.equals(clazz))
            return (Short) value != 0;
        if (Long.class.equals(clazz))
            return (Long) value != 0;
        if (Double.class.equals(clazz))
            return (Double) value != 0;
        if (Float.class.equals(clazz))
            return (Float) value != 0;
        if (BigDecimal.class.equals(clazz))
            return ((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0;
        if (BigInteger.class.equals(clazz))
            return ((BigInteger) value).compareTo(BigInteger.ZERO) != 0;
        return defaultValue;
    }

    /**
     * {@link String}转换为{@link Integer}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link Integer}值。
     * @param value 要转换成{@link Integer}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static Integer toInt(String value, Integer defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        // 字符串中包含小数点，Integer.valueOf会报异常，先转换为BigDecimal，返回其整数部分
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return d.intValue();
            } catch (Exception e) {
                return defaultValue;
            }
        }

        // 当做整数字符串处理
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@link Object}转换为{@link Integer}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defaultValue；
     * <br /> 2) value为{@link Integer}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toInt(String, Integer)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Long}、{@link Float}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其intValue()值；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成{@link Integer}值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null)
            return defaultValue;
        Class<?> cls = value.getClass();
        if (Integer.class.equals(cls))
            return (Integer) value;
        if (value instanceof Number)
            return ((Number) value).intValue();
        if (String.class.equals(cls))
            return toInt((String) value, defaultValue);
        return defaultValue;
    }

    /**
     * {@link String}转换为{@link Long}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link Long}值。
     * @param value 要转换成{@link Long}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static Long toLong(String value, Long defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        // 字符串中包含小数点，Long.valueOf会报异常，先转换为BigDecimal，返回其整数部分
        if (value.indexOf('.') >= 0) {
            try {
                Double d = Double.valueOf(value);
                return d.longValue();
            } catch (Exception e) {
                return defaultValue;
            }
        }

        // 当做整数字符串处理
        try {
            return Long.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@link Object}转换为{@link Long}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defaultValue；
     * <br /> 2) value为{@link Long}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toLong(String, Long)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Float}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其longValue()值；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成{@link Long}值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Long toLong(Object value, Long defaultValue) {
        if (value == null)
            return defaultValue;
        Class<?> cls = value.getClass();
        if (Long.class.equals(cls))
            return (Long) value;
        if (value instanceof Number)
            return ((Number) value).longValue();
        if (String.class.equals(cls))
            return toLong((String) value, defaultValue);
        return defaultValue;
    }

    /**
     * {@link String}转换为{@link Float}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link Float}值。
     * @param value 要转换成{@link Float}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static Float toFloat(String value, Float defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        try {
            return Float.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@link Object}转换为{@link Float}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defaultValue；
     * <br /> 2) value为{@link Float}类型时，不进行类型转换，直接返回value值；
     * <br /> 3) value为{@link String}类型时，参考{@link #toFloat(String, Float)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Double}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其floatValue()值；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成{@link Float}值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Float toFloat(Object value, Float defaultValue) {
        if (value == null)
            return defaultValue;
        Class<?> cls = value.getClass();
        if (Float.class.equals(cls))
            return (Float) value;
        if (value instanceof Number)
            return ((Number) value).floatValue();
        if (String.class.equals(cls))
            return toFloat((String) value, defaultValue);
        return defaultValue;
    }

    /**
     * {@link String}转换为{@link Double}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link Double}值。
     * @param value 要转换成{@link Double}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static Double toDouble(String value, Double defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        try {
            return Double.valueOf(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@link Object}转换为{@link Double}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defaultValue；
     * <br /> 2) value为{@link Double}类型时，不进行类型转换，直接返回value对象；
     * <br /> 3) value为{@link String}类型时，参考{@link #toDouble(String, Double)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link BigDecimal}、{@link BigInteger}
     *           等类型时，返回其doubleValue()值；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成{@link Double}值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null)
            return defaultValue;
        Class<?> cls = value.getClass();
        if (Double.class.equals(cls))
            return (Double) value;
        if (value instanceof Number)
            return ((Number) value).doubleValue();
        if (String.class.equals(cls))
            return toDouble((String) value, defaultValue);
        return defaultValue;
    }

    /**
     * {@link String}转换为{@link BigDecimal}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>value等于null或者为空字符串，或者在转换过程中发生异常，均返回defaultValue，否则返回转换后的{@link BigDecimal}值。
     * @param value 要转换成{@link BigDecimal}值的字符串。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）。
     * @return
     */
    public static BigDecimal toDecimal(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().length() <= 0)
            return defaultValue;

        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * {@link Object}转换为{@link BigDecimal}类型。
     * <p>转换不成功时返回defaultValue并记录info或者warn日志，不会抛出任何异常。
     * <p>
     * 1) value等于null时返回defaultValue；
     * <br /> 2) value为{@link BigDecimal}类型时，不进行类型转换，直接返回value对象；
     * <br /> 3) value为{@link String}类型时，参考{@link #toDecimal(String, BigDecimal)}；
     * <br /> 4) value为{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}、{@link Double}、{@link BigInteger}
     *           等类型时，返回其{@link BigDecimal}值；
     * <br /> 5) 其他情况均返回defaultValue；
     * @param value 要转换成{@link BigDecimal}值的对象。
     * @param defaultValue 转换不成功时的默认值（可以为null，这种情况下转换不成功时将返回null值）
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static BigDecimal toDecimal(Object value, BigDecimal defaultValue) {
        if (value == null)
            return defaultValue;

        Class clazz = value.getClass();
        // 最可能出现的类型放在最前面判断
        if (BigDecimal.class.equals(clazz))
            return ((BigDecimal) value);
        if (Double.class.equals(clazz))
            // Double转BigDecimal，需要使用new Decimal(doubleValue.toString())
            // 如果使用new Decimal(doubleValue)，像89.7会变成89.70000000000000012之类值
            return toDecimal(value.toString(), defaultValue);
        if (Float.class.equals(clazz))
            // 同double
            return toDecimal(value.toString(), defaultValue);
        if (String.class.equals(clazz))
            return toDecimal((String) value, defaultValue);
        if (Integer.class.equals(clazz))
            return new BigDecimal((Integer) value);
        if (Short.class.equals(clazz))
            return new BigDecimal((Short) value);
        if (Byte.class.equals(clazz))
            return new BigDecimal((Byte) value);
        if (Long.class.equals(clazz))
            return new BigDecimal((Long) value);
        return defaultValue;
    }
}