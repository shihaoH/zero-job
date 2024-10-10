package cn.shihh.zerojob;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shihh
 * @since 2024/10/9
 */
@EnableScheduling
@SpringBootApplication
@MapperScan("cn.shihh.zerojob.spring.mapper")
public class ZeroJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZeroJobApplication.class, args);
    }
}
