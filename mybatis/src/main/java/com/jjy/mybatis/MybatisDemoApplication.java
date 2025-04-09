package com.jjy.mybatis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@Slf4j
public class MybatisDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisDemoApplication.class,args);
        log.info("启动成功了！！！");
    }
}
