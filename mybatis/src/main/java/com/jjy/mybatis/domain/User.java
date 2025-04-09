package com.jjy.mybatis.domain;
import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName
public class User {
    private Integer id;
    private String username;
    private String password;
}
