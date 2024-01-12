package kz.ipcorp.controller;


import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import util.FileManager;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @PostMapping(path = "/put", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addImage(@RequestParam("icon") MultipartFile icon) throws IOException {
        System.out.println("qwerty");
        System.out.println("icon: " + icon.getOriginalFilename());
        final String subFolder = "/icon";
        System.out.println(icon.getInputStream());
        String hashStringIcon = FileManager.hashFile(icon.getInputStream());

        return FileManager.saveFile(
                icon.getInputStream(),
                subFolder.concat("/".concat(hashStringIcon.substring(0,2))),
                hashStringIcon.substring(2));
    }
}
