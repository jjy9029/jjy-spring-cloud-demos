package com.jjy.controllerExample.controller;

import com.jjy.controllerExample.common.ApiResult;
import com.jjy.controllerExample.domain.Example;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

@RestController
@RequestMapping("/example")
@CrossOrigin("*")
@Tag(name = "ExampleController",description = "rest风格接口样例")
public class ExampleController {
    @Value("${jjy.db.username}")
    private String username;
    @GetMapping(value = "/get/{id}")
    public ApiResult getOneById(@PathVariable Integer id){
        String res = "get id: " + id+username;
        return new ApiResult(res,1631001,"success");
    }


    @GetMapping(value = "/getObject")
    public ApiResult getByObject(Example example){
        return new ApiResult(example,1631001,"success");
    }



    @PostMapping("/postObject")
    public ApiResult postByObject(@RequestBody Example example){
        return new ApiResult(example,1631001,"success");
    }



    @PostMapping(value = "/postMultipartFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult postByMultipartFile(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getInputStream().readAllBytes();
        return new ApiResult(bytes,1631001,"success");
    }


    @PutMapping("/putObject")
    public ApiResult putByObject(@RequestBody Example example){
        return new ApiResult(example,1631001,"success");
    }



    @DeleteMapping("/delete/{id}")
    public ApiResult deleteById(@PathVariable Integer id) {
        String res = "delete id: " + id;
        return new ApiResult(res, 1631001, "success");
    }

    @DeleteMapping("/deleteList/{ids}")
    public ApiResult deleteByList(@PathVariable List<Integer> ids){
        StringJoiner joiner = new StringJoiner(",");
        ids.forEach(item->{
            joiner.add(item.toString());
        });
        System.out.println(joiner);
        return new ApiResult(joiner.toString(),1631001,"success");
    }




}
