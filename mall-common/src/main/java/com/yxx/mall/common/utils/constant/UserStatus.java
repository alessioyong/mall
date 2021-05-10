package com.yxx.mall.common.utils.constant;

/**
 * @author xyong
 * date 2021-05-10
 */
public enum  UserStatus {

    OK("0", "正常"), DISABLE("1", "停用"), DELETED("2", "删除");

    private final String value;
    private final String info;

    UserStatus(String value, String info)
    {
        this.value = value;
        this.info = info;
    }

    public String getValue()
    {
        return value;
    }

    public String getInfo()
    {
        return info;
    }
}
