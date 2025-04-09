package com.jjy.swagger.domain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User")  //类上的Schema注解,会在swagger-ui上生成 这个类的描述信息
public class User {
    // 加在属性的上的Schema注解,会在swagger-ui上生成 属性的描述信息，但是要类上加上Schema注解才会生效
    @Schema(name = "id",description = "用户id",type= "Integer",example = "1")
    private Integer id;
}
