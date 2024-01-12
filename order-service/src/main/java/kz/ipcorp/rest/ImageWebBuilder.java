package kz.ipcorp.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.io.InputStream;

@Slf4j
@Component
public class ImageWebBuilder {

    private static final String BASE_URL ="http://localhost:8765/media-service/image";
//    public Byte[] imageGetAsync(String iconPath){
//        Flux<Byte> image = WebClient.create(BASE_URL)
//                .post()
//                .uri("get")
//                .bodyValue(iconPath)
//                .retrieve()
//                .bodyToFlux(Byte.class);
//    }

    public Flux<String> imagePutAsync(MultipartFile icon){

        Flux<String> iconPath = null;
        try {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("icon", icon.getResource());

            System.out.println("it's working");
            iconPath = WebClient.create(BASE_URL)
                    .post()
                    .uri("/put")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(builder.build()))
                    .retrieve()
                    .bodyToFlux(String.class);

            System.out.println("it's workeddd");
        }catch (Exception e){
            log.error("Exception WebClient: {}", e.getMessage());
            e.fillInStackTrace();
        }
        return iconPath;
    }
}
