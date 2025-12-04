package fr.imt.springforce;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "SpringForce API", version = "1.0", description = "Documentation SpringForce API v1.0"))
public class SpringForceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringForceApplication.class, args);
    }

}
