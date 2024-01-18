package kz.ipcorp.controller;

import kz.ipcorp.util.FileManager;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/license")
public class LicenseController {

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addLicense(@RequestPart("license") MultipartFile license) throws IOException {
        final String subFolder = "/license";
        String hashStringLicense = FileManager.hashFile(license.getInputStream());
        return FileManager.saveFile(
                license.getInputStream(),
                subFolder.concat("/".concat(hashStringLicense.substring(0,2))),
                hashStringLicense.substring(2));
    }

    @GetMapping("/download")
    ResponseEntity<Resource> getLicense(@RequestParam("licensePath") String licensePath) throws IOException {
        return new ResponseEntity<>(FileManager.loadFile(licensePath), HttpStatus.OK);
    }
}