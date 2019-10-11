package com.github.echcz.sb2demo2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.PushBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloController {

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @GetMapping("/demo")
    public void http2ServerPush(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PushBuilder pushBuilder = request.newPushBuilder();
        pushBuilder
                .path("/demo.jpg")
                .push();
        try(PrintWriter respWriter = response.getWriter()){
            respWriter.write("<html><img src='/demo.jpg'></html>");
        }
    }

    @GetMapping(value = "/demo.jpg")
    public void demoJpg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("{} is calling demo.jpg", request.getRemoteAddr());
        InputStream data = getClass().getClassLoader().getResourceAsStream("demo.jpg");
        response.setHeader("content-type", "image/jpg");
        FileCopyUtils.copy(data,response.getOutputStream());
    }
}
