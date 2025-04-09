package com.jjy.controllerExample.common;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// controller的返回值对象 一定需要有getters和setters方法，否则无法序列化
@Data
@Schema(name = "ApiResult", description = "API返回结果对象")
public class ApiResult {
    @Schema(name = "data", description = "返回数据",example = "obj",type = "Object")
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
