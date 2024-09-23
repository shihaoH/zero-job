package cn.shihh.zerojob.core.enums;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;

/**
 * 参数类型枚举
 * 参照JSON Schema的类型定义，包括字符串、整数、数字、布尔、数组、对象等类型
 * @author shihh
 * @since 2024/9/22
 */
@AllArgsConstructor
public enum TypeClassEnum implements TypeConverter {
    STRING("string", "标准字符串类型") {
        @Override
        public String converter(String value) {
            return value;
        }
    },
    INTEGER("integer", "整数类型") {
        @Override
        public Integer converter(String value) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    },
    NUMBER("number", "数字类型，包含整数、小数、正数、负数") {
        @Override
        public Double converter(String value) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
    },
    BOOLEAN("boolean", "布尔类型，true false") {
        @Override
        public Boolean converter(String value) {
            return BooleanUtil.toBoolean(value);
        }
    },
    ARRAY("array", "数组类型") {
        @Override
        public JSONArray converter(String value) {
            if (JSONUtil.isTypeJSONArray(value)) {
                return JSONUtil.parseArray(value);
            }
            return new JSONArray();
        }
    },
    OBJECT("object", "对象类型") {
        @Override
        public JSONObject converter(String value) {
            if (JSONUtil.isTypeJSONObject(value)) {
                return JSONUtil.parseObj(value);
            }
            return new JSONObject();
        }
    },
    ;
    public final String name;
    public final String desc;

    public static TypeClassEnum getEnum(String name) {
        for (TypeClassEnum typeClassEnum : TypeClassEnum.values()) {
            if (typeClassEnum.name.equals(name)) {
                return typeClassEnum;
            }
        }
        return STRING;
    }
}
