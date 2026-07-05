package main.java.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import java.util.Properties;

/**
 * Configuración para cargar las variables de entorno desde .env
 *
 * @author [Martín Vozmediano]
 * @version 1.0
 * @since 2026-07-04
 */
@Configuration
public class EnvironmentConfiguration {

    static {
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

        if (dotenv.get("MONGODB_URI") != null) {
            System.setProperty("spring.data.mongodb.uri", dotenv.get("MONGODB_URI"));
        }
    }

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        Properties properties = new Properties();

        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

        if (dotenv.get("MONGODB_URI") != null) {
            properties.put("spring.data.mongodb.uri", dotenv.get("MONGODB_URI"));
        }

        configurer.setProperties(properties);
        return configurer;
    }
}

