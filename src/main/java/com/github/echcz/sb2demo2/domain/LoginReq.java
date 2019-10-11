package com.github.echcz.sb2demo2.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("登录请求体")
public class LoginReq {
    @ApiModelProperty("用户名")
    @NotNull
    @Pattern(regexp = "\\w{4,16}")
    private String username;
    @ApiModelProperty("密码")
    @NotNull
    @Size(min = 4, max = 16)
    private String password;
}
