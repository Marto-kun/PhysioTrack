package main.java.com.physiotrack;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"main.java", "services", "repositories", "com.physiotrack"})
@EnableMongoRepositories(basePackages = {"main.java.repositories"})
@Push
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        // Cargar variables de entorno desde .env
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

        // Establecer la variable de entorno de MongoDB
        if (dotenv.get("MONGODB_URI") != null) {
            System.setProperty("MONGODB_URI", dotenv.get("MONGODB_URI"));
        }

        SpringApplication.run(Application.class, args);
    }
}
