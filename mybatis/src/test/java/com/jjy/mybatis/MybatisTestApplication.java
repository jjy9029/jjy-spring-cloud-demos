package com.jjy.mybatis;
import com.jjy.mybatis.domain.User;
import com.jjy.mybatis.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
public class MybatisTestApplication {
    @Autowired
    private UserMapper userMapper;
    @Test
    void testSelectbyId(){
        System.out.println(userMapper.selectById(1));
    }


    @Test
    void testInsert(){
        User user = User.builder().username("jjy").password("123").build();
        userMapper.insertUser(user);
    }
}
