package com.github.echcz.sb2demo2.controller;

import com.github.echcz.sb2demo2.component.AuthProcessor;
import com.github.echcz.sb2demo2.config.AdminProperties;
import com.github.echcz.sb2demo2.constant.ErrorStatus;
import com.github.echcz.sb2demo2.domain.LoginReq;
import com.github.echcz.sb2demo2.exception.HttpClientException;
import io.jsonwebtoken.JwtBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Objects;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    @NonNull
    private Supplier<JwtBuilder> jwtBuilderSupplier;
    @NonNull
    private AdminProperties adminProperties;

    @PostMapping("/login")
    public String login(@RequestBody LoginReq req) {
        if (Objects.equals(adminProperties.getUsername(), req.getUsername())
                && Objects.equals(adminProperties.getPassword(), req.getPassword())) {
            String userId = adminProperties.getUserId().toString();
            String username = adminProperties.getUsername();
            String roles = String.join(",", adminProperties.getRoles());
            log.info("userId={};username={};roles={}", userId, username, roles);
            return jwtBuilderSupplier.get()
                    .setAudience(userId)
                    .setSubject(roles)
                    .compact();
        }
        throw new HttpClientException(HttpStatus.UNAUTHORIZED, ErrorStatus.UNAUTHORIZED.getCode(), "用户名或密码错误");
    }

    @GetMapping("/show-me")
    public String showMe(@ApiIgnore @RequestHeader(AuthProcessor.USER_ID_HEADER_NAME) String userId,
                         @ApiIgnore @RequestHeader(AuthProcessor.ROLES_HEADER_NAME) String roles) {
        return String.format("userId=%s&roles=%s", userId, roles);
    }

}
