package com.github.echcz.sb2demo2.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private AuthPath authPath = new AuthPath();

    @NoArgsConstructor
    public static class AuthPath {
        /**
         * 格式为: {methods} {ant-path-pattern}
         * 例如: GET,POST /exp/**
         */
        @Getter
        private List<String> whiteList = new ArrayList<>();
        /**
         * 格式为: {methods} {ant-path-pattern}
         * 例如: GET,POST /exp/**
         */
        @Getter
        private List<String> blackList = new ArrayList<>();
        /**
         * 格式为: {methods} {ant-path-pattern}
         * 例如: GET,POST /exp/**
         */
        @Getter
        private List<String> checkList = new ArrayList<>();
        /**
         * 格式为: {methods} {ant-path-pattern} {roles}
         * 例如: GET,POST /exp/** user,admin
         */
        @Getter
        private List<String> roleList = new ArrayList<>();

        public void setWhiteList(List<String> whiteList) {
            this.whiteList = new ArrayList<>(whiteList.size());
            for (String s : whiteList) {
                this.whiteList.add(s.trim().toLowerCase());
            }
        }

        public void setBlackList(List<String> blackList) {
            this.blackList = new ArrayList<>(blackList.size());
            this.blackList = new ArrayList<>();
            for (String s : blackList) {
                this.blackList.add(s.trim().toLowerCase());
            }
        }

        public void setCheckList(List<String> checkList) {
            this.checkList = new ArrayList<>(checkList.size());
            for (String s : checkList) {
                this.checkList.add(s.trim().toLowerCase());
            }
        }

        public void setRoleList(List<String> roleList) {
            this.roleList = new ArrayList<>(roleList.size());
            for (String s : roleList) {
                this.roleList.add(s.trim().toLowerCase());
            }
        }
    }
}
