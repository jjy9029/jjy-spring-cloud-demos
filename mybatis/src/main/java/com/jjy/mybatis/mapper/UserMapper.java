package com.jjy.mybatis.mapper;
import com.jjy.mybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
     //根据id查询
    public User selectById(@Param("id") int id); // @param注解用于指定参数xml中的名称


    // 根据条件 动态查询
    public User selectByUser(User user);

    //动态插入——满足条件的属性插入到数据库中
    public boolean insertUser(User user);

    // 动态更新，满足条件的属性更新到数据库中
    public boolean updateId(User user);

    //根据id删除
    public boolean deleteById(int id);

    //id批量删除
    public boolean deleteByList(@Param("list")List<Integer> list);


}
