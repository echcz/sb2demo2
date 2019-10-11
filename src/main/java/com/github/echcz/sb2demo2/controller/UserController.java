package com.github.echcz.sb2demo2.controller;

import com.github.echcz.sb2demo2.config.CommonConfig;
import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.domain.User;
import com.github.echcz.sb2demo2.exception.HttpClientException;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    private static Map<Integer, User> userMap = new HashMap<>();
    private static int cur;

    static {
        userMap.put(1, User.builder().id(1).username("jack").password("123456").email("jack@xx.com")
                .createTime(LocalDateTime.parse("2019-10-03 12:32:43", CommonConfig.DATE_TIME_FMT)).build());
        cur = 1;
    }

    @ApiOperation("查询所有用户")
    @GetMapping("")
    public Collection<User> getAll() {
        return userMap.values();
    }

    @ApiOperation("根据id查询1个用户")
    @GetMapping("/{id}")
    public User getOne(@PathVariable("id") Integer id) {
        User user = userMap.get(id);
        if (user == null) {
            throw new HttpClientException(HttpStatus.NOT_FOUND, ErrorStatus.NOT_FOUND.getCode(), ErrorStatus.NOT_FOUND.getMsg());
        }
        return user;
    }

    @ApiOperation("添加1个用户")
    @PostMapping("/0")
    public ResponseEntity addOne(@Validated @RequestBody User user) {
        user.setId(++cur);
        user.setCreateTime(LocalDateTime.now());
        userMap.put(cur, user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation("修改一个用户")
    @PutMapping("/{id}")
    public ResponseEntity editOne(@PathVariable("id") Integer id, @Validated @RequestBody User user) {
        if (userMap.containsKey(id)) {
            user.setId(id);
            userMap.put(id, user);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("根据id删除1个用户")
    @DeleteMapping("/{id}")
    public ResponseEntity delOne(@PathVariable("id") Integer id) {
        userMap.remove(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
