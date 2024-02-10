package kz.ipcorp.controller;

import kz.ipcorp.util.FileManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/products")
public class ImageController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addImage(@RequestPart("image") MultipartFile image) throws IOException {
        final String subFolder = "/image";
        String hashStringIcon = FileManager.hashFile(image.getInputStream());
        return FileManager.saveFile(
                image.getInputStream(),
                subFolder.concat("/".concat(hashStringIcon.substring(0, 2))),
                hashStringIcon.substring(2));
    }

    @GetMapping
    public ResponseEntity<byte[]> getImage(@RequestParam("imagePath") String imagePath) throws IOException {
        return new ResponseEntity<>(FileManager.getFile(imagePath), HttpStatus.OK);
    }
}