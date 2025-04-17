package com.jjy.controllerExample.common;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// controller的返回值对象 一定需要有getters和setters方法，否则无法序列化
@Data
public class ApiResult {
    private Object data;
    private Integer code;
    private String msg;
    public ApiResult(Object data, Integer code, String msg){
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public ApiResult (Object data, Integer code){
        this.data = data;
        this.code = code;
    }
}
