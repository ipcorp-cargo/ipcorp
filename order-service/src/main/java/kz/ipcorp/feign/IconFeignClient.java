package kz.ipcorp.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "media-service", configuration = FeignSupportConfig.class)
public interface IconFeignClient {
    @PostMapping(path = "api/icons/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String getPathName(@RequestPart("icon") MultipartFile icon);
}
