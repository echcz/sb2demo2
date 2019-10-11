package com.github.echcz.sb2demo2.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户")
public class User {
    @ApiModelProperty(value = "用户id", readOnly = true)
    private Integer id;
    @ApiModelProperty("用户名")
    @NotNull(message = "用户名不能为空")
    @Pattern(regexp = "\\w{4,24}", message = "用户名格式不正确")
    private String username;
    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 14, message = "密码格式不正确")
    private String password;
    @ApiModelProperty("邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    @ApiModelProperty(value = "创建时间", readOnly = true)
    private LocalDateTime createTime;
}
