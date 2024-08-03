package org.netmen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.netmen.dao.mapper")
public class NetRecruitmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetRecruitmentApplication.class, args);
    }

}
