package projekt.wypozyczalnia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/product/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/cart/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/user/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/order/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/reports/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
        registry.addMapping("/reservation/**")
                .allowedOrigins("http://localhost:8080", "http://localhost:63342")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*");
    }
}
