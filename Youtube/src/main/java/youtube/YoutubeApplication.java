package youtube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import youtube.model.services.EmailService;
import youtube.model.utils.VideoWithoutPathCleaner;

import java.util.Collection;
import java.util.Collections;

@SpringBootApplication
//@EnableSwagger2
public class YoutubeApplication {
    public static void main(String[] args) {
        SpringApplication.run(YoutubeApplication.class, args);
    }

    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Youtube API",
                "Every endpoint and request sample",
                "1.0",
                null,
                null,
                null,
                null,
                Collections.emptyList()
        );
    }
}
