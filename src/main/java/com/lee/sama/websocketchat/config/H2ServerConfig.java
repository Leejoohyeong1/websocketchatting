package com.lee.sama.websocketchat.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
public class H2ServerConfig {

    @Bean
    public Server H2DatabaseServer() throws SQLException {
        //jdbc:h2:tcp://localhost:9092/mem:testdb;OLD_INFORMATION_SCHEMA=TRUE
        return  Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
    }
}

