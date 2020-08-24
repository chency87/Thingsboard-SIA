package org.thingsboard.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="org.jcasbin")
@Data
public class EnforcerConfigProperties {
    private String url;

    private String driverClassName;

    private String username;

    private String password;

    private String modelPath;

}
