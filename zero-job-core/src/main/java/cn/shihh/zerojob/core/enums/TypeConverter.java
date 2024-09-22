package cn.shihh.zerojob.core.enums;

/**
 * @author shihh
 * @since 2024/9/22
 */
@FunctionalInterface
public interface TypeConverter {
    <T> T converter(String value);
}
