package amor.library.library;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Barcha endpointlarga ruxsat
                .allowedOrigins("http://localhost:5173") // Frontend domeni
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Ruxsat berilgan HTTP metodlar
                .allowedHeaders("*") // Ruxsat berilgan headerlar
                .allowCredentials(true); // Cookie va autentifikatsiya uchun ruxsat
    }
}
