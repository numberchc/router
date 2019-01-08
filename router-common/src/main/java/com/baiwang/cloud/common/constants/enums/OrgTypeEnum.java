package com.baiwang.cloud.common.constants.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 组织机构类型
 */
public enum OrgTypeEnum {

    ALL("0", "全部"),
    TAX("1", "纳税主体"),
    ORG("2", "非纳税主体");

    private String code;
    private String msg;

    /**
     * 构造器<br>
     * 用于初始枚举实例的代码和信息
     *
     * @param code
     * @param msg
     */
    OrgTypeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获得枚举实例代码
     *
     * @return 代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 获得枚举实例代码
     *
     * @return 代码
     */
    public int getIntCode() {
        return Integer.valueOf(code);
    }

    /**
     * 获得枚举实例信息
     *
     * @return 信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 根据code获得枚举实例
     *
     * @param code 码
     * @return 枚举实例
     */
    public static OrgTypeEnum getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (OrgTypeEnum e : OrgTypeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
    /**
     * 根据code获得枚举实例
     *
     * @param code 代码
     * @return 枚举实例
     */
    public static OrgTypeEnum getByCode(int code) {
        return getByCode(String.valueOf(code));
    }

    public static String getCodes() {
        StringBuffer codes = new StringBuffer();
        for (OrgTypeEnum e : OrgTypeEnum.values()) {
            if (codes.length() > 0) {
                codes.append(",");
            }
            codes.append(e.getCode());
        }
        return codes.toString();
    }

}
