package com.cellinfo.enums;

public enum ResultEnum {
	
    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    INVALIDUSER(100, "无效用户"),
    VALIDUSER(101, "有效用户"), 
    EXPIREDJWT(102,"登录过期，需要重新登录"),

    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
