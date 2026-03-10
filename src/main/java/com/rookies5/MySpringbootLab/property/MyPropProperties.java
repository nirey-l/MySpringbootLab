package com.rookies5.MySpringbootLab.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myprop") // application.properties의 'myprop.xxx' 값들을 가져옵니다.
public class MyPropProperties {

    private String username;
    private int port;

    // ⚠️ 중요: @ConfigurationProperties를 쓸 때는 반드시 Getter와 Setter가 있어야 작동합니다!
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}