package ir.tiroon.notes

import ir.tiroon.notes.model.Role
import ir.tiroon.notes.service.RoleServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class Application implements CommandLineRunner, WebMvcConfigurer {

    static void main(String[] args) {
        SpringApplication.run Application, args
    }

    @Autowired
    RoleServices roleServices

    @Override
    void run(String... args) throws Exception {

        if (roleServices.getByName("USER") == null) {
            def r1 = new Role()
            r1.setRoleId(1)
            r1.setRoleName("USER")
            r1.setDescription("Common system users")
            roleServices.save(r1)
        }

        if (roleServices.getByName("ADMIN") == null) {
            def r2 = new Role()
            r2.setRoleId(2)
            r2.setRoleName("ADMIN")
            r2.setDescription("Administrators")
            roleServices.save(r2)
        }
    }

    private static final def RESOURCE_LOCATIONS = ["classpath:/META-INF/resources/", "classpath:/resources/",
                                                   "classpath:/static/", "classpath:/public/"]

    void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(RESOURCE_LOCATIONS);
        }
//        registry.addResourceHandler("/view/**")
//                .addResourceLocations('file:/home/mmamini/outside-resources/')
    }


}
