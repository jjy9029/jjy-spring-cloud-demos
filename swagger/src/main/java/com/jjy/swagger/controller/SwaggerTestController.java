package com.jjy.swagger.controller;

import com.jjy.swagger.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apiguardian.api.API;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "测试接口",description = "用户测试接口") // 标注controller类
@RestController
@RequestMapping("/swagger")
public class SwaggerTestController {
    @Operation( summary = "添加用户", description = "添加用户")
    @Parameters(
            {
                    @Parameter(name = "id", description = "用户id", example = "1", required = true),
                    @Parameter(name = "username",description= "用户名", example = "jjy", required = true)
            }
    )
    @GetMapping("/test1/{id}")
    public Map<String, Object> test1(@PathVariable int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", "jjy");
        return map;
    }


    @PostMapping(value = "/multi", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String mul(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        byte[] bytes = file.getInputStream().readAllBytes();
        return new String(bytes);
    }


    @GetMapping(value = "/get", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String get(@RequestParam User user) {
        return "get";
    }

}
