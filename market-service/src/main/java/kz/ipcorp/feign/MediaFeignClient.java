package kz.ipcorp.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "media-service", configuration = FeignSupportConfig.class)
public interface MediaFeignClient {
    @PostMapping(path = "api/licenses", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String getPathName(@RequestPart("license") MultipartFile license);

    @PostMapping(path = "api/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String getPath(@RequestPart("image") MultipartFile image);

    @PostMapping(path = "api/icons", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String getPathCategoryIcon(@RequestPart("icon") MultipartFile icon);
}
