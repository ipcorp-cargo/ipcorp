package kz.ipcorp.controller;

import kz.ipcorp.util.FileManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/licenses")
public class LicenseController {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String addLicense(@RequestPart("license") MultipartFile license) throws IOException {
        final String subFolder = "/license";
        String hashStringLicense = FileManager.hashFile(license.getInputStream());
        return FileManager.saveFile(
                license.getInputStream(),
                subFolder.concat("/".concat(hashStringLicense.substring(0,2))),
                hashStringLicense.substring(2));
    }

    @GetMapping
    public ResponseEntity<byte[]> getLicense(@RequestParam("licensePath") String licensePath) throws IOException {
        return new ResponseEntity<>(FileManager.getFile(licensePath), HttpStatus.OK);
    }
}