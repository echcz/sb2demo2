package com.github.echcz.sb2demo2.domain;

import com.github.echcz.sb2demo2.constant.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorVo {
    public static ErrorVo fromErrorStatus(ErrorStatus errorStatus) {
        return new ErrorVo(errorStatus.getCode(), errorStatus.getMsg());
    }

    private int errCode;
    private String errMsg;
}
