package com.jjy.mybatisPlus;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jjy.mybatisPlus.domain.User;
import com.jjy.mybatisPlus.mapper.UserMapper;
import com.jjy.mybatisPlus.service.UserService;
import org.apache.ibatis.executor.BatchResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class MybatisPlusTest {
    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Test
    void testSelectOne() {
        // 通过id查出唯一的
        User userById = userService.getById(4);
        System.out.println(userById);

        // 通过条件查出唯一的
        User userByCondiction = userService.lambdaQuery().eq(User::getId, 1).eq(User::getUsername, "jjy").one();
        System.out.println(userByCondiction);
    }

    @Test
    void testSelectList() {
        // 通过id查出多个
        List<User> usersByIds = userService.listByIds(List.of(1, 2));


        // 通过条件查出多个
        List<User> usersByCondiction = userService.lambdaQuery()
                .or(i -> i.eq(User::getId, 1))
                .or(i -> i.eq(User::getId, 2))
                .list();
    }


    @Test
    void testCondition() {

        // 如果是 （）or （）类型的
        userService.lambdaQuery()
                .or(i -> i.eq(User::getId, 1).eq(User::getUsername, "jjy"))
                .or(i -> i.eq(User::getId, 2).eq(User::getUsername, "jjy"))
                .list();


        // （or） and （or）
        userService.lambdaQuery()
                .and(i -> i.or().or())
                .and(i -> i.or().or())
                .list();
    }

    @Test
    void testSelectPage() {
        // 使用mp的分页插件 ，做到分页查询
        // 无条件分页
        Page page = Page.of(1, 1);  // 前参数为当前页号，后参数为每页显示的条数
        System.out.println(userService.page(page));

        // 有条件分页 选出满足wrapper条件的数据然后分页
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(User::getUsername, "jjy1");
        System.out.println(userService.page(page, wrapper));
    }

    @Test
    void testUpdateOne() {
        userService.update();
        userService.lambdaUpdate().eq(User::getUsername, "jjy").set(User::getPassword, "123456").update();
    }

    @Test
    @Transactional
    void testUpdateBatch() {
        List<User> list = List.of(User.builder().id(1).username("jjy1").build());
        List<BatchResult> batchResults = userService.getBaseMapper().updateById(
                list
        );
        batchResults.forEach(result -> {

            System.out.println(result.getUpdateCounts().length);
        });
    }


    // 删除都是基于mp的逻辑删除 如果已经标记为删除，还是可以执行成功
    @Test
    void testDeleteOne() {
        // 根据id删除
        boolean b = userService.removeById(3);
        System.out.println(b);
    }


    @Test
    void testDeleteBatch() {
        userService.removeBatchByIds(List.of(1,2,3));
    }
}
