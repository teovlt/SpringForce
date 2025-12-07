package fr.imt.springforce;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModuleIntegrationTest {

    @Test
    void verifyModuleDependencies() {
        ApplicationModules modules = ApplicationModules.of(SpringForceApplication.class);
        modules.forEach(System.out::println);
        // This test will FAIL if your 'ordering' module
        // tries to import 'customer.domain.Customer' (a hidden class).
        // It will PASS if 'ordering' only imports 'customer.api.CustomerDetails'.
        ApplicationModules.of(SpringForceApplication.class).verify();
    }

}
