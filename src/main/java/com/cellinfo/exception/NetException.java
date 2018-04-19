package com.cellinfo.exception;

import com.cellinfo.enums.ResultEnum;

public class NetException extends RuntimeException{

    private Integer code;

    public NetException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
