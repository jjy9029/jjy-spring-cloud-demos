package com.jjy.mybatisPlus.domain;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "username")
    private String username;
    private String password;
    @TableLogic // 用作逻辑删除
    private  Integer deleted;
}
