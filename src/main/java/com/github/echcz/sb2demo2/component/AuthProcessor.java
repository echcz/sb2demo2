package com.github.echcz.sb2demo2.component;

import javax.servlet.http.HttpServletRequest;

public interface AuthProcessor {
    String METHOD_WILDCARD = "any";
    String USER_ID_HEADER_NAME = "echcz-user-id";
    String ROLES_HEADER_NAME = "echcz-roles";

    /**
     * 用于安全认证的处理器
     *
     * @param request 请求
     * @return 处理后的请求
     * @throws com.github.echcz.sb2demo2.exception.HttpClientException 如果认证不通过，抛出此异常
     */
    HttpServletRequest process(HttpServletRequest request);
}
