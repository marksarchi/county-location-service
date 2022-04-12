package com.marksarchi.countylocationservice.utils;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = DataBaseIT.DockerPostgresDataSourceInitializer.class)
public abstract class DataBaseIT {
    private static String POSTGIS_DB_IMAGE = "postgis/postgis:12-2.5-alpine";
    private static String POSTGRES_DB_IMAGE = "postgres";

    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>(
            DockerImageName.parse(POSTGIS_DB_IMAGE).asCompatibleSubstituteFor(POSTGRES_DB_IMAGE)
    );

    static {
        postgreDBContainer.start();
    }

    public static class DockerPostgresDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreDBContainer.getUsername(),
                    "spring.datasource.password=" + postgreDBContainer.getPassword()
            );
        }
    }

}
