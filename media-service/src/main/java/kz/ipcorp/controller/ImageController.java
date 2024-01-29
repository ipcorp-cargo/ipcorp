package kz.ipcorp.controller;

import kz.ipcorp.util.FileManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ImageController {

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> addImage(@RequestPart("images") List<MultipartFile> images) throws IOException {
        final String subFolder = "/image";
        List<String> paths = new ArrayList<>();
        for (MultipartFile image : images) {
            String hashStringIcon = FileManager.hashFile(image.getInputStream());
            paths.add(FileManager.saveFile(
                    image.getInputStream(),
                    subFolder.concat("/".concat(hashStringIcon.substring(0, 2))),
                    hashStringIcon.substring(2)));
        }
        return paths;
    }

    @GetMapping("/download")
    public List<byte[]> getImage(@RequestParam("paths") List<String> paths) throws IOException {
        List<byte[]> images = new ArrayList<>();
        for(String path : paths){
            images.add(
                    FileManager.getFile(path)
            );
        }
        return images;
    }
}