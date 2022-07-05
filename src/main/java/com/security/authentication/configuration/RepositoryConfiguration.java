package com.security.authentication.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.security.authentication.persistence.repository")
public class RepositoryConfiguration {
}
