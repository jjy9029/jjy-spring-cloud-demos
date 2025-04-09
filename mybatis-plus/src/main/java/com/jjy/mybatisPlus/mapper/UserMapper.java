package com.jjy.mybatisPlus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jjy.mybatisPlus.domain.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    public int deleteByList(List<Integer> list);

}
