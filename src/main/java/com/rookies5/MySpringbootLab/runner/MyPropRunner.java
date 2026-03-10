package com.rookies5.MySpringbootLab.runner;

import com.rookies5.MySpringbootLab.config.MyEnvironment;
import com.rookies5.MySpringbootLab.property.MyPropProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements ApplicationRunner {

    // 1. SLF4J 로거 생성 (클래스 이름이 MyPropRunner이므로 그대로 맞춰줍니다)
    private static final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Autowired
    private MyPropProperties myPropProperties;

    @Autowired
    private MyEnvironment myEnvironment;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // INFO 레벨: 운영(prod)과 테스트(test) 환경 모두에서 기본적으로 출력됩니다.
        logger.info("=========================================");
        logger.info("🚀 현재 활성화된 모드 : {}", myEnvironment.getMode());
        logger.info("👤 프로퍼티 객체 유저명 : {}", myPropProperties.getUsername());

        // DEBUG 레벨: 로깅 레벨을 DEBUG로 설정한 환경(test)에서만 출력됩니다.
        logger.debug("🐛 디버그 로그 - 프로퍼티 객체 포트번호 : {}", myPropProperties.getPort());
        logger.info("=========================================");
    }
}