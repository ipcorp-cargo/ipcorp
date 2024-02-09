package kz.ipcorp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import kz.ipcorp.util.FileManager;

import java.io.IOException;

@RestController
@RequestMapping("api/icons")
public class IconController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addIcon(@RequestPart("icon") MultipartFile icon) throws IOException {
        final String subFolder = "/icon";
        String hashStringIcon = FileManager.hashFile(icon.getInputStream());
        return FileManager.saveFile(
                icon.getInputStream(),
                subFolder.concat("/".concat(hashStringIcon.substring(0,2))),
                hashStringIcon.substring(2));
    }

    @GetMapping
    ResponseEntity<byte[]> getIcon(@RequestParam("iconPath") String iconPath) throws IOException {
        return new ResponseEntity<>(FileManager.getFile(iconPath), HttpStatus.OK);
    }
}