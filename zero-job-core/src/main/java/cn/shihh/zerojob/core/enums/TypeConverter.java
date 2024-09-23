package cn.shihh.zerojob.core.enums;

/**
 * 类型转换器接口
 * @author shihh
 * @since 2024/9/22
 */
@FunctionalInterface
public interface TypeConverter {
    <T> T converter(String value);
}
